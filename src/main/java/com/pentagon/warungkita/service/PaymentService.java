package com.pentagon.warungkita.service;

import com.pentagon.warungkita.dto.PaymentRequestDTO;
import com.pentagon.warungkita.model.Payment;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<Object> getAllPayment();
    ResponseEntity<Object> getPaymentById(Long Id);
    ResponseEntity<Object> createPayment(PaymentRequestDTO paymentRequestDTO);
    ResponseEntity<Object> deletePaymentById(Long Id);
    ResponseEntity<Object> updatePayment(Long id, PaymentRequestDTO paymentRequestDTO);
    Payment getReferenceById (Long Id);
    ResponseEntity<Object>  findByOrderUserIdUsernameContaining();
    ResponseEntity<Object> cancelPaymnet(Long Id);
    ResponseEntity<Object>  historiSeller();
}
