package com.example.user.kupurchase1.Setting.Message;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kupurchase1.R;

import java.util.ArrayList;

public class SendMessageActivity extends AppCompatActivity {

    private String TAGLOG = "로그: SendMessageActivity: ";

    public TextView tvToUserID;
    public EditText etMessageBox;
    public Button bSendMessage;
    public String toUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvToUserID = (TextView) findViewById(R.id.tvToUserID);
        etMessageBox = (EditText) findViewById(R.id.etMessageBox);
        bSendMessage = (Button) findViewById(R.id.bSendMessage);

        Intent intent = getIntent();
        toUserID = intent.getStringExtra("toUserID");
        tvToUserID.setText(toUserID);
    }

    public void SendMessage(View v) {
        if(!etMessageBox.getText().toString().isEmpty()) {
            String sendMessage = etMessageBox.getText().toString();
            MessageConstructor messageConstructor = new MessageConstructor(toUserID, sendMessage);
            MessageServerRequests messageServerRequests = new MessageServerRequests(this);
            messageServerRequests.sendMessageInBackground(messageConstructor, new GetMessageCallback() {
                @Override
                public void done(ArrayList<MessageConstructor> returnedMessage) {
                    Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SendMessageActivity.this, MessageActivity.class);
                    startActivity(intent);

                    MessageActivity messageActivity = (MessageActivity)MessageActivity.messageActivity;
                    //일딴 임시방편
                    try {
                        messageActivity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    finish();
                }
            });
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("내용을 입력하세요.");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        }
    }

    public void BackBeforeActivity(View v) {

        this.finish();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
