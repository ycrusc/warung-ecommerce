package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.controller.OrderProductController;
import com.pentagon.warungkita.dto.OrderProductRequestDTO;
import com.pentagon.warungkita.dto.OrderProductResponseDTO;
import com.pentagon.warungkita.dto.OrderProductResponsePOST;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.*;
import com.pentagon.warungkita.repository.OrderProductRepo;
import com.pentagon.warungkita.repository.ProductRepo;
import com.pentagon.warungkita.repository.UsersRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.security.service.UserDetailsImpl;
import com.pentagon.warungkita.service.OrderProductService;
import com.pentagon.warungkita.service.ProductStatusService;
import com.pentagon.warungkita.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderProductImpl implements OrderProductService {

    private OrderProductRepo orderProductRepo;
    private ProductRepo productRepo;
    UsersRepo usersRepo;
    private ProductStatusService productStatusService;
    UsersService usersService;
    private static final Logger logger = LogManager.getLogger(OrderProductController.class);

    @Override
    public ResponseEntity<Object> orderProductList() {
        try {
            List<OrderProduct> orderList = orderProductRepo.findAll();
            List<OrderProductResponseDTO> orderMaps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Order Product     ====================");
            for (OrderProduct dataOrder : orderList) {
                Map<String, Object> order = new HashMap<>();
                order.put("OrderProductID       : ", dataOrder.getOrderProductId());
                order.put("ProductID : ", dataOrder.getOrderProductId());
                logger.info("OrderProductID       : " + dataOrder.getOrderProductId());
                logger.info("ProductID : " + dataOrder.getProductId());
                OrderProductResponseDTO orderResponseDTO = dataOrder.convertToResponse();
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
    public ResponseEntity<Object> OrderListById(@PathVariable Long orderProductId) {
        try {
            Optional<OrderProduct> orderList = orderProductRepo.findById(orderProductId);
            OrderProduct orderProduct = orderList.get();
            OrderProductResponseDTO orderResponseDTO = orderProduct.convertToResponse();
            logger.info("==================== Logger Start Get Order Product By ID ====================");
            logger.info(orderResponseDTO);
            logger.info("==================== Logger End Get Order Product By ID =================");
            return ResponseHandler.generateResponse("Success Get By Id", HttpStatus.OK, orderResponseDTO);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "ID not found");
        }
    }

//    @Override
//    public OrderProduct saveOrderProduct(OrderProduct orderProduct) {
//        return this.orderProductRepo.save(orderProduct);
//    }

    @Override
    public ResponseEntity<Object> saveOrderProduct(@RequestBody OrderProductRequestDTO orderProductRequestDTO) {
        try {


            /**
             * Logic subtotal on Order Product
             * */
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional <Users> users = usersRepo.findById(userDetails.getUserId());
            Optional <Product> product1 = productRepo.findById(orderProductRequestDTO.getProduct().getProductId());
            if(product1.get().getUsers().getUserId().equals(userDetails.getUserId()) ){
                throw new ResourceNotFoundException("Can't add your own product");
            }
            if(orderProductRequestDTO.getQuantity()==0){
                throw new ResourceNotFoundException("Quantity can't 0");
            }


            Product product = productRepo.findById(orderProductRequestDTO.getProduct().getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(orderProductRequestDTO.getProduct())
                    .quantity(orderProductRequestDTO.getQuantity())
                    .userId(users.get())
                    .build();
            Integer totalPrice = product.getRegularPrice() * orderProductRequestDTO.getQuantity();
            orderProduct.setSubtotal(totalPrice);

            /**
             * Logic if Qty of Product less then Qty of Order Product
             * */
            if(product.getQuantity() < orderProduct.getQuantity()){
                throw new ResourceNotFoundException("stok kurang");
            }
            /**
             * Update Qty Product
             * */
            Integer newQty = product.getQuantity() - orderProductRequestDTO.getQuantity();
            product.setQuantity(newQty);
            /**
             * Update if qty product 0 set to Sold Out
             * */
            if(newQty==0){
                ProductStatus psSoldOut = productStatusService.getProductStatusBy(2L).get();
                product.setProductStatusId(psSoldOut);
            }

            this.orderProductRepo.save(orderProduct);

            OrderProductResponseDTO orderProductResponseDTO = orderProduct.convertToResponse();
            return ResponseHandler.generateResponse("Successfully  save Order", HttpStatus.CREATED, orderProductResponseDTO);


        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Bad Request!!");
        }
    }

    @Override
    public ResponseEntity<Object> updateOrderProduct(@PathVariable Long orderProductId, @RequestBody OrderProductRequestDTO orderProductRequestDTO) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional <Users> users = usersRepo.findById(userDetails.getUserId());
            Optional<OrderProduct> updatedOrderProduct = orderProductRepo.findById(orderProductId);
            Product product = productRepo.findById(orderProductRequestDTO.getProduct().getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
            if (updatedOrderProduct.isEmpty()) {
                throw new ResourceNotFoundException("Order not found with id " + orderProductId);
            }
            if(!updatedOrderProduct.get().getUserId().getUsername().equals(userDetails.getUsername())){
                throw new ResourceNotFoundException("You only can update your order");
            }
            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(orderProductRequestDTO.getProduct())
                    .quantity(orderProductRequestDTO.getQuantity())
                    .userId(users.get())
                    .build();
            Integer totalPrice = product.getRegularPrice() * orderProductRequestDTO.getQuantity();
            orderProduct.setSubtotal(totalPrice);

            orderProduct.setOrderProductId(orderProductId);
//            OrderProduct responseUpdate = orderProductRepo.updateOrderProduct(orderProduct);
            OrderProductResponseDTO responseDTO = orderProduct.convertToResponse();
            logger.info("==================== Logger Start Update Order Product By ID ====================");
            logger.info(responseDTO);
            logger.info("==================== Logger End Update Order Product By ID =================");
            this.orderProductRepo.save(orderProduct);
            return ResponseHandler.generateResponse("Data Updated!", HttpStatus.CREATED, responseDTO);
        } catch (ResourceNotFoundException e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Bad Request");
        }
    }

    @Override
    public ResponseEntity<Object> deleteOrderProduct(@PathVariable Long orderProductId){
        try {
            orderProductRepo.deleteById(orderProductId);
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
}
