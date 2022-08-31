package com.pentagon.warungkita.model;

import com.pentagon.warungkita.dto.PaymentResponseDTO;
import com.pentagon.warungkita.model.Enum.BankList;
import com.pentagon.warungkita.model.Enum.PaymentResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    private LocalDate datePay;
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private BankList ccType;

    private String ccNum;

    @Enumerated(EnumType.STRING)
    private PaymentResponse response;

    private boolean active;

    public PaymentResponseDTO convertToResponse(){
        return PaymentResponseDTO.builder().id_pembayaran(this.getPaymentId())
                .id_order(this.getOrder().getOrderId())
                .tanggal_bayar(this.getDatePay())
                .total(this.getAmount())
                .nomor_kartu(this.getCcNum())
                .tipe_kartu(this.getCcType())
                .status(this.getResponse())
                .build();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", order=" + order +
                ", datePay=" + datePay +
                ", amount=" + amount +
                ", ccNum='" + ccNum + '\'' +
                ", ccType='" + ccType + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}


