/*package com.demo.flashSaleApi.predicates;

import com.demo.flashSaleApi.model.Buyer;
import com.demo.flashSaleApi.model.FlashSale;
import com.demo.flashSaleApi.pojo.RegistrationStatus;
import com.mysema.query.types.Predicate;





public class RegistrationPredicates {

	public static Predicate registrationWithFlashsale(FlashSale flashSale) {
        return QRegistration.registration.flashSale.id.eq(flashSale.getId())
                .and(QRegistration.registration.registrationStatus.eq(RegistrationStatus.REGISTERED));
    }

    public static Predicate registrationWithBuyerAndFlashsale(FlashSale flashSale, Buyer buyer) {
        return registrationByBuyerAndProductId(flashSale.getId(), buyer.getId());
    }

    public static Predicate registrationByBuyerAndProductId(Integer flashSaleId, Integer buyerId) {
        return QRegistration.registration.buyer.id.eq(buyerId)
                .and(QRegistration.registration.flashSale.id.eq(flashSaleId));
    }
}
*/