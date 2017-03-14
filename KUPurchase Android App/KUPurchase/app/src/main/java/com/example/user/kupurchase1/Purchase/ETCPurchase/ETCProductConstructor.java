package com.example.user.kupurchase1.Purchase.ETCPurchase;

/**
 * Created by user on 2016-03-12.
 */
public class ETCProductConstructor {

    public String uploadProductsDate, userID, titleOfETCProduct, productDetailInform, productExpireDateOfPurchase, productImageURL;
    public int userManagementCode, productManagementCode;

    public ETCProductConstructor(String uploadProductsDate, String userID, String titleOfETCProduct, String productDetailInform, String productExpireDateOfPurchase,
                                 String productImageURL, int userManagementCode, int productManagementCode) {
        this.uploadProductsDate = uploadProductsDate;
        this.userID = userID;
        this.titleOfETCProduct = titleOfETCProduct;
        this.productDetailInform = productDetailInform;
        this.productExpireDateOfPurchase = productExpireDateOfPurchase;
        this.productImageURL = productImageURL;
        this.userManagementCode = userManagementCode;
        this.productManagementCode = productManagementCode;
    }

    public ETCProductConstructor(String userID, String titleOfETCProduct, String productDetailInform, String productExpireDateOfPurchase) {
        this.uploadProductsDate = "";
        this.userID = userID;
        this.titleOfETCProduct = titleOfETCProduct;
        this.productDetailInform = productDetailInform;
        this.productExpireDateOfPurchase = productExpireDateOfPurchase;
        this.productImageURL = "";
        this.userManagementCode = -1;
        this.productManagementCode = -1;
    }

}
