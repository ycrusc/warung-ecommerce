package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.CategoriesRequestDTO;
import org.springframework.http.ResponseEntity;

public interface CategoriesService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getCategoriesById(Long categoriesId);
    ResponseEntity<Object> createCategories(CategoriesRequestDTO categoriesRequestDTO);
    ResponseEntity<Object> updateCategories(Long categoriesId, CategoriesRequestDTO categoriesRequestDTO);
    ResponseEntity<Object> deleteCategories(Long categoriesId);

}
