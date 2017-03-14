package com.example.user.kupurchase1.Community.FreeBoard;

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
 * Created by user on 2016-03-11.
 */
public class FreeBoardServerRequests {

    private String TAGLOG = "로그: FreeBoardServerRequests: ";

    public Context context;

    public ProgressDialog progressDialog;
    public String freeBoards[];

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://52.79.93.255/KUPurchase/";

    public FreeBoardServerRequests(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("잠시만 기다려주세요...");
    }

    public void fetchFreeBoardInBackground(GetFreeBoardCallback getFreeBoardCallback) {
        new FetchFreeBoardAsyncTask(getFreeBoardCallback).execute();
    }

    public void storeFreeBoardListInBackground(Bitmap image, FreeBoardConstructor freeBoard, GetFreeBoardCallback freeBoardCallback) {
        progressDialog.show();
        new StoreFreeBoardListAsyncTask(image, freeBoard, freeBoardCallback).execute();
    }

    public void updateFreeBoardListInBackground(Bitmap image, FreeBoardConstructor freeBoardConstructor, GetFreeBoardCallback getFreeBoardCallback) {
        progressDialog.show();
        new UpdateFreeBoardListAsyncTask(image, freeBoardConstructor, getFreeBoardCallback).execute();
    }

    public void deleteFreeBoardListInBackground(int freeBoardManagementCode, GetFreeBoardCallback getFreeBoardCallback) {
        new DeleteFreeBoardListAsyncTask(freeBoardManagementCode, getFreeBoardCallback).execute();

    }

    public void updateFreeBoardCountInBackground(int freeBoardManagementCode) {
        new UpdateFreeBoardCountAsyncTask(freeBoardManagementCode).execute();
    }

    public class StoreFreeBoardListAsyncTask extends AsyncTask<Void, Void, Void> {

        FreeBoardConstructor freeBoard;
        GetFreeBoardCallback freeBoardCallback;
        Bitmap image;

