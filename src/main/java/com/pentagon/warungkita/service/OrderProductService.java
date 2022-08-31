package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.OrderProductRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderProductService {
    ResponseEntity<Object> OrderListById(Long orderProductId);
    ResponseEntity<Object> saveOrderProduct (OrderProductRequestDTO orderProductRequestDTO);
    ResponseEntity<Object> orderProductList();
    ResponseEntity<Object> updateOrderProduct(Long orderProductId,OrderProductRequestDTO orderProductRequestDTO);
    ResponseEntity<Object> deleteOrderProduct(@PathVariable Long orderProductId);
}
