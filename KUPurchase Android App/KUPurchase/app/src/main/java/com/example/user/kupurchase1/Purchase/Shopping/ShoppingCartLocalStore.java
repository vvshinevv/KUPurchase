package com.example.user.kupurchase1.Purchase.Shopping;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2016-03-16.
 */
public class ShoppingCartLocalStore {
    public static final String SP_NAME = "cartDetails";

    SharedPreferences shoppingCartLocalDatabase;

    public ShoppingCartLocalStore(Context context) {
        shoppingCartLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeShoppingCartData(ShoppingConstructor shoppingConstructor) {
        SharedPreferences.Editor spEditor = shoppingCartLocalDatabase.edit();
        spEditor.putString("kuProductName", shoppingConstructor.kuProductName);
        spEditor.putInt("kuProductPrice", shoppingConstructor.kuProductPrice);
        spEditor.putInt("kuProductCount", shoppingConstructor.kuProductCount);
        spEditor.putInt("kuProductManagementCode", shoppingConstructor.kuProductManagementCode);
        spEditor.commit();
    }

    public ShoppingConstructor getShoppingCartData() {
        String kuProductName = shoppingCartLocalDatabase.getString("kuProductName", "");
        int kuProductPrice = shoppingCartLocalDatabase.getInt("kuProductPrice", -1);
        int kuProductCount = shoppingCartLocalDatabase.getInt("kuProductCount", -1);
        int kuProductManagementCode = shoppingCartLocalDatabase.getInt("kuProductManagementCode", -1);

        ShoppingConstructor shoppingConstructor = new ShoppingConstructor(kuProductName, kuProductPrice, kuProductCount, kuProductManagementCode);

        return shoppingConstructor;
    }
}