        public StoreFreeBoardListAsyncTask(Bitmap image, FreeBoardConstructor freeBoard, GetFreeBoardCallback freeBoardCallback) {
            this.freeBoard = freeBoard;
            this.freeBoardCallback = freeBoardCallback;
            this.image = image;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userID", freeBoard.userID));
            dataToSend.add(new BasicNameValuePair("freeBoardTitle", freeBoard.freeBoardTitle));
            dataToSend.add(new BasicNameValuePair("freeBoardContents", freeBoard.freeBoardContents));
            dataToSend.add(new BasicNameValuePair("isImageEmpty", freeBoard.isImageEmpty));
            dataToSend.add(new BasicNameValuePair("image", encodedImage));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam); // connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "RegisterFreeBoard.php");

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
            freeBoardCallback.done(null);
        }
    }

    public class FetchFreeBoardAsyncTask extends AsyncTask<Void, Void, ArrayList<FreeBoardConstructor>> {

        GetFreeBoardCallback getFreeBoardCallback;

        public FetchFreeBoardAsyncTask(GetFreeBoardCallback getFreeBoardCallback) {
            this.getFreeBoardCallback = getFreeBoardCallback;
        }

        @Override
        protected ArrayList<FreeBoardConstructor> doInBackground(Void... params) {
            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchFreeBoard.php");

            ArrayList<FreeBoardConstructor> returnedFreeBoards = new ArrayList<>();

            try {
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e(TAGLOG, "json result : " + result);
                JSONObject jsonObject = new JSONObject(result);

                freeBoards = new String[jsonObject.length()];

                if(jsonObject.length() != 0) {
                    String resultCode = jsonObject.getString("resultCode");
                    Log.e(TAGLOG, "resultCode : " + resultCode);

                    for(int i = 0 ; i < jsonObject.length()-1 ; i++) {
                        freeBoards[i] = jsonObject.getString(String.valueOf(i));
                        Log.e(TAGLOG, "freeBoards[" + i + "] : " + freeBoards[i]);

                        JSONObject freeBoardObject = new JSONObject(freeBoards[i]);

                        int freeBoardManagementCode = freeBoardObject.getInt("freeBoardManagementCode");
                        String freeBoardTitle = freeBoardObject.getString("freeBoardTitle");
                        String freeBoardContents = freeBoardObject.getString("freeBoardContents").replace("\\n", "\n");
                        String freeBoardUploadDate = freeBoardObject.getString("freeBoardUploadDate");
                        String userID=freeBoardObject.getString("userID");
                        int freeBoardCount = freeBoardObject.getInt("freeBoardCount");
                        String freeBoardImageURL = freeBoardObject.getString("freeBoardImageURL");

                        FreeBoardConstructor freeBoardConstructor = new FreeBoardConstructor(freeBoardTitle, freeBoardContents,
                                freeBoardUploadDate, userID, freeBoardManagementCode, freeBoardCount, freeBoardImageURL);

                        returnedFreeBoards.add(freeBoardConstructor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedFreeBoards;
        }

        @Override
        protected void onPostExecute(ArrayList<FreeBoardConstructor> freeBoardConstructors) {
            super.onPostExecute(freeBoardConstructors);
            getFreeBoardCallback.done(freeBoardConstructors);
        }
    }

    public class UpdateFreeBoardListAsyncTask extends AsyncTask<Void, Void, Void> {

        FreeBoardConstructor freeBoard;
        GetFreeBoardCallback getFreeBoardCallback;
        Bitmap image;

        public UpdateFreeBoardListAsyncTask(Bitmap image, FreeBoardConstructor freeBoard, GetFreeBoardCallback getFreeBoardCallback) {
            this.freeBoard = freeBoard;
            this.getFreeBoardCallback = getFreeBoardCallback;
            this.image = image;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("freeBoardTitle", freeBoard.freeBoardTitle));
            dataToSend.add(new BasicNameValuePair("freeBoardContents", freeBoard.freeBoardContents));
            dataToSend.add(new BasicNameValuePair("freeBoardManagementCode", String.valueOf(freeBoard.freeBoardManagementCode)));
            dataToSend.add(new BasicNameValuePair("freeBoardImageURL", freeBoard.freeBoardImageURL));
            dataToSend.add(new BasicNameValuePair("isImageEmpty", freeBoard.isImageEmpty));

            Log.e(TAGLOG, "isImageEmpty : " + freeBoard.isImageEmpty);
            dataToSend.add(new BasicNameValuePair("userID", freeBoard.userID));
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            Log.e(TAGLOG, "encodedImage : " + encodedImage);


            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam); // connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateFreeBoard.php");

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
            getFreeBoardCallback.done(null);
        }
    }

    public class DeleteFreeBoardListAsyncTask extends AsyncTask<Void, Void, Void> {

        GetFreeBoardCallback getFreeBoardCallback;
        int freeBoardManagementCode;

        public DeleteFreeBoardListAsyncTask(int freeBoardManagementCode, GetFreeBoardCallback getFreeBoardCallback) {
            this.getFreeBoardCallback = getFreeBoardCallback;
            this.freeBoardManagementCode = freeBoardManagementCode;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("freeBoardManagementCode", String.valueOf(freeBoardManagementCode)));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "DeleteFreeBoard.php");

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
            getFreeBoardCallback.done(null);
        }
    }

    public class UpdateFreeBoardCountAsyncTask extends AsyncTask<Void, Void, Void> {

        int freeBoardManagementCode;

        public UpdateFreeBoardCountAsyncTask(int freeBoardManagementCode) {
            this.freeBoardManagementCode = freeBoardManagementCode;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("freeBoardManagementCode", String.valueOf(freeBoardManagementCode)));

            HttpParams httpRequestParam = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParam, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParam, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParam);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FreeBoardCount.php");

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
        }
    }
}
