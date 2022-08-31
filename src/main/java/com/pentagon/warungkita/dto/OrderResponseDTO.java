package com.pentagon.warungkita.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pentagon.warungkita.model.OrderProduct;
import com.pentagon.warungkita.model.Users;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private List<OrderProductResponseDTO> orderProductId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private String ekspedisiName;

    private Number total;
    private UsersResponsePOST user;

    public void setOrderProductId (List<OrderProduct> orderProduct) {
        List<OrderProductResponseDTO> responseDTOs = new ArrayList<>();
        this.orderProductId = responseDTOs;
        orderProduct.forEach(orderProducts -> {
            OrderProductResponseDTO orderProductResponseDTO = new OrderProductResponseDTO();
            orderProductResponseDTO.setOrderProductId(orderProducts.getOrderProductId());
            orderProductResponseDTO.setProductId(orderProducts.getProductId().getProductId());
            orderProductResponseDTO.setQuantity(orderProducts.getQuantity());
            orderProductResponseDTO.setSubtotal(orderProducts.getSubtotal());
            orderProductResponseDTO.setUser(orderProducts.getUserId());
            responseDTOs.add(orderProductResponseDTO);
        });


    }

    public void setUser (Users users){
        UsersResponsePOST usersResponsePOST = new UsersResponsePOST();
        this.user = usersResponsePOST;
        this.user.setEmail(users.getEmail());
        this.user.setUsername(users.getUsername());
    }

}