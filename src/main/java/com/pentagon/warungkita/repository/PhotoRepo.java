package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepo extends JpaRepository<Photo, Long> {
}
