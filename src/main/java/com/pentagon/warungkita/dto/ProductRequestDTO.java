package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDTO {
    private String sku;
    private String productName;
    private List<Categories> categories;
    private String description;

    private Integer regularPrice;
    private Integer quantity;
    private List<Photo> productPicture;


    public Product convertToEntity(Users users){
        return Product.builder()
                .sku(this.sku)
                .productName(this.productName)
                .categories(this.categories)
                .description(this.description)

                .regularPrice(this.regularPrice)
                .quantity(this.quantity)
                .productPicture(this.productPicture)
                .users(users)
                .build();
    }
}
