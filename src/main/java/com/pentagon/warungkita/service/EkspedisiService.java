package com.pentagon.warungkita.service;

import com.pentagon.warungkita.model.Ekspedisi;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EkspedisiService {
    ResponseEntity<Object> getAll();
    ResponseEntity <Object> createEkspedisi(Ekspedisi ekspedisi);
    ResponseEntity<Object> getEkspedisiById(Long Id);
    ResponseEntity<Object> deleteEkspedisiById(Long Id);
    ResponseEntity<Object> updateEkspedisi(Long Id,Ekspedisi ekspedisiDetails);
}
