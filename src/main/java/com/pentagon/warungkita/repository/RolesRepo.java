package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}
