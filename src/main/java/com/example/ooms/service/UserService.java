package com.example.ooms.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ooms.exception.Exception_UserAlreadyExists;
import com.example.ooms.exception.Exception_UserDoesNotExists;
import com.example.ooms.model.AuthResponse;
import com.example.ooms.model.User;
import com.example.ooms.repository.UserRepository;
import com.example.ooms.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private SequenceGeneratorService seqService;
	
    @Autowired
    private UserRepository userRepository;
    
        
    public User addUser(User user) throws Exception_UserAlreadyExists,InputMismatchException {
        User existingUser = userRepository.findByUsername(user.getUsername());
        boolean valid = false;
        if(existingUser==null) {
            String mail=user.getEmail();
            String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(mail);
            if(matcher.matches()==true && user.getPassword().equals(user.getConfirmPassword())) {
                valid = true;
            }
            if(valid) {
            	log.info("user Added");
            	
            		String password = user.getPassword();
            		user.setPassword(hashPassword(password));
            		user.setConfirmPassword(user.getPassword());
            		user.setRole("USER");
            		return userRepository.save(user);
            }
            
            else{
                throw  new InputMismatchException("Password Mismatch");
            }
        }
        throw new Exception_UserAlreadyExists("User already exists");
    }

    public List<User> getAllUsers() throws Exception_UserDoesNotExists {
        List<User> user= userRepository.findAll();
        return user;
    }
    
    
    public User getUser(String username) throws Exception_UserDoesNotExists{
    	User user = userRepository.findByUsername(username);
    	if(user==null) {
    		throw new Exception_UserDoesNotExists("No user Found");
    	}
    	return user;
    }

    
    public User getUserById(Integer id) throws Exception_UserDoesNotExists {
        Optional<User> user= userRepository.findById(id);
        log.info(user+"");
        return user.get();
    }
    
    
    public AuthResponse getValidity(String token) {
    	//retrive the token (removing Bearer from token)
    	String token1 = token.substring(7);
    	AuthResponse authResponse = new AuthResponse();
    	//if valid
    	if(jwtUtil.validateToken(token1)) {
    		log.info("Token is valid");
    		String username = jwtUtil.getUsernameFromToken(token1);
    		
    		//set values for the response
    		authResponse.setUsername(username);
    		authResponse.setValid(true);
    		authResponse.setId(userRepository.findByUsername(username).getId());
    	}else {
    		authResponse.setValid(false);
    		log.info("Token is invalid or Expired");
    	}
    	return authResponse;
    }
    
    public String hashPassword(String plainTextPassword){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
	}
    
    public Boolean checkPass(String plainPassword, String hashedPassword) {
		if (BCrypt.checkpw(plainPassword, hashedPassword)) {
			return true;
		}
		else {
			return false;
		}
	}

}
