package com.example.user.kupurchase1.Purchase.KUPurchase;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-09.
 */
public class KuProductsServerRequests {

    private String TAGLOG = "로그: KuProductsServerRequests: ";

    public String products[];

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://172.30.1.49/KUPurchase/";

    public void fetchKuProductsListInBackground(GetKuProductCallback getKuProductCallback) {
        new FetchKuProductsListAsyncTask(getKuProductCallback).execute();
    }

    public class FetchKuProductsListAsyncTask extends AsyncTask<Void, Void, ArrayList<KuProductConstructor>> {

        GetKuProductCallback getKuProductCallback;

        public FetchKuProductsListAsyncTask(GetKuProductCallback getKuProductCallback) {
            this.getKuProductCallback = getKuProductCallback;
        }

        @Override
        protected ArrayList<KuProductConstructor> doInBackground(Void... params) {
            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchKuProductList.php");

            ArrayList<KuProductConstructor> returnedProducts = new ArrayList<>();

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
                    for(int i = 0 ; i < jsonObject.length()-1 ; i++ ){
                        products[i] = jsonObject.getString(String.valueOf(i));
                        Log.e(TAGLOG, "products[" + i + "] : " + products[i]);
                        JSONObject productObject = new JSONObject(products[i]);

                        int productManagementCode = productObject.getInt("productManagementCode");
                        String productName = productObject.getString("productName");
                        int productPrice = productObject.getInt("productPrice");
                        String productExpireDateOfPurchase = productObject.getString("productExpireDateOfPurchase");
                        String productDepositStartDateOfPurchase = productObject.getString("productDepositStartDateOfPurchase");
                        String productDepositDueDateOfPurchase = productObject.getString("productDepositDueDateOfPurchase");
                        String productPictureUrlAddress = productObject.getString("productPictureUrlAddress");
                        KuProductConstructor kuProductConstructor = new KuProductConstructor(productManagementCode, productPictureUrlAddress, productName, productPrice,
                                productExpireDateOfPurchase, productDepositStartDateOfPurchase, productDepositDueDateOfPurchase);

                        returnedProducts.add(kuProductConstructor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedProducts;
        }

        @Override
        protected void onPostExecute(ArrayList<KuProductConstructor> kuProductConstructors) {
            super.onPostExecute(kuProductConstructors);
            getKuProductCallback.done(kuProductConstructors);
        }
    }

}
