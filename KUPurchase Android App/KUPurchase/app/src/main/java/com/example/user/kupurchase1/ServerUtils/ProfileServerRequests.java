package com.example.user.kupurchase1.ServerUtils;

import android.app.ProgressDialog;
import android.content.Context;
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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-08.
 */
public class ProfileServerRequests {

    private String TAGLOG = "로그: ProfileServerRequests: ";

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://172.30.1.49/KUPurchase/";

    public ProfileServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("잠시만 기다려주세요...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback getUserCallback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, getUserCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback getUserCallback) {
        progressDialog.show();
        new FetchUserDataAsyncTask(user, getUserCallback).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {

        User user;
        GetUserCallback getUserCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback getUserCallback) {
            this.user = user;
            this.getUserCallback = getUserCallback;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userID", user.userID));
            dataToSend.add(new BasicNameValuePair("userPW", user.userPW));
            dataToSend.add(new BasicNameValuePair("userName", user.userName));
            dataToSend.add(new BasicNameValuePair("userPhoneNumber", user.userPhoneNum));
            dataToSend.add(new BasicNameValuePair("userMailAddress", user.userMailAddress));
            dataToSend.add(new BasicNameValuePair("userToken", user.userToken));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "RegisterUser.php");

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
            getUserCallback.done(null);
        }
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallback getUserCallback;

        public FetchUserDataAsyncTask(User user, GetUserCallback getUserCallback) {
            this.user = user;
            this.getUserCallback = getUserCallback;
        }
        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userID", user.userID));
            dataToSend.add(new BasicNameValuePair("userPW", user.userPW));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserInformation.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.w(TAGLOG, "json result : " + result);
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() != 0) {
                    String userName = jsonObject.getString("userName");
                    String userPhoneNumber = jsonObject.getString("userPhoneNumber");
                    String userMailAddress = jsonObject.getString("userMailAddress");
                    int userManagementCode = jsonObject.getInt("userManagementCode");
                    Log.e(TAGLOG, "userManagementCode : " + userManagementCode);

                    returnedUser = new User(user.userID, user.userPW, userName, userPhoneNumber, userMailAddress, userManagementCode);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            getUserCallback.done(returnedUser);
        }
    }
}
