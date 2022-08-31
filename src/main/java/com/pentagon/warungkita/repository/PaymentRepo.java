package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderUserIdUsernameContaining(String userName);
    List<Payment> findByOrderOrderProductProductIdUsersUsernameContaining(String userName);
    List<Payment> findByOrderOrderId(Long id);
}
