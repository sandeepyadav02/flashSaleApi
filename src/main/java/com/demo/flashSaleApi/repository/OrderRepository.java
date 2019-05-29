package com.demo.flashSaleApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.demo.flashSaleApi.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
