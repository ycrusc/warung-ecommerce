package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Product;
import com.pentagon.warungkita.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    public List<Product> findByProductNameContaining(String productName);
    public List<Product> findByUsersUsernameContaining(String userName);
    public List<Product> findByUsersUserId(Long userId);
    public List<Product> findByCategoriesName(String name);


}
