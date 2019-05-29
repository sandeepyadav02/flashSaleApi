package com.demo.flashSaleApi.buyer;

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
import org.springframework.stereotype.Service;

import com.demo.flashSaleApi.LockService.ILockService;
import com.demo.flashSaleApi.util.Constants;






@Service(value = "buyerClient")
public class BuyerClient implements IbuyerClient {
	
	private final ExecutorService EXECUTOR_SERVICE;
	
	@Autowired
    private ILockService lockService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public BuyerClient() {
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
    }

    @Override
    public Boolean checkIfEligible(final Integer flashsaleId, final Integer productId, final Integer buyerId) {

        final String productKey = Constants.PRODUCT_CACHE_PREFIX + ":" + flashsaleId + "." + productId;
        final String buyerKey = Constants.BUYER_CACHE_PREFIX + ":" + flashsaleId + "." + buyerId;

        Future<Boolean> future = EXECUTOR_SERVICE.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return redisTemplate.execute(new SessionCallback<Boolean>() {
                    @Override
                    public Boolean execute(RedisOperations operations) throws DataAccessException {
                        String lock = null;
                        try {
                            lock = lockService.acquireLockWithTimeout(Constants.ELIGIBILITY_LOCKNAME,
                                    Constants.LOCK_ACQUIRE_TIMEOUT, Constants.ELIGIBILITY_LOCK_TIMEOUT);
                            if (lock == null) {
                                return Boolean.FALSE;
                            }
                            final Integer remainingSKU = (Integer) operations.opsForValue().get(productKey);
                            final Boolean buyerStatus = (Boolean) operations.opsForValue().get(buyerKey);
                            if (buyerStatus != null && buyerStatus && remainingSKU != null && remainingSKU > 0) {
                                return Boolean.TRUE;
                            }
                            return Boolean.FALSE;
                        } finally {
                            if (lock != null)
                                lockService.releaseLock(Constants.ELIGIBILITY_LOCKNAME, lock);
                        }
                    }
                });
            }
        });
        try {
            return future.get(Constants.BUYER_ELIGIBILITY_QUERY_TIMEOUT.longValue(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
         //   logger.error("Eligibility interrupted", e);
        } catch (ExecutionException e) {
          //  logger.error("Aborted", e);
        } catch (TimeoutException e) {
          //  logger.error("Eligibility timed out", e);
        }
        return Boolean.FALSE;
    }
}



