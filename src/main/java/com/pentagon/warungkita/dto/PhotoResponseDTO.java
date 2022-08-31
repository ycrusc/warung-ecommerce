package com.pentagon.warungkita.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoResponseDTO {
    private Long kodeFoto;
    private String namaFoto;

    @Override
    public String toString() {
        return "PhotoResponseDTO{" +
                "kodeFoto=" + kodeFoto +
                ", namaFoto='" + namaFoto + '\'' +
                '}';
    }
}
