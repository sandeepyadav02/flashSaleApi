package com.demo.flashSaleApi.buyer;

public interface IbuyerClient {
	
	//checking the eligibility of the buyer
	
    Boolean checkIfEligible(final Integer flashsaleId, final Integer productId, final Integer buyerId);

}
