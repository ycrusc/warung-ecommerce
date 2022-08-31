package com.pentagon.warungkita.model;


import com.pentagon.warungkita.dto.WishlistResponseDTO;
import com.pentagon.warungkita.dto.WishlistResponsePOST;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder

@Table(name = "wishlist")
public class Wishlist {
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public WishlistResponseDTO convertToResponse(){
       return WishlistResponseDTO.builder()
               .product(this.product.convertToResponse())
               .user(this.user.convertToResponsePOST())
               .build();
    }
    public WishlistResponsePOST convertToResponsePost(){
        return WishlistResponsePOST.builder()
                .user_id(this.getUser().getUserId())
                .product_id(this.getProduct().getProductId())
                .build();
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistId=" + wishlistId +
                ", user=" + user +
                ", product=" + product +
                '}';
    }
}
