package com.demo.flashSaleApi.registration;

import java.util.List;

import com.demo.flashSaleApi.model.Buyer;
import com.demo.flashSaleApi.model.FlashSale;
import com.demo.flashSaleApi.model.Registration;
import com.demo.flashSaleApi.pojo.RegistrationResult;




public interface IregistrationClient {
	List<Registration> findRegistrationByFlashsale(final FlashSale flashSale);

   // Registration findRegistrationByFlashsaleBuyer(final FlashSale flashSale, final Buyer buyer);

    RegistrationResult newRegistrationResult(final Buyer b, final FlashSale f);
}
