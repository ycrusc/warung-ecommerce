package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Enum.BankList;
import com.pentagon.warungkita.model.Enum.PaymentResponse;
import com.pentagon.warungkita.model.Order;
import com.pentagon.warungkita.model.Payment;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Order order;
    @CreatedDate
    private LocalDate tanggalBayar;
    private Integer amount;
    private String nomorRekening;
    private BankList namaBank;
    private PaymentResponse response;

    public Payment convertToEntity(){
        return Payment.builder()
                .order(this.order)
                .datePay(this.tanggalBayar)
                .amount(this.amount)
                .ccNum(this.nomorRekening)
                .ccType(this.namaBank)
                .response(this.response)
                .build();
    }
}
