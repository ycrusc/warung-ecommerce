package com.pentagon.warungkita.model;

import com.pentagon.warungkita.dto.ProductResponseDTO;
import com.pentagon.warungkita.dto.ProductResponsePOST;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String sku;
    private String productName;
    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    private List<Categories> categories;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @ManyToOne
    @JoinColumn(name = "product_status_id")
    private ProductStatus productStatusId;

     private Integer regularPrice;

    private Integer quantity;
    @OneToMany
    @JoinTable(name = "photo_product",
            joinColumns = @JoinColumn (name = "product_id"),
            inverseJoinColumns = @JoinColumn (name = "photo_id" ))
    private List<Photo> productPicture;


    public ProductResponseDTO convertToResponse(){
        return ProductResponseDTO.builder()
                .kodeProduk(this.getProductId())
                .sku(this.getSku())
                .namaProduk(this.getProductName())
                .kategori(this.getCategories())
                .deskripsi(this.getDescription())
                .status(this.getProductStatusId())
                .harga(this.getRegularPrice())
                .stok(this.getQuantity())
                .gambar(this.getProductPicture())
                .sellerName(this.users.getUsername())
                .build();
    }

    public ProductResponsePOST convertToResponsePost(){
        return ProductResponsePOST.builder()
                .kodeProduk(this.getProductId())
                .sku(this.getSku())
                .namaProduk(this.getProductName())
                .deskripsi(this.getDescription())
                .status(this.productStatusId.getProductStatusId())
                .harga(this.getRegularPrice())
                .stok(this.getQuantity())
                .userId(this.users.getUserId())
                .build();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", sku='" + sku + '\'' +
                ", productName='" + productName + '\'' +
                ", categories=" + categories +
                ", description='" + description + '\'' +
                ", users=" + users +
                ", productStatusId=" + productStatusId +
                ", regularPrice=" + regularPrice +
                ", quantity=" + quantity +
                ", productPicture=" + productPicture +
                '}';
    }
}
