package com.pentagon.warungkita.repository;

import com.pentagon.warungkita.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

        List<Wishlist> findByUserUsernameContaining(String userName);



}
