package com.demo.flashSaleApi.adminService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo.flashSaleApi.LockService.ICacheService;
import com.demo.flashSaleApi.model.Buyer;
import com.demo.flashSaleApi.model.FlashSale;
import com.demo.flashSaleApi.model.Product;
import com.demo.flashSaleApi.model.Registration;
import com.demo.flashSaleApi.pojo.RegistrationStatus;
import com.demo.flashSaleApi.registration.IregistrationClient;
import com.demo.flashSaleApi.repository.FlashSaleRepository;
import com.demo.flashSaleApi.util.Constants;



public class AdminService implements IadminService {
	@Autowired
    private FlashSaleRepository flashSaleRepository;

    @Autowired
    private IregistrationClient registrationClient;

    @Autowired
    private ICacheService<String, Object> cacheService;

    @Override
    public FlashSale createFlashSale(final Product p) {
        FlashSale f = new FlashSale();
        f.setProduct(p);
        f.setStatus(Boolean.FALSE);
        f.setRegistrationOpen(Boolean.TRUE);
        return flashSaleRepository.save(f);
    }

    @Override
    public Boolean startFlashSale(final FlashSale f) {
        preprocess(f);
        f.setStatus(Boolean.TRUE);
        f.setRegistrationOpen(Boolean.FALSE);
        flashSaleRepository.saveAndFlush(f);
        return true;
    }

    @Override
    public Boolean stopFlashSale(final FlashSale f) {
        cacheService.deleteInMemory(f.getId().toString(), Constants.FLASHSALE_CACHE_PREFIX);
        f.setStatus(Boolean.FALSE);
        flashSaleRepository.saveAndFlush(f);
        return true;
    }

    private void preprocess(FlashSale f) {
        List<Registration> registrationsForThisFlashsale = registrationClient.findRegistrationByFlashsale(f);
        Product p = f.getProduct();

        // cache product id in memory, not going to change during sale
        cacheService.setInMemory(f.getId().toString(), Constants.FLASHSALE_CACHE_PREFIX, p.getId());

        // cache sku and buyer information in redis
        cacheService.set(f.getId() + "." + p.getId(), Constants.PRODUCT_CACHE_PREFIX, p.getSku(),
                Constants.FLASHSALE_DURATION);
        for (Registration registration : registrationsForThisFlashsale) {
            if (registration.getRegistrationStatus() == RegistrationStatus.REGISTERED) {
                Buyer buyer = registration.getBuyer();
                cacheService.set(f.getId() + "." + buyer.getId(), Constants.BUYER_CACHE_PREFIX, Boolean.TRUE,
                        Constants.FLASHSALE_DURATION);
            }
        }
        
    }
}

