package com.pentagon.warungkita.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pentagon.warungkita.model.Users;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductResponseDTO {
    private Long orderProductId;
    private Long productId;
    private  Integer quantity;
    private Integer subtotal;
    private UsersResponsePOST user;

    @Override
    public String toString() {
        return "OrderProductResponseDTO{" +
                "orderProductId=" + orderProductId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", user=" + user +
                '}';
    }
    public void setUser (Users users){
        UsersResponsePOST usersResponsePOST = new UsersResponsePOST();
        this.user = usersResponsePOST;
        this.user.setEmail(users.getEmail());
        this.user.setUsername(users.getUsername());
    }
}
