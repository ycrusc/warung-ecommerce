package com.pentagon.warungkita.model;

import com.pentagon.warungkita.dto.OrderProductResponseDTO;
import com.pentagon.warungkita.dto.OrderProductResponsePOST;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "order_products")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
    private Integer quantity;
    private Integer subtotal;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;


    public OrderProductResponseDTO convertToResponse(){
        return OrderProductResponseDTO.builder()
                .orderProductId(this.orderProductId)
                .productId(this.productId.getProductId())
                .quantity(this.quantity)
                .subtotal(this.subtotal)
                .user(this.userId.convertToResponsePOST())
                .build();
    }

    public OrderProductResponsePOST convertToResponsePOST(){
        return OrderProductResponsePOST.builder()
                .productId(this.productId.getProductId())
                .quantity(this.quantity)
                .subtotal(this.subtotal)
                .build();
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "orderProductId=" + orderProductId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}
