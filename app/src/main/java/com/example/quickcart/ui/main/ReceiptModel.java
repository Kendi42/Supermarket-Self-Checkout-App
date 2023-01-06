package com.example.quickcart.ui.main;

public class ReceiptModel {
    String date;
    String total;

    public ReceiptModel() {
    }

    public ReceiptModel(String date, String total) {
        this.date = date;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ReceiptModel{" +
                "date='" + date + '\'' +
                ", total=" + total +
                '}';
    }
}
