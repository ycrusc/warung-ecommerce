package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepo extends JpaRepository<Categories, Long> {
    Categories findByName(String name);

}
