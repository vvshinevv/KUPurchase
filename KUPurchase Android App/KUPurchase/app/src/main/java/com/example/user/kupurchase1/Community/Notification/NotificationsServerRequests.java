package com.example.user.kupurchase1.Community.Notification;

import android.content.Context;
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
 * Created by user on 2016-03-11.
 */
public class NotificationsServerRequests {
    private String TAGLOG = "로그: NotificationsServerRequests: ";
    public Context context;
    public String[] notifications;

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://172.30.1.49/KUPurchase/";

    public NotificationsServerRequests(Context context) {
        this.context = context;
    }

    public void fetchNotificationInBackground(GetNotificationCallback getNotificationCallback) {
        new FetchNotificationAsyncTask(getNotificationCallback).execute();
    }

    public class FetchNotificationAsyncTask extends AsyncTask<Void, Void, ArrayList<NotificationConstructor>> {

        GetNotificationCallback getNotificationCallback;

        public FetchNotificationAsyncTask(GetNotificationCallback getNotificationCallback) {
            this.getNotificationCallback = getNotificationCallback;
        }

        @Override
        protected ArrayList<NotificationConstructor> doInBackground(Void... params) {
            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchNotification.php");

            ArrayList<NotificationConstructor> returnedNotifications = new ArrayList<>();

            try {
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e(TAGLOG, "json result : " + result);
                JSONObject jsonObject = new JSONObject(result);

                notifications = new String[jsonObject.length()];

                if(jsonObject.length() != 0) {
                    String resultCode = jsonObject.getString("resultCode");
                    Log.e(TAGLOG, "resultCode : " + resultCode);

                    for(int i = 0 ; i < jsonObject.length()-1 ; i++) {
                        notifications[i] = jsonObject.getString(String.valueOf(i));
                        Log.e(TAGLOG, "notifications[" + i + "] : " + notifications[i]);

                        JSONObject notificationObject = new JSONObject(notifications[i]);

                        String noticeOfTitle = notificationObject.getString("noticeOfTitle");
                        String noticeOfContents = notificationObject.getString("noticeOfContents").replace("\\n","\n");
                        String noticeOfUploadDate = notificationObject.getString("noticeOfUploadDate");

                        NotificationConstructor notificationConstructor = new NotificationConstructor(noticeOfTitle, noticeOfContents, noticeOfUploadDate);

                        returnedNotifications.add(notificationConstructor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedNotifications;
        }

        @Override
        protected void onPostExecute(ArrayList<NotificationConstructor> notificationConstructors) {
            super.onPostExecute(notificationConstructors);
            getNotificationCallback.done(notificationConstructors);
        }
    }
}
