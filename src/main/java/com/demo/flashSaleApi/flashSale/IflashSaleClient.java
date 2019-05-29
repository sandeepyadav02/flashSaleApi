package com.demo.flashSaleApi.flashSale;

import com.demo.flashSaleApi.pojo.PurchaseResult;

public interface IflashSaleClient {
	
	PurchaseResult purchase(final Integer flashsaleId, final Integer productId, final Integer buyerId);
}
