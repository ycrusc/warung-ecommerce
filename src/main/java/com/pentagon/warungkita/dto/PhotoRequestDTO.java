package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Photo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoRequestDTO {
    private String photoName;

    public Photo convertToEntity(){
        return Photo.builder()
                .photoName(photoName)
                .build();
    }
}
