package com.example.ooms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ooms.model.User;
import com.example.ooms.repository.UserRepository;

//change user package

@Service
public class MyUserDetailsService implements UserDetailsService {

	  @Autowired
	  private UserRepository userRepository;

	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User u=userRepository.findByUsername(username);
	        if(u==null)
	            throw new UsernameNotFoundException("user not found!!");

	        return new MyUserDetails(u);
	  }

}
