package com.example.quickcart.ui.main;

public class CartModel {
    String productName;
    String imageURL;
    int quantityPicked;
    float unitPrice;
    float subtotal;
    Long productID;


    public CartModel(){}
    public CartModel(String productName, Long productID, String imageURL, int quantityPicked, float unitPrice, float subtotal) {
        this.productName = productName;
        this.productID = productID;
        this.imageURL = imageURL;
        this.quantityPicked = quantityPicked;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }


    @Override
    public String toString() {
        return "CartModel{" +
                "productName='" + productName + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", quantityPicked=" + quantityPicked +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", productID=" + productID +
                '}';
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantityPicked() {
        return quantityPicked;
    }

    public void setQuantityPicked(int quantityPicked) {
        this.quantityPicked = quantityPicked;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }
}
