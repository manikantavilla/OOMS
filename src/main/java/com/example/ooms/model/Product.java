package com.example.ooms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Document(collection = "products")

public class Product {

	@Transient
    public static final String SEQUENCE_NAME="products_sequence";

    @Id
    private int id;
    
    @NonNull
    private String productName;
    
	@NonNull
    private String description;
	
	private int price;
	
	private String imgURL;


}
