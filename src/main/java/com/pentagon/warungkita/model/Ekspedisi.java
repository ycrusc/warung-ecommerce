package com.pentagon.warungkita.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table (name = "ekspedisi")
public class Ekspedisi {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long ekspedisiId;
    private String name;

    @Override
    public String toString() {
        return "Ekspedisi{" +
                "ekspedisiId=" + ekspedisiId +
                ", name='" + name + '\'' +
                '}';
    }
}
