package com.pentagon.warungkita.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesResponseDTO {
    private Long kodeKategori;
    private String namaKategori;

    @Override
    public String toString() {
        return "CategoriesResponseDTO{" +
                "Kode Kategri=" + kodeKategori +
                ", Nama Kategori='" + namaKategori + '\'' +
                '}';
    }
}
