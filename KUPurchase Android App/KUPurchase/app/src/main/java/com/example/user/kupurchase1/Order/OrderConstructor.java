package com.example.user.kupurchase1.Order;

/**
 * Created by dahye on 2016-03-23.
 */
public class OrderConstructor {
    public String kuProductName;
    public int kuProductPrice;
    public int kuProductCount;
    public int autoIncrement;
    public int totalPrice;
    public int kuProductDepositState;
    public int userManagementCode;

    public OrderConstructor(int autoIncrement, String kuProductName, int kuProductCount, int kuProductPrice,  int kuProductDepositState, int totalPrice){
        this.autoIncrement = autoIncrement;
        this.kuProductName = kuProductName;
        this.kuProductPrice = kuProductPrice;
        this.kuProductCount = kuProductCount;
        this.kuProductDepositState = kuProductDepositState;
        this.totalPrice = totalPrice;
    }

    public OrderConstructor(int autoIncrement, int userManagementCode){
        this.autoIncrement = autoIncrement;
        this.userManagementCode = userManagementCode;
    }
}
