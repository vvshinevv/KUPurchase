package com.example.user.kupurchase1.Community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.kupurchase1.MainActivity;
import com.example.user.kupurchase1.R;

public class MainCommunityActivity extends AppCompatActivity {
    public static Activity mainCommunityActivity;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mainCommunityActivity=MainCommunityActivity.this;
        setContentView(R.layout.activity_main_community);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlTab_Layout);
        tabLayout.addTab(tabLayout.newTab().setText("공지사항"));
        tabLayout.addTab(tabLayout.newTab().setText("자유 게시판"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vpView_Pager);
        final MainCommunityAdapter mainCommunityAdapter = new MainCommunityAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(mainCommunityAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void BackMainActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
