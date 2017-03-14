package com.example.user.kupurchase1.Purchase.Shopping;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-14.
 */
public class ShoppingCarts {

    private String kuProductName;
    private int kuProductPrice, kuProductCount, kuProductManagementCode;
    private ArrayList<ShoppingCarts> shoppingCartsArrayList;

    public ArrayList<ShoppingCarts> getShoppingCartsArrayList(){
        return shoppingCartsArrayList;
    }

    public void setShoppingCartsArrayList(ArrayList<ShoppingCarts> shoppingCartsArrayList) {
        this.shoppingCartsArrayList = shoppingCartsArrayList;
    }

    public String getKuProductName() {
        return kuProductName;
    }

    public void setKuProductName(String kuProductName) {
        this.kuProductName = kuProductName;
    }

    public int getKuProductPrice() {
        return kuProductPrice;
    }

    public void setKuProductPrice(int kuProductPrice) {
        this.kuProductPrice = kuProductPrice;
    }

    public int getKuProductCount() {
        return kuProductCount;
    }

    public void setKuProductCount(int kuProductCount) {
        this.kuProductCount = kuProductCount;
    }

    public int getKuManagementCode() {
        return kuProductManagementCode;
    }

    public void setKuProductManagementCode(int kuProductManagementCode) {
        this.kuProductManagementCode = kuProductManagementCode;
    }
}
