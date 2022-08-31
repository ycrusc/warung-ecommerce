package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.OrderProduct;
import com.pentagon.warungkita.model.Product;
import com.pentagon.warungkita.model.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductRequestDTO {
    private Long orderProductId;
    private Product product;
    private Integer quantity;
    private Integer subtotal;
    private Users userId;

    public OrderProduct convertToEntity(){
        return OrderProduct.builder()
                .orderProductId(this.orderProductId)
                .productId(this.product)
                .quantity(this.quantity)
                .subtotal(this.subtotal)
                .userId(this.userId)
                .build();
    }
}
