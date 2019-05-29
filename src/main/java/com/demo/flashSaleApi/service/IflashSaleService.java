package com.demo.flashSaleApi.service;

import com.demo.flashSaleApi.pojo.PurchaseResult;
import com.demo.flashSaleApi.pojo.RegistrationResult;

public interface IflashSaleService {
 
	RegistrationResult register(final Integer flashsaleId, final Integer buyerId);

    Boolean isEligible(final Integer flashsaleId, final Integer buyerId);

    PurchaseResult purchase(final Integer flashsaleId, final Integer buyerId);
	
	
}
