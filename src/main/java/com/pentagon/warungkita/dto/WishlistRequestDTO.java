package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Product;
import com.pentagon.warungkita.model.Wishlist;
import com.pentagon.warungkita.model.Users;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistRequestDTO {
    private Users user;
    private Product product;

    public Wishlist convertToEntity(){
        return Wishlist.builder()
                .user(this.user)
                .product(this.product)
                .build();

    }

}
