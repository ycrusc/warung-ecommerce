package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.PhotoRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoProfileService {
    ResponseEntity<Object> createPhoto(PhotoRequestDTO photoRequestDTO, MultipartFile multipartFile);
}
