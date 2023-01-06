package com.example.quickcart.ui.main;

public class ListModel {
    String productName;
    Long productID;
    float unitPrice;
    float subtotal;
    Boolean checkedStatus;
    int quantityPicked;

    public ListModel(){}

    public ListModel(String productName, int quantityPicked, Long productID, float unitPrice, float subtotal, Boolean checkedStatus) {
        this.productName = productName;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.checkedStatus = checkedStatus;
        this.quantityPicked = quantityPicked;
    }

    @Override
    public String toString() {
        return "ListModel{" +
                "productName='" + productName + '\'' +
                ", productID=" + productID +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", checkedStatus='" + checkedStatus + '\'' +
                ", quantityPicked=" + quantityPicked +
                '}';
    }

    public String getProductName() {
        return productName;
    }


    public void setProductName(String productName) {
        this.productName = productName;
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

    public Boolean getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(Boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }
}
