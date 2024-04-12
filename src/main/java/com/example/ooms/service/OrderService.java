package com.example.ooms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ooms.exception.Exception_OrderDoesNotExists;
import com.example.ooms.model.Order;
import com.example.ooms.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public Order addOrder(Order order) {
		orderRepository.save(order);
		return order;
	}

    public List<Order> getAllOrders() throws Exception_OrderDoesNotExists {
        List<Order> order= orderRepository.findAll();
        return order;
    }
    
    public Order getOrderById(Integer id) throws Exception_OrderDoesNotExists {
        Optional<Order> order= orderRepository.findById(id);
        log.info(order+"");
        return order.get();
    }

}
