package com.demo.flashSaleApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.flashSaleApi.model.Product;

public interface InventoryRepository extends JpaRepository<Product, Integer> {

}