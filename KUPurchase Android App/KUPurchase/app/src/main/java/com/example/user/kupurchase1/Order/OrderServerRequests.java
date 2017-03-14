package com.example.user.kupurchase1.Order;

import android.os.AsyncTask;
import android.util.Log;

import com.example.user.kupurchase1.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dahye on 2016-03-23.
 */
public class OrderServerRequests {

    private String TAGLOG = "로그: OrderServerRequests : ";

    int usercode;
    int autoCode;
    String userManagementCode;
    String autoIncrementCode;

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://172.30.1.49/KUPurchase/";

    public String products[];

    public OrderServerRequests(int autoIncrement, int userManagementCode) {
        this.usercode = userManagementCode;
        this.autoCode = autoIncrement;

    }
    public OrderServerRequests(int userManagementCode) {
        this.usercode = userManagementCode;
    }


    public void fetchOrderKuProductInBackground(GetOrderProductCallback getOrderProductCallback){
        new FetchOrderAsyncTask(getOrderProductCallback).execute();
    }

    public void deleteOrder(OrderConstructor orderConstructor, GetOrderProductCallback getOrderProductCallback){
        new DeleteOrderAsyncTask(orderConstructor, getOrderProductCallback).execute();
    }

    public class FetchOrderAsyncTask extends AsyncTask<Void, Void, ArrayList<OrderConstructor>> {
       //111 Testing ...
        User user;
        GetOrderProductCallback getOrderProductCallback;

        public FetchOrderAsyncTask( GetOrderProductCallback getOrderProductCallback){

            this.getOrderProductCallback = getOrderProductCallback;
        }

        @Override
        protected ArrayList<OrderConstructor> doInBackground(Void... params) {

            userManagementCode = String.valueOf(usercode);
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userManagementCode", userManagementCode));


            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + "FetchKuPurchaseProducts.php");

            ArrayList<OrderConstructor> returnedOrders = new ArrayList<>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                client.execute(httpPost);

                HttpResponse httpResponse = client.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e(TAGLOG, "json result : " + result);

                JSONObject jsonObject = new JSONObject(result);

                for(int i =0; i<jsonObject.length()-1;i++)
                {
                    JSONObject jsonObject2 = new JSONObject(jsonObject.getString(""+i));
                    String productName = jsonObject2.getString("productName");
                    int autoIncrement = jsonObject2.getInt("autoIncrement");
                    int productCount = jsonObject2.getInt("productCount");
                    int productPrice = jsonObject2.getInt("productPrice");
                    int productDepositState = jsonObject2.getInt("productDepositState");

                    int totalPrice = productPrice*productCount;

                    OrderConstructor orderConstructor = new OrderConstructor( autoIncrement, productName, productCount, productPrice, productDepositState, totalPrice);

                    returnedOrders.add(orderConstructor);

                }

                //products = new String[jsonObject.length()];

                /*
                if(jsonObject.length() != 0){
                    String resultCode = jsonObject.getString("resultCode");
                    Log.e(TAGLOG, "resultCode : " + resultCode);
                    for(int i=0; i< jsonObject.length()-1; i++){
                        products[i] = jsonObject.getString(String.valueOf(i));
                        Log.e(TAGLOG, "products[" + i + "] : " + products[i]);
                        JSONObject productObject = new JSONObject(products[i]);
                        */

                        /*
                        String productName = productObject.getString("productName");
                        int productCount = productObject.getInt("productCount");
                        int productPrice = productObject.getInt("productPrice");
                        int productDepositState = productObject.getInt("productDepositState");

                        OrderConstructor orderConstructor = new OrderConstructor(productName, productCount, productPrice, productDepositState);

                        returnedOrders.add(orderConstructor);
                        */
                    //}
                //}
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnedOrders;
        }

        @Override
        protected void onPostExecute(ArrayList<OrderConstructor> orderKuProductses) {
            super.onPostExecute(orderKuProductses);
            getOrderProductCallback.done(orderKuProductses);
        }
    }

    public class DeleteOrderAsyncTask extends AsyncTask<Void, Void, Void>{

        OrderConstructor orderConstructor;
        GetOrderProductCallback getOrderProductCallback;


        public DeleteOrderAsyncTask(OrderConstructor orderConstructor, GetOrderProductCallback getOrderProductCallback){
            this.orderConstructor = orderConstructor;
            this.getOrderProductCallback = getOrderProductCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            userManagementCode = String.valueOf(usercode);
            autoIncrementCode = String.valueOf(autoCode);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("uerManagementCode", userManagementCode));
            dataToSend.add(new BasicNameValuePair("autoIncrementNum", autoIncrementCode));

            System.out.println("testdahye2 :"+ dataToSend);

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + "DeletePurchaseKuProduct.php");


            try {
               httpPost.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                client.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getOrderProductCallback.done(null);
        }
    }
}
