package com.pentagon.warungkita.controller;

import com.pentagon.warungkita.dto.OrderRequestDTO;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pentagon/warung-kita")
@RestController
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-key")
@Tag(name = "08. Order")
public class OrderController {

    OrderService orderService;

    /*Get All Data dari Order Table
     * Untuk Penampilan Data Bisa Menggunakan ResponseDTO
     * */
    @Operation(summary = "View all Order (ADMIN)")
    @GetMapping("/list/order")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> OrderList(){
        return orderService.getAll();
    }

    @Operation(summary = "View Own Order (BUYER)")
    @GetMapping("/list/order/own_order")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> Orderbuyer(){
        return orderService.getBuyerOrder();
    }

    @Operation(summary = "View Order by Id (ADMIN)")
    @GetMapping("/list/order/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> OrderListById(@PathVariable Long orderId){
        return orderService.getOrderById(orderId);
    }

    /*
     *save(create) order baru untuk order table
     *throws ResourceNotFoundException jika data tidak ditemukan
     * membuat RequestDTO
     * */
    @Operation(summary = "Create Order (BUYER)")
    @PostMapping("/save/order")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @Transactional
    public ResponseEntity<Object> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws ResourceNotFoundException {
        return orderService.saveOrder(orderRequestDTO);
    }

    /*
     *update order baru untuk order table
     *throws ResourceNotFoundException jika data tidak ditemukan
     * membuat RequestDTO
     * */
    @Operation(summary = "Update Order (BUYER)")
    @PutMapping("/update/order/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> updateOrder( @PathVariable Long orderId, @RequestBody OrderRequestDTO orderRequestDTO){
        return orderService.updateOrder(orderRequestDTO, orderId);
    }
    @Operation(summary = "Delete Order by Id (ADMIN, BUYER)")
    @DeleteMapping("/delete/order/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long orderId){
        return orderService.deleteOrderById(orderId);
    }
}