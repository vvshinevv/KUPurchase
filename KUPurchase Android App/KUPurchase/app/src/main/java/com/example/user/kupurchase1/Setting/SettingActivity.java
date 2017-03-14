package com.example.user.kupurchase1.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.Setting.AskAdministrator.AskAdministratorActivity;
import com.example.user.kupurchase1.Setting.Message.MessageActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    public TextView tvAskManager, tvManageMessage, tvUpdateUser, tvBackMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        tvAskManager = (TextView) findViewById(R.id.tvAskManager);
        tvManageMessage = (TextView) findViewById(R.id.tvManageMessage);
        tvUpdateUser = (TextView) findViewById(R.id.tvUpdateUser);
        tvBackMainActivity = (TextView) findViewById(R.id.tvBackMainActivity);

        tvAskManager.setOnClickListener(this);
        tvManageMessage.setOnClickListener(this);
        tvUpdateUser.setOnClickListener(this);
        tvBackMainActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAskManager: {
                Intent intent = new Intent(this, AskAdministratorActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tvManageMessage: {
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tvUpdateUser:
                break;

            case R.id.tvBackMainActivity:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
