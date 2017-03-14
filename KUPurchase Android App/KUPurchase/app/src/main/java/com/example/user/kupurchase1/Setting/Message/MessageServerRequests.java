package com.example.user.kupurchase1.Setting.Message;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

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
 * Created by user on 2016-03-17.
 */
public class MessageServerRequests {

    private String TAGLOG = "로그: MessageServerRequests: ";

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://52.79.93.255/KUPurchase/";

    public UserLocalStore userLocalStore;
    public Context context;
    public String[] messages;
    public ProgressDialog progressDialog;


    public MessageServerRequests(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        userLocalStore = new UserLocalStore(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("메세지 전송 중입니다.");
    }

    public void sendMessageInBackground(MessageConstructor message, GetMessageCallback getMessageCallback) {
        progressDialog.show();
        new SendMessageAsyncTask(message, getMessageCallback).execute();
    }

    public void fetchMessageInBackground(GetMessageCallback getMessageCallback) {
        new FetchMessageAsyncTask(getMessageCallback).execute();
    }

    public class SendMessageAsyncTask extends AsyncTask<Void, Void, Void>{

        GetMessageCallback getMessageCallback;
        MessageConstructor message;
        User user;

        public SendMessageAsyncTask(MessageConstructor message, GetMessageCallback getMessageCallback) {
            this.getMessageCallback = getMessageCallback;
            this.message = message;
            user = userLocalStore.getLoggedInUser();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("toUserID", message.toUserID));
            dataToSend.add(new BasicNameValuePair("fromUserID", user.userID));
            dataToSend.add(new BasicNameValuePair("sendMessage", message.sendMessage));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam); // connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SendMessage.php");

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
            getMessageCallback.done(null);
        }
    }

    public class FetchMessageAsyncTask extends AsyncTask<Void, Void, ArrayList<MessageConstructor>>{

        GetMessageCallback getMessageCallback;
        User user;

        public FetchMessageAsyncTask(GetMessageCallback getMessageCallback) {
            this.getMessageCallback = getMessageCallback;
            user = userLocalStore.getLoggedInUser();
        }
        @Override
        protected ArrayList<MessageConstructor> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userID", user.userID));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchMessage.php");

            ArrayList<MessageConstructor> returnedMessages = new ArrayList<>();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend, HTTP.UTF_8));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e(TAGLOG, "json result : " + result);
                JSONObject jsonObject = new JSONObject(result);

                messages = new String[jsonObject.length()];

                if(jsonObject.length() != 0) {
                    String resultCode = jsonObject.getString("resultCode");
                    Log.e(TAGLOG, "resultCode : " + resultCode);

                    for(int i = 0 ; i < jsonObject.length()-1 ; i++) {
                        messages[i] = jsonObject.getString(String.valueOf(i));
                        Log.e(TAGLOG, "messages[" + i + "] : " + messages[i]);

                        JSONObject messageObject = new JSONObject(messages[i]);

                        String toUserID = messageObject.getString("toUserID");
                        String fromUserID = messageObject.getString("fromUserID");
                        String sendTime = messageObject.getString("sendTime");
                        String message = messageObject.getString("message").replace("\\n", "\n");

                        MessageConstructor messageConstructor = new MessageConstructor(toUserID, fromUserID, sendTime, message);
                        returnedMessages.add(messageConstructor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedMessages;
        }

        @Override
        protected void onPostExecute(ArrayList<MessageConstructor> messageConstructors) {
            super.onPostExecute(messageConstructors);
            getMessageCallback.done(messageConstructors);
        }
    }
}
