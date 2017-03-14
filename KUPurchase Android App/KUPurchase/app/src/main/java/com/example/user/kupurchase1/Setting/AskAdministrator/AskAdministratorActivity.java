package com.example.user.kupurchase1.Setting.AskAdministrator;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.Setting.SettingActivity;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

public class AskAdministratorActivity extends AppCompatActivity {

    private EditText etAskTitle, etAskContents;
    public UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_administrator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        userLocalStore = new UserLocalStore(this);
        etAskTitle = (EditText) findViewById(R.id.etAskTitle);
        etAskContents = (EditText) findViewById(R.id.etAskContents);
    }

    public void SendMessage(View v) {
        if((etAskTitle.getText().toString()).equals("") || (etAskContents.getText().toString()).equals("")) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("내용을 적어주세요.");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            String askTitle = etAskTitle.getText().toString();
            String askContents = etAskContents.getText().toString();

            User user = userLocalStore.getLoggedInUser();

            AskAdminConstructor askAdminConstructor = new AskAdminConstructor(askTitle, askContents, user.userID);
            SendToAdministratorServerRequest sendToAdministratorServerRequest = new SendToAdministratorServerRequest(this);
            sendToAdministratorServerRequest.sendToAdministratorInBackground(askAdminConstructor, new GetAdminDataCallback() {
                @Override
                public void done(String returnedMessage) {
                    Toast.makeText(getApplicationContext(), "감사합니다. 전송이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void BackSettingActivity(View v) {
        finish();
    }

    public void CallToAdministrator(View v) {
        Uri uri= Uri.parse("tel:01026325301");
        Intent intent= new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    }
}
