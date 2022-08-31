package com.pentagon.warungkita.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResponsePOST {
    private Long user_id;
    private Long product_id;

    @Override
    public String toString() {
        return "ProductListResponsePOST{" +
                "user_id=" + user_id +
                ", product_id=" + product_id +
                '}';
    }
}

