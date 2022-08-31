package com.pentagon.warungkita.dto;

import com.pentagon.warungkita.model.Enum.BankList;
import com.pentagon.warungkita.model.Enum.PaymentResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long id_pembayaran;
    private Long id_order;
    private LocalDate tanggal_bayar;
    private Integer total;
    private String nomor_kartu;
    private BankList tipe_kartu;
    private PaymentResponse status;

    @Override
    public String toString() {
        return "PaymentResponseDTO{" +
                "id_pembayaran=" + id_pembayaran +
                ", id_order=" + id_order +
                ", tanggal_bayar=" + tanggal_bayar +
                ", total=" + total +
                ", nomor_kartu='" + nomor_kartu + '\'' +
                ", tipe_kartu='" + tipe_kartu + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
