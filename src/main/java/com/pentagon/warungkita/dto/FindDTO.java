package com.pentagon.warungkita.dto;

public class FindDTO {
    private String username;
    private String productName;
    private String description;
    private String quantity;
    private String price;
    private String total;
    private String ekspedisiName;

    public FindDTO(String username, String productName, String description, String quantity, String price, String total, String ekspedisiName) {
        this.username = username;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.ekspedisiName = ekspedisiName;
    }

    @Override
    public String toString() {
        return "FindDTO{" +
                "username='" + username + '\'' +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", total='" + total + '\'' +
                ", ekspedisiName='" + ekspedisiName + '\'' +
                '}';
    }
}
