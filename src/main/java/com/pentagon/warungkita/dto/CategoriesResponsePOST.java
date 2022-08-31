package com.pentagon.warungkita.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesResponsePOST {
    private Long kodeKategori;
    private String namaKategori;

    @Override
    public String toString() {
        return "CategoriesResponsePOST{" +
                "kodeKategori=" + kodeKategori +
                ", namaKategori='" + namaKategori + '\'' +
                '}';
    }
}
