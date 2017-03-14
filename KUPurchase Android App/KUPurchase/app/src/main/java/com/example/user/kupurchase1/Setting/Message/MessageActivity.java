package com.example.user.kupurchase1.Setting.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.kupurchase1.MainActivity;
import com.example.user.kupurchase1.R;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private String TAGLOG = "로그: MessageActivity: ";
    public Context context;
    public static Activity messageActivity;
    public MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = getApplicationContext();
        messageActivity = MessageActivity.this;

        initView();
    }

    private void initView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<Messages> messagesArrayList = new ArrayList<>();
        MessageServerRequests messageServerRequests = new MessageServerRequests(this);
        messageServerRequests.fetchMessageInBackground(new GetMessageCallback() {
            @Override
            public void done(ArrayList<MessageConstructor> returnedMessage) {
                for (int i = 0; i < returnedMessage.size(); i++) {
                    Messages messages = new Messages();
                    messages.setFromUserID(returnedMessage.get(i).fromUserID);
                    messages.setToUserID(returnedMessage.get(i).toUserID);
                    messages.setSendMessageTime(returnedMessage.get(i).sendMessageTime);
                    messages.setSendMessage(returnedMessage.get(i).sendMessage);
                    messagesArrayList.add(messages);
                }

                messageAdapter = new MessageAdapter(messagesArrayList, getApplicationContext());
                recyclerView.setAdapter(messageAdapter);
                recyclerView.scrollToPosition(messagesArrayList.size()-1); // scroll 맨 아래로
            }
        });
    }

    public void BackMainActivity(View v) {
        if(isTaskRoot()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(isTaskRoot()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }
}
