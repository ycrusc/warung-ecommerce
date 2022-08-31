package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Product;
import com.pentagon.warungkita.model.Users;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResponseDTO {
    private ProductResponseDTO product;
    private UsersResponsePOST user;

    public void setProduct (Product product){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        this.product.setNamaProduk(product.getProductName());
        this.product.setSellerName(product.getUsers().getUsername());
    }

    public void setUser (Users users){
        UsersResponsePOST usersResponsePOST = new UsersResponsePOST();
        this.user = usersResponsePOST;
        this.user.setEmail(users.getEmail());
        this.user.setUsername(users.getUsername());
    }


}
