package com.example.user.kupurchase1.Community.FreeBoard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kupurchase1.Community.MainCommunityActivity;
import com.example.user.kupurchase1.FullScreenActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.Setting.Message.SendMessageActivity;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FreeBoardContentsActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAGLOG = "로그: FreeBoardContentsActivity: ";

    public TextView tvFreeBoardTitle, tvFreeBoardUploadDate, tvFreeBoardContents, tvUserID, tvBackMainCommunityActivity, tvFreeBoardCount;
    public Button bUpdateFreeBoardDetail, bDeleteFreeBoardDetail, bSendMessage;
    public ImageView ivFreeBoardImage;
    public UserLocalStore userLocalStore;

    public String freeBoardTitle, freeBoardContents, freeBoardUploadDate, userID, freeBoardImageURL;
    public int freeBoardManagementCode, freeBoardCount;

    public static Activity freeBoardContentsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeboard_contents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        tvFreeBoardContents = (TextView) findViewById(R.id.tvFreeBoardContents);
        tvFreeBoardTitle = (TextView) findViewById(R.id.tvFreeBoardTitle);
        tvFreeBoardUploadDate = (TextView) findViewById(R.id.tvFreeBoardDate);
        tvBackMainCommunityActivity = (TextView) findViewById(R.id.tvBackMainCommunityActivity);
        tvUserID = (TextView) findViewById(R.id.tvUserID);
        tvFreeBoardCount = (TextView) findViewById(R.id.tvFreeBoardCount);
        bUpdateFreeBoardDetail = (Button) findViewById(R.id.bUpdateFreeBoardDetail);
        bDeleteFreeBoardDetail = (Button) findViewById(R.id.bDeleteFreeBoardDetail);
        bSendMessage = (Button) findViewById(R.id.bSendMessage);
        ivFreeBoardImage = (ImageView) findViewById(R.id.ivFreeBoardImage);

        bUpdateFreeBoardDetail.setOnClickListener(this);
        bDeleteFreeBoardDetail.setOnClickListener(this);
        tvBackMainCommunityActivity.setOnClickListener(this);
        bSendMessage.setOnClickListener(this);
        ivFreeBoardImage.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    protected void onResume() {
        freeBoardContentsActivity = FreeBoardContentsActivity.this;
        super.onResume();

        Intent intent = getIntent();
        freeBoardTitle = intent.getStringExtra("freeBoardTitle");
        freeBoardContents = intent.getStringExtra("freeBoardContents");
        freeBoardUploadDate = intent.getStringExtra("freeBoardUploadDate");
        userID = intent.getStringExtra("userID");
        freeBoardManagementCode = intent.getIntExtra("freeBoardManagementCode", -1);
        freeBoardCount = intent.getIntExtra("freeBoardCount", -1);
        freeBoardImageURL = intent.getStringExtra("freeBoardImageURL");

        tvFreeBoardTitle.setText(freeBoardTitle);
        tvFreeBoardContents.setText(freeBoardContents);
        tvFreeBoardUploadDate.setText(freeBoardUploadDate);
        tvUserID.setText(userID);
        tvFreeBoardCount.setText("조회수: " + freeBoardCount);

        authenticate();
    }

    private void authenticate() {
        User user = userLocalStore.getLoggedInUser();
        if(freeBoardImageURL.equals("ImageEmpty")) {
            ivFreeBoardImage.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(freeBoardImageURL)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .into(ivFreeBoardImage);
        }
        if(user.userID.equals(userID)){
            bUpdateFreeBoardDetail.setVisibility(View.VISIBLE);
            bDeleteFreeBoardDetail.setVisibility(View.VISIBLE);
            bSendMessage.setVisibility(View.INVISIBLE);
        } else {
            bUpdateFreeBoardDetail.setVisibility(View.INVISIBLE);
            bDeleteFreeBoardDetail.setVisibility(View.INVISIBLE);
            bSendMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bUpdateFreeBoardDetail: {
                Intent intent = new Intent(this, UpdateFreeBoardActivity.class);
                intent.putExtra("freeBoardTitle", freeBoardTitle);
                intent.putExtra("freeBoardContents", freeBoardContents);
                intent.putExtra("freeBoardManagementCode", freeBoardManagementCode);
                intent.putExtra("freeBoardImageURL", freeBoardImageURL);
                startActivity(intent);
                break;
            }
            case R.id.bDeleteFreeBoardDetail: {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("삭제 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FreeBoardServerRequests freeBoardServerRequests = new FreeBoardServerRequests(getApplicationContext());
                                freeBoardServerRequests.deleteFreeBoardListInBackground(freeBoardManagementCode, new GetFreeBoardCallback() {
                                    @Override
                                    public void done(ArrayList<FreeBoardConstructor> returnedFreeBoard) {
                                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                                        MainCommunityActivity mainCommunityActivity = (MainCommunityActivity) MainCommunityActivity.mainCommunityActivity;
                                        mainCommunityActivity.finish();

                                        Intent intent = new Intent(getApplicationContext(), MainCommunityActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }).setNegativeButton("취소", null);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                break;
            }
            case R.id.tvBackMainCommunityActivity: {
                finish();
                break;
            }

            case R.id.bSendMessage : {
                Intent intent = new Intent(this, SendMessageActivity.class);
                intent.putExtra("toUserID", userID);
                startActivity(intent);
                break;
            }

            case R.id.ivFreeBoardImage : {
                Intent intent = new Intent(this, FullScreenActivity.class);
                intent.putExtra("productImageURL", freeBoardImageURL);
                startActivity(intent);
            }
        }
    }
}
