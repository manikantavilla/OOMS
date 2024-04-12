package com.example.ooms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ooms.model.Employee;
import com.example.ooms.model.Product;
import com.example.ooms.model.User;


public interface EmployeeRepository extends MongoRepository<Employee,Integer>{
	
	List<Employee> findByEmployeeName(String employeeName);
	Employee findByUsername(String username);

}
