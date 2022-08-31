package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.ProductRequestDTO;
import com.pentagon.warungkita.model.Product;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ResponseEntity<Object> getAllProduct();
    ResponseEntity<Object> getProductById(Long productId);
    ResponseEntity<Object> createProduct(ProductRequestDTO productRequestDTO);
    ResponseEntity<Object> updateProduct(Long productId, ProductRequestDTO productRequestDTO);
    ResponseEntity<Object> deleteProduct(Long productId);
    ResponseEntity<Object> findByProductNameContaining(String productName);
    ResponseEntity<Object> findByUsersUsernameContaining(String username);
    ResponseEntity<Object> findByCategories(String name);
    ResponseEntity<Object> findBySellerProduct();
}
