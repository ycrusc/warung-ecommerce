package com.pentagon.warungkita.controller;

import com.pentagon.warungkita.model.ProductStatus;
import com.pentagon.warungkita.service.ProductStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pentagon/warung-kita")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "10.Product Status")
public class ProductStatusController {

    private final ProductStatusService productStatusService;

    @Operation(summary = "View all Product Status (ADMIN)")
    @GetMapping("/product_status/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllProductStatus() {
       return this.productStatusService.getAll();
    }

    @Operation(summary = "View Product Status by Id (ADMIN)")
    @GetMapping("/product_status/{Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getProductStatusById(@PathVariable Long Id) {
       return this.productStatusService.getProductStatusById(Id);
    }

    @Operation(summary = "Create Product Status (ADMIN)")
    @PostMapping("/product_status/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity <Object> createProductStatus(@RequestBody ProductStatus productStatus) {
        return this.productStatusService.createProductStatus(productStatus);
    }

    @Operation(summary = "Update Product Status by Id (ADMIN)")
    @PutMapping("/product_status/update/{Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateProductStatus(@PathVariable Long Id, @RequestBody ProductStatus productStatus){
       return this.productStatusService.updateProductStatus(Id, productStatus);
    }

    @Operation(summary = "Delete Product Status by Id (ADMIN)")
    @DeleteMapping("/product_status/delete/{Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteProductStatus(@PathVariable Long Id){
       return this.productStatusService.deleteProductStatusById(Id);
    }
}
