package com.demo.flashSaleApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.demo.flashSaleApi.model.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Integer>{

}
