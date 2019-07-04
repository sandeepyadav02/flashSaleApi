package com.demo.flashSaleApi.service;

import com.demo.flashSaleApi.pojo.PurchaseResult;
import com.demo.flashSaleApi.pojo.RegistrationResult;

public interface IflashSaleService {
	
	//invoked by an individual user
 
	RegistrationResult register(final Integer flashsaleId, final Integer buyerId);
	
    //api for checking eligibility
    Boolean isEligible(final Integer flashsaleId, final Integer buyerId);
    
    
    //api for persisting the purchase into the database
    PurchaseResult purchase(final Integer flashsaleId, final Integer buyerId);
	
	
}
