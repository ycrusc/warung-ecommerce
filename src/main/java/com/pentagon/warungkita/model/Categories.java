package com.pentagon.warungkita.model;

import com.pentagon.warungkita.dto.CategoriesResponseDTO;
import com.pentagon.warungkita.dto.CategoriesResponsePOST;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoriesId;
    private String name;

    public CategoriesResponseDTO convertToResponse(){
        return CategoriesResponseDTO.builder()
                .kodeKategori(this.getCategoriesId())
                .namaKategori(this.getName())
                .build();
    }

    public CategoriesResponsePOST convertToResponsePost(){
        return CategoriesResponsePOST.builder()
                .kodeKategori(this.getCategoriesId())
                .namaKategori(this.getName())
                .build();
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categoriesId=" + categoriesId +
                ", name='" + name + '\'' +
                '}';
    }
}
