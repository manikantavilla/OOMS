package com.example.ooms.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ooms.exception.Exception_EmployeeDoesNotExists;
import com.example.ooms.exception.Exception_ProductDoesNotExists;
import com.example.ooms.exception.Exception_UserAlreadyExists;
import com.example.ooms.exception.Exception_UserDoesNotExists;
import com.example.ooms.model.Employee;
import com.example.ooms.model.Product;
import com.example.ooms.model.User;
import com.example.ooms.repository.EmployeeRepository;
import com.example.ooms.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private UserService userService;
	
    public Employee addEmployee(Employee employee) throws Exception_UserAlreadyExists,InputMismatchException {
        Employee existingEmployee = employeeRepository.findByUsername(employee.getUsername());
        boolean valid = false;
        if(existingEmployee==null) {
            String mail=employee.getEmail();
            String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(mail);
            if(matcher.matches()==true && employee.getPassword().equals(employee.getConfirmPassword())) {
                valid = true;
            }
            if(valid) {
            	log.info("user Added");
            	
            		String password = employee.getPassword();
            		employee.setPassword(userService.hashPassword(password));
            		employee.setConfirmPassword(employee.getPassword());
            		employee.setRole("EMPLOYEE");
            		return employeeRepository.save(employee);
            }
            
            else{
                throw  new InputMismatchException("Password Mismatch");
            }
        }
        throw new Exception_UserAlreadyExists("Employee already exists");
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employee= employeeRepository.findAll();
        return employee;
    }
    
    public Employee getEmployee(String username) throws Exception_EmployeeDoesNotExists{
    	Employee emp = employeeRepository.findByUsername(username);
    	if(emp==null) {
    		throw new Exception_EmployeeDoesNotExists("No user Found");
    	}
    	return emp;
    }
    
    
//    public List<Employee> getEmployee(String employeeName) throws Exception_EmployeeDoesNotExists{
//    	List<Employee> employee = employeeRepository.findByEmployeeName(employeeName);
//    	if(employee==null) {
//    		throw new Exception_EmployeeDoesNotExists("No Employee Found");
//    	}
//    	return employee;
//    }
    
    public Employee getEmployeeById(Integer id) throws Exception_EmployeeDoesNotExists {
        Optional<Employee> employee= employeeRepository.findById(id);
        log.info(employee+"");
        return employee.get();
    }
    
    public Employee editEmployee(Integer employeeId, Employee updatedEmployee) throws Exception_EmployeeDoesNotExists {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            if(updatedEmployee.getEmployeeName()!=null)
            existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
            if(updatedEmployee.getAddress()!=null)
            	existingEmployee.setAddress(updatedEmployee.getAddress());
            if(updatedEmployee.getState()!=null)
            	existingEmployee.setState(updatedEmployee.getState());
            if(updatedEmployee.getCity()!=null)
            	existingEmployee.setCity(updatedEmployee.getCity());
            if(updatedEmployee.getEmail()!=null)
            	existingEmployee.setEmail(updatedEmployee.getEmail());
            return employeeRepository.save(existingEmployee);
        } else {
            throw new Exception_EmployeeDoesNotExists("Product with id " + employeeId + " not found");
        }
    }

}
