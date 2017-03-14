package com.example.user.kupurchase1.Purchase.KUPurchase;

/**
 * Created by user on 2016-03-11.
 */
public class KuProductConstructor {

    public int kuProductManagementCode, kuProductPrice;
    public String kuProductImageURL, kuProductName, kuProductExpireDate, kuProductDepositStartDate, kuProductDepositDueDate ;

    public KuProductConstructor(int kuProductManagementCode, String kuProductImageURL, String kuProductName, int kuProductPrice,
                                    String kuProductExpireDate, String kuProductDepositStartDate, String kuProductDepositDueDate) {
        this.kuProductManagementCode = kuProductManagementCode;
        this.kuProductImageURL = kuProductImageURL;
        this.kuProductName = kuProductName;
        this.kuProductPrice = kuProductPrice;
        this.kuProductExpireDate = kuProductExpireDate;
        this.kuProductDepositStartDate = kuProductDepositStartDate;
        this.kuProductDepositDueDate = kuProductDepositDueDate;
    }
}
