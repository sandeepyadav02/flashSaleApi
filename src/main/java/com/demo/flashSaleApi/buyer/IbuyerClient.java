package com.demo.flashSaleApi.buyer;

public interface IbuyerClient {
	
    Boolean checkIfEligible(final Integer flashsaleId, final Integer productId, final Integer buyerId);

}
