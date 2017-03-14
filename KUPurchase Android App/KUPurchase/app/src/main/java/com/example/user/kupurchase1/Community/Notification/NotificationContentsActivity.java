package com.example.user.kupurchase1.Community.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.kupurchase1.R;

public class NotificationContentsActivity extends AppCompatActivity {

    public TextView tvNotificationTitle, tvNotificationDate, tvNotificationContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_contents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        tvNotificationContents = (TextView) findViewById(R.id.tvNotificationContents);
        tvNotificationTitle = (TextView) findViewById(R.id.tvNotificationTitle);
        tvNotificationDate = (TextView) findViewById(R.id.tvNotificationDate);

        Intent intent = getIntent();
        tvNotificationTitle.setText(intent.getStringExtra("noticeTitle"));
        tvNotificationDate.setText(intent.getStringExtra("noticeDate"));
        tvNotificationContents.setText(intent.getStringExtra("noticeContents"));
    }

    public void BackMainCommunityActivity(View v) {
//        Intent intent = new Intent(this, MainCommunityActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, MainCommunityActivity.class);
//        startActivity(intent);
        finish();
    }
}
