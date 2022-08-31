package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Categories;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesRequestDTO {
    private String name;

    public Categories convertToEntity(){
        return Categories.builder()
                .name(this.name)
                .build();
    }
}
