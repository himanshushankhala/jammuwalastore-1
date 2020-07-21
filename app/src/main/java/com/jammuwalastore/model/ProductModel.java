package com.jammuwalastore.model;

/**
 * Created by Kishan Singh.
 */
public class ProductModel {
    private int id;
    private String Productid;
    private String ProductName;
    private String ProductPrice;
    private int ProductQuantity;
    private String ProductActualPrice;
    private String ProductImage;
    private String TotalPrice;
    private String product_weight;
    private String tax_status;
    private String tax_class;


    public ProductModel() {

    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }

    public String getTax_class() {
        return tax_class;
    }

    public void setTax_class(String tax_class) {
        this.tax_class = tax_class;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }


    public String getProductActualPrice() {
        return ProductActualPrice;
    }

    public void setProductActualPrice(String productActualPrice) {
        ProductActualPrice = productActualPrice;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}