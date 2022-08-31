package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.PhotoRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getPhotoById(Long photoId);
    ResponseEntity<Object> createPhoto(PhotoRequestDTO photoRequestDTO,MultipartFile multipartFile);
    ResponseEntity<Object> updatePhoto (Long photoId, PhotoRequestDTO photoRequestDTO);
    ResponseEntity<Object> deletePhoto(Long photoId);
    ResponseEntity<?> downloadFile(String fileCode);

    }
