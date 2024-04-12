package com.example.ooms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Configuration
public class Authenticate {

    @Autowired
    private AuthenticationManager authenticationManager;

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED",e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS",e);
        }
    }
}
