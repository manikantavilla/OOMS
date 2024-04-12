package com.example.ooms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ooms.model.Order;

public interface OrderRepository extends MongoRepository<Order,Integer>{

}
