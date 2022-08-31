package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.dto.*;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.*;
import com.pentagon.warungkita.repository.*;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.security.service.UserDetailsImpl;
import com.pentagon.warungkita.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    OrderRepo orderRepo;
    UsersRepo usersRepo;
    OrderProductRepo orderProductRepo;
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public ResponseEntity<Object> getAll() {
        List<Order> orderList = this.orderRepo.findAll();
        if (orderList.isEmpty()) {
            log.error("No order found");
            throw new ResourceNotFoundException("No order found");
        }
        try {
            List<OrderResponseDTO> orderMaps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Order Product     ====================");

            for (Order dataOrder : orderList) {
                Map<String, Object> order = new HashMap<>();
                order.put("OrderID       : ", dataOrder.getOrderId());
                order.put("OrderDate : ", dataOrder.getOrderDate());
                order.put("EkspedisiID    : ", dataOrder.getEkspedisiId());
                order.put("Total    : ", dataOrder.getTotal());
                order.put("UserID : ", dataOrder.getUserId());
                logger.info("OrederID       : " + dataOrder.getOrderId());
                logger.info("OrderDate : " + dataOrder.getOrderDate());
                logger.info("EkspedisiID    : " + dataOrder.getEkspedisiId());
                logger.info("Total    : " + dataOrder.getTotal());
                logger.info("UserID : " + dataOrder.getUserId());
                OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(dataOrder.getOrderId())
                        .orderDate(dataOrder.getOrderDate())
                        .ekspedisiName(dataOrder.getEkspedisiId().getName())
                        .total(dataOrder.getTotal())
                        .build();
                orderResponseDTO.setOrderProductId(dataOrder.getOrderProduct());
                orderResponseDTO.setUser(dataOrder.getUserId());
                orderMaps.add(orderResponseDTO);
            }
            logger.info("==================== Logger Start Get All Order Product     ====================");
            return ResponseHandler.generateResponse("Successfully  getAll data!", HttpStatus.OK, orderMaps);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table not found");
        }
    }

    @Override
    public ResponseEntity<Object> getOrderById(Long id) throws ResourceNotFoundException {
        Optional<Order> orderList = this.orderRepo.findById(id);
        if (orderList.isEmpty()) {
            log.error("No order found");
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        try {
            Order order = orderList.get();
            OrderResponseDTO orderResponseDTO = order.convertToResponse();
            logger.info("==================== Logger Start Get Order Product By ID ====================");
            logger.info(orderResponseDTO);
            logger.info("==================== Logger End Get Order Product By ID =================");
            return ResponseHandler.generateResponse("Success Get By Id",HttpStatus.OK,orderResponseDTO);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "ID not found");
        }
    }

    @Override
    public ResponseEntity<Object> saveOrder(OrderRequestDTO orderRequestDTO) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional <Users> users = usersRepo.findById(userDetails.getUserId());

            orderRequestDTO.getOrderProduct().forEach(orderProductId -> {
                OrderProduct orderProduct1 = orderProductRepo.findById(orderProductId.getOrderProductId()).orElseThrow(() -> new ResourceNotFoundException("not found"));
                if(!orderProduct1.getUserId().getUserId().equals(userDetails.getUserId())){
                    throw new ResourceNotFoundException("You only can order your order product");
                }
            });
            List<Integer> subtotal = new ArrayList<>();
            orderRequestDTO.getOrderProduct().forEach(orderProductId -> {
                OrderProduct orderProduct = orderProductRepo.findById(orderProductId.getOrderProductId()).orElseThrow(() -> new ResourceNotFoundException("not found"));
                subtotal.add(orderProduct.getSubtotal());
            });

            Integer total = subtotal.stream().mapToInt(map -> map.intValue()).sum();
            Order order = Order.builder()
                    .orderProduct(orderRequestDTO.getOrderProduct())
                    .orderDate(orderRequestDTO.getOrderDate())
                    .ekspedisiId(orderRequestDTO.getEkspedisiId())
                    .total(total.intValue())
                    .userId(users.get())
                    .build();

            orderRepo.save(order);
            OrderResponsePOST orderResponsePOST = order.convertToResponsePOST();
            logger.info("==================== Logger Start Post Order Product ====================");
            logger.info(orderResponsePOST);
            logger.info("==================== Logger Start Post Order Product =================");
            return ResponseHandler.generateResponse("Successfully  save Order", HttpStatus.CREATED, orderResponsePOST);
        } catch (ResponseStatusException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Bad Request!!");
        }
    }

    @Override
    public ResponseEntity<Object> updateOrder(OrderRequestDTO orderRequestDTO, Long orderId) {
        try {
            Optional<Order> updatedOrder = orderRepo.findById(orderId);
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional <Users> users = usersRepo.findById(userDetails.getUserId());
            orderRequestDTO.getOrderProduct().forEach(orderProductId -> {
                OrderProduct orderProduct1 = orderProductRepo.findById(orderProductId.getOrderProductId()).orElseThrow(() -> new ResourceNotFoundException("not found"));
                if(!orderProduct1.getUserId().getUserId().equals(userDetails.getUserId())){
                    throw new ResourceNotFoundException("You only can order your order product");
                }
            });
            if (updatedOrder.isEmpty()) {
                throw new ResourceNotFoundException("Order not found with id " + orderId);
            }
            if(!updatedOrder.get().getUserId().getUsername().equals(userDetails.getUsername())){
                throw new ResourceNotFoundException("You only can update your order");
            }
            List<Integer> subtotal = new ArrayList<>();
            orderRequestDTO.getOrderProduct().forEach(orderProductId -> {
                OrderProduct orderProduct = orderProductRepo.findById(orderProductId.getOrderProductId()).orElseThrow(() -> new ResourceNotFoundException("not found"));
                subtotal.add(orderProduct.getSubtotal());
            });

            Integer total = subtotal.stream().mapToInt(map -> map.intValue()).sum();
            Order order = Order.builder()

                    .orderProduct(orderRequestDTO.getOrderProduct())
                    .orderDate(orderRequestDTO.getOrderDate())
                    .ekspedisiId(orderRequestDTO.getEkspedisiId())
                    .total(total.intValue())
                    .userId(users.get())
                    .build();
            order.setOrderDate(LocalDate.now());
            order.setOrderId(orderId);
            Order responseUpdate = orderRepo.save(order);
            OrderResponsePOST responseDTO = responseUpdate.convertToResponsePOST();
            logger.info("==================== Logger Start Update Order Product By ID ====================");
            logger.info(responseDTO);
            logger.info("==================== Logger End Update Order Product By ID =================");
            return ResponseHandler.generateResponse("Data Updated!", HttpStatus.CREATED, responseDTO);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Bad Request");
        }
    }

    @Override
    public ResponseEntity<Object> deleteOrderById(Long id) {
        Optional<Order> updatedOrder = this.orderRepo.findById(id);
        if (updatedOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        Order order = orderRepo.getReferenceById(id);
        try {
            orderRepo.delete(order);
            Boolean result = Boolean.TRUE;
            logger.info("==================== Logger Start Delete Order Product By ID ====================");
            logger.info(result);
            logger.info("==================== Logger End Delete Order Product By ID =================");
            return ResponseHandler.generateResponse("Success Delete Booking by ID", HttpStatus.OK, result);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    @Override
    public ResponseEntity<Object> getBuyerOrder() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Order> orderList = this.orderRepo.findByUserIdUsername(userDetails.getUsername());
            if (orderList.isEmpty()) {
                log.error("No order found");
                throw new ResourceNotFoundException("You not have Order ");
            }
            List<Order> test = this.orderRepo.findByUserIdUsername(userDetails.getUsername());
            List<OrderResponseDTO> productListmaps = new ArrayList<>();
            for (Order dataresult : test) {

                OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(dataresult.getOrderId())
                        .orderDate(dataresult.getOrderDate())
                        .ekspedisiName(dataresult.getEkspedisiId().getName())
                        .total(dataresult.getTotal())
                        .build();
                orderResponseDTO.setOrderProductId(dataresult.getOrderProduct());
                orderResponseDTO.setUser(dataresult.getUserId());
                productListmaps.add(orderResponseDTO);
            }


            logger.info("==================== Logger Start Get Order Product By ID ====================");
            logger.info(orderList);
            logger.info("==================== Logger End Get Order Product By ID =================");
            return ResponseHandler.generateResponse("Success Get Your Order",HttpStatus.OK,productListmaps);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "ID not found");
        }
    }
}
