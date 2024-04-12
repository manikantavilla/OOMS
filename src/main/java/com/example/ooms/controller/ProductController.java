package com.example.ooms.controller;

import static com.example.ooms.model.Product.SEQUENCE_NAME;

import java.util.InputMismatchException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.example.ooms.exception.Exception_ProductDoesNotExists;
import com.example.ooms.exception.Exception_UserAlreadyExists;
import com.example.ooms.exception.Exception_UserDoesNotExists;
import com.example.ooms.model.Product;
import com.example.ooms.model.User;
import com.example.ooms.service.ProductService;
import com.example.ooms.service.SequenceGeneratorService;
import com.example.ooms.service.UserService;
import com.example.ooms.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins= "*")
@RequestMapping(value="/api/v1.0/ooms/products")
public class ProductController {
	
    @Autowired
    private ProductService productService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    
    
    @PostMapping(value = "/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        product.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        Product pro = productService.addProduct(product);
        return new ResponseEntity<>(pro, HttpStatus.CREATED);
    }
    
    @GetMapping(value = "/allProducts")
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("Authorization") String authorization) throws Exception_ProductDoesNotExists {
        List<Product> pro= productService.getAllProducts();
        return new ResponseEntity<>(pro, HttpStatus.OK);
    }
    
    @GetMapping(value ="/getById/{id}")
    public ResponseEntity<?> getProductbyId(@RequestHeader("Authorization") String authorization, @PathVariable("id") int id) throws Exception_ProductDoesNotExists {
        if(id>0) {
            Product pro = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(pro);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID PRODUCT");
        }
    }
    
    @GetMapping(value ="/getByProductName/{productName}")
    public ResponseEntity<List<Product>> getByProductName(@RequestHeader("Authorization") String authorization, @PathVariable("productName") String productName) throws Exception_ProductDoesNotExists {
    	 List<Product> pro = productService.getProduct(productName);
    	if (pro.isEmpty()) {
            throw new Exception_ProductDoesNotExists("No product found with the name: " + productName);
        }
        return ResponseEntity.status(HttpStatus.OK).body(pro);
    }
    
    @PutMapping("updateProduct/{productId}")
    public ResponseEntity<Product> editProduct(@RequestHeader("Authorization") String authorization,@PathVariable Integer productId, @RequestBody Product updatedProduct) {
        try {
            Product editedProduct = productService.editProduct(productId, updatedProduct);
            return ResponseEntity.ok(editedProduct);
        } catch (Exception_ProductDoesNotExists e) {
            return ResponseEntity.notFound().build();
        }
    }

}
