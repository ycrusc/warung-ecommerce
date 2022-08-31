package com.pentagon.warungkita.model;

import com.pentagon.warungkita.dto.OrderResponseDTO;
import com.pentagon.warungkita.dto.OrderResponsePOST;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    private LocalDate orderDate;
    @ManyToOne
    @JoinColumn(name = "ekspedisi_id")
    private Ekspedisi ekspedisiId;

    @OneToMany
    @JoinTable(name = "order_order_products",
            joinColumns = @JoinColumn (name = "order_id"),
            inverseJoinColumns = @JoinColumn (name = "order_product_id" ))
    private List<OrderProduct> orderProduct;

    private Integer total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;

    public OrderResponseDTO convertToResponse(){
        return OrderResponseDTO.builder()
                .orderId(this.orderId)
//                .orderProductId(this.convertToResponse())
                .orderDate(this.orderDate)
                .ekspedisiName(this.ekspedisiId.getName())
                .total(this.total)
                .user(this.userId.convertToResponsePOST())
//                .fullName(this.userId.getFullName())
//                .userName(this.userId.getUsername())
//                .email(this.userId.getEmail())
//                .address(this.userId.getAddress())
//                .phoneNum(this.userId.getPhoneNum())
//                .profilPicture(this.userId.getProfilPicture())
                .build();
    }

    public OrderResponsePOST convertToResponsePOST(){
        return OrderResponsePOST.builder()
                .orderDate(this.orderDate)
                .ekspedisiId(this.ekspedisiId.getEkspedisiId())
                .total(this.total)
                .buyer(this.userId.getUsername())
                .build();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", ekspedisiId=" + ekspedisiId +
                ", orderProduct=" + orderProduct +
                ", total=" + total +
                ", userId=" + userId +
                '}';
    }
}
