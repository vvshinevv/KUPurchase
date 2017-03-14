package com.example.user.kupurchase1.Order;

import java.util.ArrayList;

/**
 * Created by dahye on 2016-03-23.
 */
public class OrderKuProducts {

    private String kuProductName;
    private int kuProductPrice;
    private int kuProductCount;
    private int kuProductManagementCode;
    private int kuProductDepositState;
    private int autoIncrement;

    public int getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(int autoIncrement) {
        this.autoIncrement = autoIncrement;
    }


    private ArrayList<OrderKuProducts> orderKuProductsArrayList;

    public ArrayList<OrderKuProducts> getOrderKuProductsArrayList(){ return orderKuProductsArrayList; }

    public void setOrderKuProductsArrayList(ArrayList<OrderKuProducts> orderKuProductsArrayList){
        this.orderKuProductsArrayList =orderKuProductsArrayList;
    }

    public String getKuProductName(){ return kuProductName; }
    public void setKuProductName(String kuProductName) { this.kuProductName = kuProductName; }

    public int getKuProductPrice(){ return kuProductPrice; }
    public void setKuProductPrice(int kuProductPrice) { this.kuProductPrice = kuProductPrice; }

    public int getKuProductCount(){ return kuProductCount;}
    public void setKuProductCount(int kuProductCount) { this.kuProductCount = kuProductCount; }

    public int getKuProductManagementCode(){return kuProductManagementCode; }
    public void setKuProductManagementCode(int kuProductManagementCode){ this.kuProductManagementCode = kuProductManagementCode; }

    public int getKuProductDepositState(){return kuProductDepositState; }
    public void setKuProductDepositState(int kuProductDepositState){ this.kuProductDepositState = kuProductDepositState; }

}
