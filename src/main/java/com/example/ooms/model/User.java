package com.example.ooms.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Document(collection = "users")
public class User {
	
	@Transient
    public static final String SEQUENCE_NAME="users_sequence";

    @Id
    private int id;
    
    @NonNull
    private String firstName;
    
	@NonNull
    private String lastName;

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
