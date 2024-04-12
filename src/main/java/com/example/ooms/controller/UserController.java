package com.example.ooms.controller;

import java.util.InputMismatchException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooms.config.Authenticate;
import com.example.ooms.exception.Exception_UserAlreadyExists;
import com.example.ooms.exception.Exception_UserDoesNotExists;
import com.example.ooms.model.AuthResponse;
import com.example.ooms.model.User;
import com.example.ooms.service.SequenceGeneratorService;
import com.example.ooms.service.UserService;
import com.example.ooms.util.JwtRequest;
import com.example.ooms.util.JwtResponse;
import com.example.ooms.util.JwtUtil;
import static com.example.ooms.model.User.SEQUENCE_NAME;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins= "*")
@RequestMapping(value="/api/v1.0/ooms")
public class UserController {

	private static final String usernotfound = "USER NOT FOUND";
    @Autowired
    private Authenticate authenticate;
    @Autowired
    private UserService userService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtilToken;
    @Autowired
    private UserDetailsService userDetailsService;




    @PostMapping(value = "/register")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception_UserAlreadyExists, InputMismatchException {
        user.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        User u=userService.addUser(user);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> passwordCheck(@RequestBody JwtRequest Credentials) throws Exception {
    	String username = Credentials.getUsername();
    	String pasword = Credentials.getPassword();
    	User user = userService.getUser(username);
    	Boolean pas = userService.checkPass(pasword,user.getPassword());
    	if(pas)
    		Credentials.setPassword(user.getPassword());
    	else
    		Credentials.setPassword(pasword);
    	return createAuthenticationToken(Credentials);
    }
    
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
    	log.info("Password " +authenticationRequest.getPassword());
        authenticate.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwtToken = jwtUtilToken.generateToken(userDetails);
        log.info("Received request to generate token for " + authenticationRequest);
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }
    
    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization") String token){
    	log.info("Inside Token Validation");
    	return new ResponseEntity<AuthResponse>(userService.getValidity(token),HttpStatus.OK);
    }
    
    @GetMapping(value = "/allusers")
    public ResponseEntity<List<User>> getAllUser(@RequestHeader("Authorization") String authorization) throws Exception_UserDoesNotExists {
        List<User> u= userService.getAllUsers();
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
   
    
    @GetMapping(value ="/getUname")
    public String getUname(@RequestHeader("Authorization") String authorization) {
        String token =authorization.substring(7);
        return jwtUtilToken.getUsernameFromToken(token);
    }

    
    @GetMapping(value ="/getById/{id}")
    public ResponseEntity<?> getUserbyId(@RequestHeader("Authorization") String authorization, @PathVariable("id") int id) throws Exception_UserDoesNotExists {
        if(id>0) {
            User u = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }
    
    @GetMapping(value ="/getByUsername/{username}")
    public ResponseEntity<?> getUserbyusername(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) throws Exception_UserDoesNotExists {
        if(username !=null) {
            User u = userService.getUser(username);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usernotfound);
        }
    }
}
