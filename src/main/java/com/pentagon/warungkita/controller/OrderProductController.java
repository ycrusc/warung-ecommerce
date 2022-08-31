package com.pentagon.warungkita.controller;

import com.pentagon.warungkita.dto.OrderProductRequestDTO;
import com.pentagon.warungkita.service.OrderProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pentagon/warung-kita")
@RestController
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-key")
@Tag(name = "07.Order Product")
public class OrderProductController {

    private OrderProductService orderProductService;

    /*
     * Get all Data Order products table
     * Menggunakan ResponseDTO
     * */

    @Operation(summary = "View all Order Product (ADMIN)")
    @GetMapping("/list/order-products")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> orderProductList() {
        return orderProductService.orderProductList();
    }

    @Operation(summary = "View Order Product by Id (ADMIN)")
    @GetMapping("/list/order-products/{orderProductId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> OrderListById(@PathVariable Long orderProductId) {
       return orderProductService.OrderListById(orderProductId);
    }

    @Operation(summary = "Create Order Product (BUYER)")
    @PostMapping("/list/order-products")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> saveOrderProduct(@RequestBody OrderProductRequestDTO orderProductRequestDTO) {
        return orderProductService.saveOrderProduct(orderProductRequestDTO);
    }

    @Operation(summary = "Update Order Product by Id (BUYER)")
    @PutMapping("/update/order-products/{orderProductId}")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> updateOrderProduct(@PathVariable Long orderProductId, @RequestBody OrderProductRequestDTO orderProductRequestDTO) {
        return orderProductService.updateOrderProduct(orderProductId, orderProductRequestDTO);
    }

    @Operation(summary = "Delete Order Product by Id (ADMIN)")
    @DeleteMapping("/delete/order-products/{orderProductId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteOrderProduct(@PathVariable Long orderProductId){
        return orderProductService.deleteOrderProduct(orderProductId);
    }
}