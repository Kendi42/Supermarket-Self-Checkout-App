package com.example.quickcart;

public class Product {
    private Long ProductID;
    private String Aisle;
    private String InStockStatus;
    private String ProductImage;
    private String ProductName;
    private String ProductDetails;
    private int QuantityAvailable;
    private int QuantityInCarts;
    private int StockQuantity;
    private float UnitPrice;


    // CONSTRUCTORS
    // Empty
    public Product(){}
    // Everything
    public Product(Long productID, String productDetails, String aisle, String inStockStatus, String productImage, String productName, int quantityAvailable, int quantityInCarts, int stockQuantity, float unitPrice) {
        ProductID = productID;
        Aisle = aisle;
        ProductDetails = productDetails;
        InStockStatus = inStockStatus;
        ProductImage = productImage;
        ProductName = productName;
        QuantityAvailable = quantityAvailable;
        QuantityInCarts = quantityInCarts;
        StockQuantity = stockQuantity;
        UnitPrice = unitPrice;
    }
    // except product ID
    public Product(String productDetails, String aisle, String inStockStatus, String productImage, String productName, int quantityAvailable, int quantityInCarts, int stockQuantity, float unitPrice) {
        Aisle = aisle;
        ProductDetails = productDetails;
        InStockStatus = inStockStatus;
        ProductImage = productImage;
        ProductName = productName;
        QuantityAvailable = quantityAvailable;
        QuantityInCarts = quantityInCarts;
        StockQuantity = stockQuantity;
        UnitPrice = unitPrice;
    }

    // Relevant to the Customer- Shows up on the dialogue box
    public Product(String aisle,String productDetails, String productImage, String productName, float unitPrice) {
        Aisle = aisle;
        ProductImage = productImage;
        ProductName = productName;
        UnitPrice = unitPrice;
        ProductDetails = productDetails;

    }

    // Relevant to customer- Show up when searching for product to addd
    public Product (String productImage, String productName){
        ProductImage = productImage;
        ProductName = productName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "ProductID='" + ProductID + '\'' +
                ", Aisle='" + Aisle + '\'' +
                ", InStockStatus='" + InStockStatus + '\'' +
                ", ProductImage='" + ProductImage + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", ProductDetails='" + ProductDetails + '\'' +
                ", QuantityAvailable=" + QuantityAvailable +
                ", QuantityInCarts=" + QuantityInCarts +
                ", StockQuantity=" + StockQuantity +
                ", UnitPrice=" + UnitPrice +
                '}';
    }
    // GETTERS AND SETTERS


    public Long getProductID() {
        return ProductID;
    }

    public void setProductID(Long productID) {
        ProductID = productID;
    }

    public String getAisle() {
        return Aisle;
    }

    public void setAisle(String aisle) {
        Aisle = aisle;
    }

    public String getInStockStatus() {
        return InStockStatus;
    }

    public void setInStockStatus(String inStockStatus) {
        InStockStatus = inStockStatus;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantityAvailable() {
        return QuantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        QuantityAvailable = quantityAvailable;
    }

    public int getQuantityInCarts() {
        return QuantityInCarts;
    }

    public void setQuantityInCarts(int quantityInCarts) {
        QuantityInCarts = quantityInCarts;
    }

    public int getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        StockQuantity = stockQuantity;
    }

    public float getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.UnitPrice = unitPrice;
    }

    public String getProductDetails() {
        return ProductDetails;
    }

    public void setProductDetails(String productDetails) {
        ProductDetails = productDetails;
    }
}
