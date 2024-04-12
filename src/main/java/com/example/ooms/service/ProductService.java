package com.example.ooms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ooms.exception.Exception_ProductDoesNotExists;
import com.example.ooms.model.Product;
import com.example.ooms.repository.ProductRepository;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
	
	
	@Autowired
	private ProductRepository productRepository;
	
	public Product addProduct(Product product) {
		productRepository.save(product);
		return product;
	}

    public List<Product> getAllProducts() throws Exception_ProductDoesNotExists {
        List<Product> product= productRepository.findAll();
        return product;
    }
    
    
    public List<Product> getProduct(String productName) throws Exception_ProductDoesNotExists{
    	List<Product> product = productRepository.findByProductName(productName);
    	if(product==null) {
    		throw new Exception_ProductDoesNotExists("No Product Found");
    	}
    	return product;
    }
    
    public Product getProductById(Integer id) throws Exception_ProductDoesNotExists {
        Optional<Product> product= productRepository.findById(id);
        log.info(product+"");
        return product.get();
    }
    
    public Product editProduct(Integer productId, Product updatedProduct) throws Exception_ProductDoesNotExists {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            if(updatedProduct.getProductName()!=null)
            existingProduct.setProductName(updatedProduct.getProductName());
            if(updatedProduct.getPrice() > 0)
            existingProduct.setPrice(updatedProduct.getPrice());
            if(updatedProduct.getDescription()!=null)
            existingProduct.setDescription(updatedProduct.getDescription());
            return productRepository.save(existingProduct);
        } else {
            throw new Exception_ProductDoesNotExists("Product with id " + productId + " not found");
        }
    }

}
