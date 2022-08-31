package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.OrderRequestDTO;
import com.pentagon.warungkita.model.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getOrderById(Long id);
    ResponseEntity<Object> saveOrder(OrderRequestDTO orderRequestDTO);
    ResponseEntity<Object> updateOrder(OrderRequestDTO orderRequestDTO, Long orderId);
    ResponseEntity<Object> deleteOrderById(Long id);
    ResponseEntity<Object> getBuyerOrder();
}
