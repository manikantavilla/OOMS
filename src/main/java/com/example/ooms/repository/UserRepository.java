package com.example.ooms.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ooms.model.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,Integer> {

    User findByUsername(String username);


}