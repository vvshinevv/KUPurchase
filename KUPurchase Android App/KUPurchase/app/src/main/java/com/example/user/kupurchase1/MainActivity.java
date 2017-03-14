package com.example.user.kupurchase1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.user.kupurchase1.Community.MainCommunityActivity;
import com.example.user.kupurchase1.Order.OrderActivity;
import com.example.user.kupurchase1.Purchase.MainPurchaseActivity;
import com.example.user.kupurchase1.Setting.SettingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView ivMainPurchase, ivMainCommunity, ivMainCheckPurchase, ivMainSetting, ivLogout;
    private BackPressCloseHandler backPressCloseHandler;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);
        userLocalStore = new UserLocalStore(this);

        ivMainPurchase = (ImageView) findViewById(R.id.ivMainPurchase);
        ivMainCommunity = (ImageView) findViewById(R.id.ivMainCommunity);
        ivMainCheckPurchase = (ImageView) findViewById(R.id.ivMainCheckPurchase);
        ivMainSetting = (ImageView) findViewById(R.id.ivMainSetting);
        ivLogout = (ImageView) findViewById(R.id.ivLogout);

        ivMainPurchase.setOnClickListener(this);
        ivMainCommunity.setOnClickListener(this);
        ivMainCheckPurchase.setOnClickListener(this);
        ivMainSetting.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMainPurchase:
                Intent mainPurchaseActivity = new Intent(this, MainPurchaseActivity.class);
                startActivity(mainPurchaseActivity);
                finish();
                break;

            case R.id.ivMainCommunity:
                Intent mainCommunityActivity = new Intent(this, MainCommunityActivity.class);
                startActivity(mainCommunityActivity);
                finish();
                break;

            case R.id.ivMainCheckPurchase:
                Intent orderActivity = new Intent(this, OrderActivity.class);
                startActivity(orderActivity);
                finish();
                break;

            case R.id.ivMainSetting:
                Intent mainSettingActivity = new Intent(this, SettingActivity.class);
                startActivity(mainSettingActivity);
                break;

            case R.id.ivLogout: {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
