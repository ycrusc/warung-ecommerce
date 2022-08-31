package com.pentagon.warungkita.service;

import com.pentagon.warungkita.model.ProductStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ProductStatusService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> createProductStatus( ProductStatus productStatusDetails);
    ResponseEntity<Object> getProductStatusById(Long Id);
    ResponseEntity<Object> deleteProductStatusById(Long Id);
    ResponseEntity<Object> updateProductStatus(Long Id, ProductStatus productStatus);
    Optional<ProductStatus> getProductStatusBy(Long Id);
}
