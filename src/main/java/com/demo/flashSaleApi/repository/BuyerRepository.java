package com.demo.flashSaleApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.demo.flashSaleApi.model.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Integer>{

}
