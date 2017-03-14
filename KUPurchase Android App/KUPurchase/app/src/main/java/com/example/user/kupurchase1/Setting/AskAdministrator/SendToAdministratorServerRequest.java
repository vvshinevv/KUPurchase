package com.example.user.kupurchase1.Setting.AskAdministrator;

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

import java.util.ArrayList;

/**
 * Created by user on 2016-03-21.
 */
public class SendToAdministratorServerRequest {

    private String TAGLOG = "로그: SendToAdministratorServerRequest: ";

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://52.79.93.255/KUPurchase/";

    public Context context;
    public ProgressDialog progressDialog;

    public SendToAdministratorServerRequest(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("메세지 전송 중입니다.");
    }

    public void sendToAdministratorInBackground(AskAdminConstructor askAdminConstructor, GetAdminDataCallback getAdminDataCallback) {
        progressDialog.show();
        new SendToAdministratorAsyncTask(askAdminConstructor, getAdminDataCallback).execute();

    }

    public class SendToAdministratorAsyncTask extends AsyncTask<Void, Void, String> {

        AskAdminConstructor askAdminConstructor;
        GetAdminDataCallback getAdminDataCallback;

        public SendToAdministratorAsyncTask(AskAdminConstructor askAdminConstructor, GetAdminDataCallback getAdminDataCallback){
            this.askAdminConstructor = askAdminConstructor;
            this.getAdminDataCallback = getAdminDataCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("askTitle", askAdminConstructor.askTitle));
            dataToSend.add(new BasicNameValuePair("askContents", askAdminConstructor.askContents));
            dataToSend.add(new BasicNameValuePair("fromUserID", askAdminConstructor.fromUserID));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam); // connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "RegisterAskAdministor.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            getAdminDataCallback.done(null);
        }
    }

}
