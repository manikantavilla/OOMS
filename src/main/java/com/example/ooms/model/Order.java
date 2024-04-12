package com.example.ooms.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Document(collection = "orders")
public class Order {
	
	@Transient
    public static final String SEQUENCE_NAME="orders_sequence";
	
	@Id
	private int id;
	private String username;
	private int quantity;
	private LocalDate orderDate;

}
