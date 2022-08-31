package com.pentagon.warungkita.controller;


import com.pentagon.warungkita.model.Ekspedisi;
import com.pentagon.warungkita.service.EkspedisiService;
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
@Tag(name = "Ekspedisi")
public class EkspedisiController {

    @Autowired
    EkspedisiService ekspedisiService;

    @Operation(summary = "View all Ekspedisi (ADMIN, BUYER, SELLER)")
    @GetMapping("/ekspedisi/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_SELLER')or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<Object> getAllEkspedisi() {

        return this.ekspedisiService.getAll();
    }

    @Operation(summary = "Get Ekspedisi By Id (ADMIN)")
    @GetMapping("/ekspedisi/{Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getEkspedisiById(@PathVariable Long Id) {
        return this.ekspedisiService.getEkspedisiById(Id);
    }

    @Operation(summary = "Create new Ekspedisi (ADMIN)")
    @PostMapping("/ekspedisi/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity <Object> createEkspedisi(@RequestBody Ekspedisi ekspedisi) {
        return this.ekspedisiService.createEkspedisi(ekspedisi);
       }

    @Operation(summary = "Update Ekspedisi by ID (ADMIN)")
    @PutMapping("/ekspedisi/update/{Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateEkspedisi(@PathVariable Long Id, @RequestBody Ekspedisi ekspedisiDetails){
        return this.ekspedisiService.updateEkspedisi(Id ,ekspedisiDetails);
    }

    @Operation(summary = "Delete Ekspedisi (ADMIN)")
    @DeleteMapping("/ekspedisi/delete/{Id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteEkspedisi(@PathVariable Long Id){
        return this.ekspedisiService.deleteEkspedisiById(Id);
    }
}
