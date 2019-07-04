package com.demo.flashSaleApi.flashSale;

import com.demo.flashSaleApi.pojo.PurchaseResult;

public interface IflashSaleClient {
	
	/*Cache hitting API that makes the purchase transaction.
    * Persists in database after the purchase went through.*/
		
	PurchaseResult purchase(final Integer flashsaleId, final Integer productId, final Integer buyerId);
}
