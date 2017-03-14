package com.example.user.kupurchase1.Purchase.Shopping;

import android.content.Context;
import android.util.Log;

import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 2016-03-14.
 */
public class MakeJsonShoppingData {

    private String TAGLOG = "로그: MakeJsonShoppingData: ";

    public SimpleDateFormat simpleDateFormat;
    public Context context;
    public ArrayList<ShoppingCarts> shoppingCartsArrayList;
    public UserLocalStore userLocalStore;

    public MakeJsonShoppingData(ArrayList<ShoppingCarts> shoppingCartsArrayList, Context context) {
        userLocalStore = new UserLocalStore(context);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        this.shoppingCartsArrayList = shoppingCartsArrayList;
        this.context = context;
    }

    public String makeJsonShoppingData() {
        String jsonString;

        JSONArray productsArray;
        JSONObject userObject = new JSONObject();

        User user = userLocalStore.getLoggedInUser();
        productsArray = makeJsonProductData();

        Date currentDate = new Date();
        String productPurchaseDate = simpleDateFormat.format(currentDate);

        try {
            userObject.put("productPurchaseDate", productPurchaseDate);
            userObject.put("userManagementCode", user.userManagementCode);
            userObject.put("userID", user.userID);
            userObject.put("userName", user.userName);
            userObject.put("userPhoneNumber", user.userPhoneNum);
            userObject.put("userMailAddress", user.userMailAddress);
            userObject.put("products", productsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonString = userObject.toString();
        Log.e(TAGLOG, "jsonString : " + jsonString);

        return jsonString;
    }

    public JSONArray makeJsonProductData() {
        JSONArray productArray = new JSONArray();

        try {
            for(int i = 0 ; i < shoppingCartsArrayList.size() ; i++) {
                JSONObject productObject = new JSONObject();
                productObject.put("productManagementCode", shoppingCartsArrayList.get(i).getKuManagementCode());
                productObject.put("productName", shoppingCartsArrayList.get(i).getKuProductName());
                productObject.put("productPrice", shoppingCartsArrayList.get(i).getKuProductPrice());
                productObject.put("productCount", shoppingCartsArrayList.get(i).getKuProductCount());
                productArray.put(productObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productArray;
    }
}
