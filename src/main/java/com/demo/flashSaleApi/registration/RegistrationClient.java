package com.demo.flashSaleApi.registration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.flashSaleApi.model.Buyer;
import com.demo.flashSaleApi.model.FlashSale;
import com.demo.flashSaleApi.model.Registration;
import com.demo.flashSaleApi.pojo.RegistrationResult;
import com.demo.flashSaleApi.pojo.RegistrationStatus;
//import com.demo.flashSaleApi.predicates.RegistrationPredicates;
import com.demo.flashSaleApi.repository.RegistrationRepository;
import com.google.common.collect.Lists;

@Service(value = "registrationClient")
public class RegistrationClient implements IregistrationClient {
	
	@Autowired
    private RegistrationRepository registrationRepository;
	
	

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findRegistrationByFlashsale(final FlashSale flashSale) {
        return Lists.newArrayList(registrationRepository.findAll());
    }


    @Override
    @Transactional(readOnly = false)
    public RegistrationResult newRegistrationResult(final Buyer b, final FlashSale f) {
        Registration registration = new Registration();
        registration.setBuyer(b);
        registration.setFlashSale(f);
        registration.setRegistrationStatus(RegistrationStatus.REGISTERED);
        registration = registrationRepository.saveAndFlush(registration);
        RegistrationResult registrationResult = new RegistrationResult();
        registrationResult.setMessage("SUCCESS!");
        registrationResult.setRegistrationId(registration.getId());
        registrationResult.setStatus(Boolean.TRUE);
        return registrationResult;
    }

}
