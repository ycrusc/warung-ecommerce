package com.pentagon.warungkita.model;

import com.pentagon.warungkita.dto.PhotoResponseDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "photos_profile")
public class PhotoProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String photoName;

    public PhotoResponseDTO convertToResponse(){
        return PhotoResponseDTO.builder()
                .kodeFoto(this.getPhotoId())
                .namaFoto(this.getPhotoName())
                .build();
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photoId=" + photoId +
                ", photoName='" + photoName + '\'' +
                '}';
    }
}
