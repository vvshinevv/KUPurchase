package com.example.user.kupurchase1.Purchase.KUPurchase;

/**
 * Created by user on 2016-03-09.
 */
public class KuProducts {

    public static final String SERVER_ADDRESS = "http://52.79.93.255/KUPurchase/";

    private int kuProductManagementCode;
    private String kuProductImageURL;
    private String kuProductName;
    private int kuProductPrice;
    private String kuProductExpireDate;
    private String kuProductDepositStartDate;
    private String kuProductDepositDueDate;

    public int getKuProductManagementCode() {
        return kuProductManagementCode;
    }

    public void setKuProductManagementCode(int kuProductManagementCode) {
        this.kuProductManagementCode = kuProductManagementCode;
    }
    public String getKuProductImageURL() {
        return kuProductImageURL;
    }

    public void setKuProductImageURL(String kuProductImageURL) {
        this.kuProductImageURL = SERVER_ADDRESS + kuProductImageURL.replace("\\", "");
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

    public String getKuProductExpireDate() {
        return kuProductExpireDate;
    }

    public void setKuProductExpireDate(String kuProductExpireDate) {
        this.kuProductExpireDate = kuProductExpireDate;
    }

    public String getKuProductDepositStartDate() {
        return kuProductDepositStartDate;
    }

    public void setKuProductDepositStartDate(String kuProductDepositStartDate) {
        this.kuProductDepositStartDate = kuProductDepositStartDate;
    }

    public String getKuProductDepositDueDate() {
        return kuProductDepositDueDate;
    }

    public void setKuProductDepositDueDate(String kuProductDepositDueDate) {
        this.kuProductDepositDueDate = kuProductDepositDueDate;
    }
}
