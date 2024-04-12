package com.example.ooms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ooms.model.Product;

public interface ProductRepository extends MongoRepository<Product,Integer>{

	List<Product> findByProductName(String productName);
}
