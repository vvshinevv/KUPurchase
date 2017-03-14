package com.example.user.kupurchase1.Purchase.ETCPurchase;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by user on 2016-03-12.
 */
public class ETCProductServerRequests {

    private String TAGLOG = "로그: ETCProductServerRequests: ";

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://172.30.1.49/KUPurchase/";

    public ProgressDialog progressDialog;
    public String products[];

    public ETCProductServerRequests() {}

    public ETCProductServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("잠시만 기다려주세요...");
    }
    public void storeETCProductsListInBackground(ETCProductConstructor etcProduct, Bitmap bitmap, GetETCProductCallback etcProductCallback) {
        progressDialog.show();
        new StoreETCProductsListAsyncTask(etcProduct, bitmap, etcProductCallback).execute();
    }

    public void fetchETCProductsListInBackground(GetETCProductCallback getETCProductCallback) {
        new FetchETCProductsListAsyncTask(getETCProductCallback).execute();
    }

    public void updateETCProductsListInBackground(ETCProductConstructor etcProduct, Bitmap bitmap, GetETCProductCallback getETCProductCallback) {
        progressDialog.show();
        new UpdateETCProductsListAsyncTask(etcProduct, bitmap, getETCProductCallback).execute();

    }

    public void deleteETCProductsListInBackground(int productManagementCode, GetETCProductCallback getETCProductCallback) {
        progressDialog.show();
        new DeleteETCProductsListAsyncTask(productManagementCode, getETCProductCallback).execute();

    }
    public class StoreETCProductsListAsyncTask extends AsyncTask<Void, Void, Void> {

        ETCProductConstructor etcProduct;
        Bitmap image;
        GetETCProductCallback etcProductCallback;

        public StoreETCProductsListAsyncTask(ETCProductConstructor etcProduct, Bitmap image, GetETCProductCallback etcProductCallback) {
            this. etcProduct = etcProduct;
            this.image = image;
            this.etcProductCallback = etcProductCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userID", etcProduct.userID));

            dataToSend.add(new BasicNameValuePair("titleOfETCProduct", etcProduct.titleOfETCProduct));
            dataToSend.add(new BasicNameValuePair("productDetailInform", etcProduct.productDetailInform));
            dataToSend.add(new BasicNameValuePair("productExpireDateOfPurchase", etcProduct.productExpireDateOfPurchase));
            dataToSend.add(new BasicNameValuePair("image", encodedImage));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam); // connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "RegisterETCProducts.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            etcProductCallback.done(null);
        }
    }

    public class FetchETCProductsListAsyncTask extends AsyncTask<Void, Void, ArrayList<ETCProductConstructor>> {

        GetETCProductCallback getETCProductCallback;

        public FetchETCProductsListAsyncTask(GetETCProductCallback getETCProductCallback) {
            this.getETCProductCallback = getETCProductCallback;
        }


        @Override
        protected ArrayList<ETCProductConstructor> doInBackground(Void... params) {

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchETCProducts.php");

            ArrayList<ETCProductConstructor> returnedProducts = new ArrayList<>();

            try {
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e(TAGLOG, "json result : " + result);
                JSONObject jsonObject = new JSONObject(result);
                products = new String[jsonObject.length()];

                if(jsonObject.length() != 0) {
                    String resultCode = jsonObject.getString("resultCode");
                    Log.e(TAGLOG, "resultCode : " + resultCode);

                    for(int i = 0 ; i < jsonObject.length()-1 ; i++ ) {
                        products[i] = jsonObject.getString(String.valueOf(i));
                        Log.e(TAGLOG, "products[" + i + "] : " + products[i]);
                        JSONObject productObject = new JSONObject(products[i]);

                        int productManagementCode = productObject.getInt("productManagementCode");
                        String uploadProductsDate = productObject.getString("uploadProductsDate");
                        int userManagementCode = productObject.getInt("userManagementCode");
                        String userID = productObject.getString("userID");
                        String titleOfETCProduct = productObject.getString("titleOfETCProduct");
                        String productDetailInform = productObject.getString("productDetailInform").replace("\\n","\n");
                        String productExpireDateOfPurchase = productObject.getString("productExpireDateOfPurchase");
                        String productImageURL = productObject.getString("productImageURL");

                        ETCProductConstructor etcProductConstructor = new ETCProductConstructor(uploadProductsDate, userID, titleOfETCProduct,
                                productDetailInform, productExpireDateOfPurchase, productImageURL, userManagementCode, productManagementCode);

                        returnedProducts.add(etcProductConstructor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedProducts;
        }

        @Override
        protected void onPostExecute(ArrayList<ETCProductConstructor> etcProductConstructors) {
            super.onPostExecute(etcProductConstructors);
            getETCProductCallback.done(etcProductConstructors);
        }
    }

    public class UpdateETCProductsListAsyncTask extends AsyncTask<Void, Void, Void> {

        ETCProductConstructor etcProduct;
        Bitmap bitmap;
        GetETCProductCallback getETCProductCallback;

        public UpdateETCProductsListAsyncTask(ETCProductConstructor etcProduct, Bitmap bitmap, GetETCProductCallback getETCProductCallback) {
            this.etcProduct = etcProduct;
            this.bitmap = bitmap;
            this.getETCProductCallback = getETCProductCallback;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("productManagementCode", String.valueOf(etcProduct.productManagementCode)));
            dataToSend.add(new BasicNameValuePair("titleOfETCProduct", etcProduct.titleOfETCProduct));
            dataToSend.add(new BasicNameValuePair("productDetailInform", etcProduct.productDetailInform));
            dataToSend.add(new BasicNameValuePair("uploadProductsDate", etcProduct.uploadProductsDate));
            dataToSend.add(new BasicNameValuePair("productExpireDateOfPurchase", etcProduct.productExpireDateOfPurchase));
            dataToSend.add(new BasicNameValuePair("imagePath", etcProduct.productImageURL));
            dataToSend.add(new BasicNameValuePair("image", encodedImage));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam); // connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateETCProducts.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            getETCProductCallback.done(null);
        }
    }

    public class DeleteETCProductsListAsyncTask extends AsyncTask<Void, Void, Void> {

        GetETCProductCallback getETCProductCallback;
        int productManagementCode;

        public DeleteETCProductsListAsyncTask(int productManagementCode, GetETCProductCallback getETCProductCallback) {
            this.getETCProductCallback = getETCProductCallback;
            this.productManagementCode = productManagementCode;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("productManagementCode", String.valueOf(productManagementCode)));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "DeleteETCProduct.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            getETCProductCallback.done(null);
        }
    }
}
