package com.pentagon.warungkita.controller;

import com.pentagon.warungkita.dto.WishlistRequestDTO;
import com.pentagon.warungkita.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pentagon/warung-kita")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "05.Wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @Operation(summary = "View all wishlist (ADMIN)")
    @GetMapping("/wishlist/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findAllWishlist(){
         return this.wishlistService.getAllWishlist();
        }

    @Operation(summary = "View wishlist by Id (ADMIN)")
    @GetMapping("/wishlist/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getWishlistById(@PathVariable Long id){
        return this.wishlistService.getWishlistById(id);
    }

    @Operation(summary = "Create wishlist (BUYER)")
    @PostMapping("/wishlist/create")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> wishlistCreate(@RequestBody WishlistRequestDTO wishlistRequestDTO){
       return this.wishlistService.createWishlist(wishlistRequestDTO);
    }

    @Operation(summary = "Delete wishlist (ADMIN, BUYER)")
    @DeleteMapping("wishlist/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> deletewishlist(@PathVariable Long id){
        return this.wishlistService.deleteProductListById(id);
    }

    @Operation(summary = "View own wishlist (BUYER)")
    @GetMapping("/wishlist")
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> findWishlistByUserName(){
        return this.wishlistService.findByUserUsernameContaining();
    }
}
