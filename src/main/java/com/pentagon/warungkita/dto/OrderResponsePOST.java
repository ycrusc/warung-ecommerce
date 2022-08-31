package com.pentagon.warungkita.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponsePOST {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private Long ekspedisiId;
    private Number total;
    private String buyer;

    @Override
    public String toString() {
        return "OrderResponsePOST{" +
                "orderDate=" + orderDate +
                ", ekspedisiId=" + ekspedisiId +
                ", total=" + total +
                ", buyer='" + buyer + '\'' +
                '}';
    }
}
