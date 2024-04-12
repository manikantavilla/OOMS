package com.example.ooms.controller;

import static com.example.ooms.model.Employee.SEQUENCE_NAME;

import java.util.InputMismatchException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooms.config.Authenticate;
import com.example.ooms.exception.Exception_EmployeeDoesNotExists;
import com.example.ooms.exception.Exception_ProductDoesNotExists;
import com.example.ooms.exception.Exception_UserAlreadyExists;
import com.example.ooms.exception.Exception_UserDoesNotExists;
import com.example.ooms.model.Employee;
import com.example.ooms.model.Product;
import com.example.ooms.model.User;
import com.example.ooms.service.EmployeeService;
import com.example.ooms.service.SequenceGeneratorService;
import com.example.ooms.service.UserService;
import com.example.ooms.util.JwtRequest;
import com.example.ooms.util.JwtResponse;
import com.example.ooms.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins= "*")
@RequestMapping(value="/api/v1.0/ooms/employee")
public class EmployeeController {
	
	@Autowired
    private EmployeeService employeeService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private JwtUtil jwtUtilToken;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private Authenticate authenticate;
    @Autowired
    private UserService userService;
    
    
    @PostMapping(value = "/addEmployee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) throws InputMismatchException, Exception_UserAlreadyExists {
        employee.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        Employee emp = employeeService.addEmployee(employee);
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> passwordCheck(@RequestBody JwtRequest Credentials) throws Exception {
    	String username = Credentials.getUsername();
    	String pasword = Credentials.getPassword();
    	Employee emp = employeeService.getEmployee(username);
    	Boolean pas = userService.checkPass(pasword,emp.getPassword());
    	if(pas)
    		Credentials.setPassword(emp.getPassword());
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
    
    @GetMapping(value = "/allEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestHeader("Authorization") String authorization) throws Exception_EmployeeDoesNotExists {
        List<Employee> emp= employeeService.getAllEmployees();
        return new ResponseEntity<>(emp, HttpStatus.OK);
    }
    
    @GetMapping(value ="/getById/{id}")
    public ResponseEntity<?> getProductbyId(@RequestHeader("Authorization") String authorization, @PathVariable("id") int id) throws Exception_EmployeeDoesNotExists {
        if(id>0) {
            Employee emp = employeeService.getEmployeeById(id);
            return ResponseEntity.status(HttpStatus.OK).body(emp);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID EMPLOYEE");
        }
    }
    
    
    @PutMapping("updateEmployee/{employeeId}")
    public ResponseEntity<Employee> editProduct(@RequestHeader("Authorization") String authorization,@PathVariable Integer employeeId, @RequestBody Employee updatedEmployee) throws Exception_EmployeeDoesNotExists {
        try {
            Employee editedEmployee = employeeService.editEmployee(employeeId, updatedEmployee);
            return ResponseEntity.ok(editedEmployee);
        } catch (Exception_EmployeeDoesNotExists e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping(value ="/getByUsername/{username}")
    public ResponseEntity<?> getUserbyusername(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) throws Exception_EmployeeDoesNotExists {
        if(username !=null) {
            Employee emp = employeeService.getEmployee(username);
            return ResponseEntity.status(HttpStatus.OK).body(emp);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found");
        }
    }

}
