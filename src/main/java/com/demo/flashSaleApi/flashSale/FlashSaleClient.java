package com.demo.flashSaleApi.flashSale;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.flashSaleApi.LockService.ILockService;
import com.demo.flashSaleApi.model.Order;
import com.demo.flashSaleApi.model.Product;
import com.demo.flashSaleApi.model.Registration;
import com.demo.flashSaleApi.pojo.OrderStatus;
import com.demo.flashSaleApi.pojo.PurchaseResult;
import com.demo.flashSaleApi.pojo.RegistrationStatus;
//import com.demo.flashSaleApi.predicates.RegistrationPredicates;
import com.demo.flashSaleApi.repository.BuyerRepository;
import com.demo.flashSaleApi.repository.InventoryRepository;
import com.demo.flashSaleApi.repository.OrderRepository;
import com.demo.flashSaleApi.repository.RegistrationRepository;

import com.demo.flashSaleApi.util.Constants;
import com.google.common.collect.Lists;

@Service(value = "flashSaleClient")
public class FlashSaleClient implements IflashSaleClient{
	
	
	private final ExecutorService EXECUTOR_SERVICE;
	
	@Autowired
    private ILockService lockService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private InventoryRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

   @Autowired
    private OrderRepository orderRepository;
    
    /*@Autowired
    private RegistrationPredicates registrationPredicate;*/

    public FlashSaleClient() {
    	 EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
    }

    @Override
    public PurchaseResult purchase(final Integer flashsaleId, final Integer productId, final Integer buyerId) {

        final String productKey = Constants.PRODUCT_CACHE_PREFIX + ":" + flashsaleId + "." + productId;
        final String buyerKey = Constants.BUYER_CACHE_PREFIX + ":" + flashsaleId + "." + buyerId;
        final List<String> watchKeys = Lists.newArrayList(productKey, buyerKey);
        final Long end = System.currentTimeMillis() + Constants.BUY_TIMEOUT.longValue() * 1000 * 1000 + 1000;

        final PurchaseResult purchaseResult = new PurchaseResult(Boolean.FALSE, buyerId, productId);

        Future<PurchaseResult> future = EXECUTOR_SERVICE.submit(new Callable<PurchaseResult>() {
            @Override
            public PurchaseResult call() throws Exception {

                // We will exit if the max retry interval has passed
                while (System.currentTimeMillis() < end) {
                    final String readLock = lockService.acquireLockWithTimeout(Constants.ELIGIBILITY_LOCKNAME,
                            Constants.LOCK_ACQUIRE_TIMEOUT, Constants.ELIGIBILITY_LOCK_TIMEOUT);
                    if (readLock == null) {
//                        logger.info("Could not obtain readLock, buyer: " + buyerId);
                        Thread.sleep(Constants.PURCHASE_CACHE_CYCLE_SLEEP);
                        continue;
                    }
                    final String writeLock = lockService.acquireLockWithTimeout(Constants.BUY_LOCKNAME,
                            Constants.LOCK_ACQUIRE_TIMEOUT, Constants.BUY_LOCK_TIMEOUT);

                    if (writeLock == null) {
//                        logger.info("Could not obtain writeLock, buyer: " + buyerId);
                        lockService.releaseLock(Constants.ELIGIBILITY_LOCKNAME, readLock);
                        Thread.sleep(Constants.PURCHASE_CACHE_CYCLE_SLEEP);
                        continue;
                    }

                    // Make the transaction only if we have both locks
                    return redisTemplate.execute(new SessionCallback<PurchaseResult>() {
                        @Override
                        public PurchaseResult execute(RedisOperations operations) throws DataAccessException {
                            try {
                                operations.watch(watchKeys);

                                final Integer remainingSKU = (Integer) operations.opsForValue().get(productKey);
                                final Boolean buyerStatus = (Boolean) operations.opsForValue().get(buyerKey);

                                if (buyerStatus != null && buyerStatus && remainingSKU != null && remainingSKU > 0) {
                                    
                                    operations.multi();
                                    final Integer changedSKU = remainingSKU - 1;
                                    operations.opsForValue().set(productKey, changedSKU);
                                    operations.delete(buyerKey);
                                    operations.exec();
                                    operations.unwatch();
                                    
                                    purchaseResult.setStatus(Boolean.TRUE);

                                    persistPurchase(changedSKU, flashsaleId, buyerId, productId);
                                    return purchaseResult;
                                } else {
                                    operations.unwatch();
//                                    logger.info("Can't buy for this buyer: " + buyerId);
                                    return purchaseResult;
                                }
                            } finally {
                                lockService.releaseLock(Constants.ELIGIBILITY_LOCKNAME, readLock);
                                lockService.releaseLock(Constants.BUY_LOCKNAME, writeLock);
                            }
                        }
                    });
                }
                return purchaseResult;
            }
        });

        try {
            return future.get(Constants.BUY_TIMEOUT.longValue(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
         //   logger.error("Purchase interrupted", e);
        } catch (ExecutionException e) {
          //  logger.error("Purchase interrupted", e);
        } catch (TimeoutException e) {
          //  logger.error("Purchase interrupted", e);
        }
        return purchaseResult;
    }

    /**
     * This should be processed serially in a message queue.
     * (Due to time constraints) Current implementation uses an async transactional call to persist in database.
     */
    @Async
    @Transactional(readOnly = false)
    private void persistPurchase(Integer newSKU, Integer flashSaleId, Integer buyerId, Integer productId) {
        Product p = productRepository.getOne(productId);
        p.setSku(newSKU);
        productRepository.saveAndFlush(p);

        Order order = new Order();
        order.setBuyer(buyerRepository.getOne(buyerId));
        order.setProduct(productRepository.getOne(productId));
        order.setCreatedAt(new Date());
        order.setOrderStatus(OrderStatus.APPROVED);
        orderRepository.saveAndFlush(order);

        /*Registration registration = registrationRepository.getOne(registrationPredicate.registrationByBuyerAndProductId(flashSaleId, buyerId));
        registration.setRegistrationStatus(RegistrationStatus.PURCHASED);
        registrationRepository.saveAndFlush(registration);*/
    }

}
