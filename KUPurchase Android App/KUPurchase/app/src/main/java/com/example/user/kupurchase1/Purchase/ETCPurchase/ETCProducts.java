package com.example.user.kupurchase1.Purchase.ETCPurchase;

/**
 * Created by user on 2016-03-12.
 */
public class ETCProducts {
    private String uploadProductsDate, userID, titleOfETCProduct, productDetailInform, productExpireDateOfPurchase, productImageURL;
    private int userManagementCode, productManagementCode;

    public String getUploadProductsDate() {
        return uploadProductsDate;
    }

    public void setUploadProductsDate(String uploadProductsDate) {
        this.uploadProductsDate = uploadProductsDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitleOfETCProduct() {
        return titleOfETCProduct;
    }

    public void setTitleOfETCProduct(String titleOfETCProduct) {
        this.titleOfETCProduct = titleOfETCProduct;
    }

    public String getProductDetailInform() {
        return productDetailInform;
    }

    public void setProductDetailInform(String productDetailInform) {
        this.productDetailInform = productDetailInform;
    }

    public String getProductExpireDateOfPurchase() {
        return productExpireDateOfPurchase;
    }

    public void setProductExpireDateOfPurchase(String productExpireDateOfPurchase) {
        this.productExpireDateOfPurchase = productExpireDateOfPurchase;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public int getUserManagementCode() {
        return userManagementCode;
    }

    public void setUserManagementCode(int userManagementCode) {
        this.userManagementCode = userManagementCode;
    }

    public int getProductManagementCode() {
        return productManagementCode;
    }

    public void setProductManagementCode(int productManagementCode) {
        this.productManagementCode = productManagementCode;
    }
}
