package com.example.ooms.controller;

import static com.example.ooms.model.Order.SEQUENCE_NAME;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooms.exception.Exception_OrderDoesNotExists;
import com.example.ooms.exception.Exception_ProductDoesNotExists;
import com.example.ooms.model.Order;
import com.example.ooms.model.Product;
import com.example.ooms.service.OrderService;
import com.example.ooms.service.ProductService;
import com.example.ooms.service.SequenceGeneratorService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins= "*")
@RequestMapping(value="/api/v1.0/ooms/orders")
public class OrderController {
	
	
    @Autowired
    private OrderService orderService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private UserDetailsService userDetailsService;
    
    
    @PostMapping(value = "/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        order.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        order.setOrderDate(LocalDate.now());
//        String jwtToken = authorization.substring(7);
//        String currentUserName = extractUsernameFromToken(jwtToken);
//        order.setUsername(currentUserName);
        Order ord = orderService.addOrder(order);
        return new ResponseEntity<>(ord, HttpStatus.CREATED);
    }
    
    @GetMapping(value = "/allOrders")
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String authorization) throws Exception_OrderDoesNotExists {
        List<Order> ord= orderService.getAllOrders();
        return new ResponseEntity<>(ord, HttpStatus.OK);
    }
    
    @GetMapping(value ="/getById/{id}")
    public ResponseEntity<?> getOrderbyId(@RequestHeader("Authorization") String authorization, @PathVariable("id") int id) throws Exception_OrderDoesNotExists {
        if(id>0) {
            Order ord = orderService.getOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ord);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID ORDER");
        }
    }
    
    private String extractUsernameFromToken(String token) {
        return userDetailsService.loadUserByUsername(token).getUsername();
    }

}
