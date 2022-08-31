package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByUserIdUsername(String userName);

}
