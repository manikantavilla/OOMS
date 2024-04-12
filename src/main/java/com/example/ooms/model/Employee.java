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
@Document(collection = "employees")
public class Employee {
	
	@Transient
    public static final String SEQUENCE_NAME="employees_sequence";
	
    @Id
    private int id;
    
    @NonNull
    private String employeeName;
   

    @Indexed(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;
    
    @NonNull
    private String confirmPassword;
    
    @NonNull
    private String address;
    
    @NonNull
    private String state;
    
    @NonNull
    private String city;

    @Indexed(unique = true)
    @NonNull
    private String email;
    
    @NonNull
    private String role;

}
