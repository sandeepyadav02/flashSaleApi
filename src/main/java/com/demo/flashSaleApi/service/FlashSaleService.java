package com.demo.flashSaleApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.flashSaleApi.LockService.ICacheService;
import com.demo.flashSaleApi.buyer.IbuyerClient;
import com.demo.flashSaleApi.flashSale.IflashSaleClient;
import com.demo.flashSaleApi.model.Buyer;
import com.demo.flashSaleApi.model.FlashSale;
import com.demo.flashSaleApi.pojo.PurchaseResult;
import com.demo.flashSaleApi.pojo.RegistrationResult;
import com.demo.flashSaleApi.registration.IregistrationClient;
import com.demo.flashSaleApi.repository.BuyerRepository;
import com.demo.flashSaleApi.repository.FlashSaleRepository;
import com.demo.flashSaleApi.util.Constants;

@Service
public class FlashSaleService implements IflashSaleService {



    @Autowired
    private FlashSaleRepository flashSaleRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
   private IregistrationClient registrationClient;

    @Autowired
    private ICacheService<String, Integer> cacheService;

    @Autowired
    private IbuyerClient buyerClient;

    @Autowired
    private IflashSaleClient flashSaleClient;

    @Override
    public RegistrationResult register(final Integer flashsaleId, final Integer buyerId) {
        FlashSale f = flashSaleRepository.getOne(flashsaleId);
        Buyer b = buyerRepository.getOne(buyerId);

        RegistrationResult registrationResult = new RegistrationResult();
        registrationResult.setStatus(Boolean.FALSE);

        if (f == null || !f.getRegistrationOpen() || f.getStatus() == Boolean.TRUE) {
            registrationResult.setMessage("Invalid flashsale");
        } else if (b == null) {
            registrationResult.setMessage("Invalid buyer");
        }
        else {
        	registrationResult = registrationClient.newRegistrationResult(b, f);
        }
        return registrationResult;
    }

    @Override
    public Boolean isEligible(final Integer flashsaleId, final Integer buyerId) {
        Integer productId = cacheService.getInMemory(flashsaleId.toString(), Constants.FLASHSALE_CACHE_PREFIX);
        return buyerClient.checkIfEligible(flashsaleId, productId, buyerId);
    }

    @Override
    public PurchaseResult purchase(final Integer flashsaleId, final Integer buyerId) {
        Integer productId = cacheService.getInMemory(flashsaleId.toString(), Constants.FLASHSALE_CACHE_PREFIX);
        return flashSaleClient.purchase(flashsaleId, productId, buyerId);
    }
}
