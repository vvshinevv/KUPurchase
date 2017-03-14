package com.example.user.kupurchase1.Purchase.Shopping;

/**
 * Created by user on 2016-03-14.
 */
public class ShoppingConstructor {

    public String kuProductName;
    public int kuProductPrice, kuProductCount, kuProductManagementCode;


    public ShoppingConstructor(String kuProductName, int kuProductPrice, int kuProductCount, int kuProductManagementCode) {
        this.kuProductName = kuProductName;
        this.kuProductPrice = kuProductPrice;
        this.kuProductCount = kuProductCount;
        this.kuProductManagementCode = kuProductManagementCode;
    }
}
