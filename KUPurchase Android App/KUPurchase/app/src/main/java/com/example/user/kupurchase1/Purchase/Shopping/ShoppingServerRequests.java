package com.example.user.kupurchase1.Purchase.Shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-16.
 */
public class ShoppingServerRequests {

    private String TAGLOG = "로그: ShoppingServerRequests: ";

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://172.30.1.49/KUPurchase/";

    public ProgressDialog progressDialog;

    public ShoppingServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("등록 중");
        progressDialog.setMessage("잠시만 기다려주세요...");
    }

    public void storeShoppingDataInBackground(String shoppingJsonString, GetShoppingCallback getShoppingCallback) {
        progressDialog.show();
        new StoreShoppingAsyncTask(shoppingJsonString, getShoppingCallback).execute();
    }

    public class StoreShoppingAsyncTask extends AsyncTask<Void, Void, String> {

        GetShoppingCallback getShoppingCallback;
        String shoppingJsonString;

        public StoreShoppingAsyncTask(String shoppingJsonString, GetShoppingCallback getShoppingCallback) {
            this.getShoppingCallback = getShoppingCallback;
            this.shoppingJsonString = shoppingJsonString;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                JSONObject jsonObject = new JSONObject(shoppingJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("products");

                if(jsonArray.length() == 0) {
                    return null;
                } else {
                    ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                    dataToSend.add(new BasicNameValuePair("jsonString", shoppingJsonString));

                    HttpParams httpRequestParam = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

                    HttpClient client = new DefaultHttpClient(httpRequestParam);
                    HttpPost post = new HttpPost(SERVER_ADDRESS + "PurchaseKuProducts.php");

                    try {
                        post.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                        client.execute(post);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "products";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            getShoppingCallback.done(result);
        }
    }
}
