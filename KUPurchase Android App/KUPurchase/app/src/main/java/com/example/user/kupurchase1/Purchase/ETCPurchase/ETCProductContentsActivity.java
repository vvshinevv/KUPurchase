package com.example.user.kupurchase1.Purchase.ETCPurchase;

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

import com.example.user.kupurchase1.FullScreenActivity;
import com.example.user.kupurchase1.Purchase.MainPurchaseActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.Setting.Message.SendMessageActivity;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ETCProductContentsActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: ETCProductContentsActivity: ";

    public TextView tvETCProductTitle, tvETCProductDate, tvETCProductContents, tvUserID, tvETCProductUploadDate, tvBackMainPurchaseActivity;
    public ImageView ivETCProductPicture;
    public Button bSendMessage, bUpdateProductDetail, bDeleteProductDetail;
    public UserLocalStore userLocalStore;

    public String userID, imagePath, titleOfETCProduct, productExpireDateOfPurchase, productDetailInform, uploadProductsDate;
    public int productManagementCode, userManagementCode;

    public static Activity etcProductContentsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etcproduct_contents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvETCProductTitle = (TextView) findViewById(R.id.tvETCProductTitle);
        tvETCProductDate = (TextView) findViewById(R.id.tvETCProductDate);
        tvETCProductContents = (TextView) findViewById(R.id.tvETCProductContents);
        tvUserID = (TextView) findViewById(R.id.tvUserID);
        tvETCProductUploadDate = (TextView) findViewById(R.id.tvETCProductUploadDate);
        tvBackMainPurchaseActivity = (TextView) findViewById(R.id.tvBackMainPurchaseActivity);

        ivETCProductPicture = (ImageView) findViewById(R.id.ivETCProductPicture);
        bSendMessage = (Button) findViewById(R.id.bSendMessage);
        bUpdateProductDetail = (Button) findViewById(R.id.bUpdateProductDetail);
        bDeleteProductDetail = (Button) findViewById(R.id.bDeleteProductDetail);

        bSendMessage.setOnClickListener(this);
        bUpdateProductDetail.setOnClickListener(this);
        bDeleteProductDetail.setOnClickListener(this);
        tvBackMainPurchaseActivity.setOnClickListener(this);
        ivETCProductPicture.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etcProductContentsActivity = ETCProductContentsActivity.this;
        Intent intent = getIntent();

        userID = intent.getStringExtra("userID");
        imagePath = intent.getStringExtra("productImageURL");
        titleOfETCProduct = intent.getStringExtra("titleOfETCProduct");
        productExpireDateOfPurchase = intent.getStringExtra("productExpireDateOfPurchase");
        productDetailInform = intent.getStringExtra("productDetailInform");
        uploadProductsDate = intent.getStringExtra("uploadProductsDate");
        productManagementCode = intent.getIntExtra("productManagementCode", -1);
        userManagementCode = intent.getIntExtra("userManagementCode", -1);

        tvETCProductTitle.setText(titleOfETCProduct);
        tvETCProductDate.setText(productExpireDateOfPurchase);
        tvETCProductContents.setText(productDetailInform);
        Picasso.with(this).load(intent.getStringExtra("productImageURL")).into(ivETCProductPicture);
        tvUserID.setText(userID);
        tvETCProductUploadDate.setText(uploadProductsDate);

        authenticate();
    }

    private void authenticate() {
        User user = userLocalStore.getLoggedInUser();
        if(user.userID.equals(userID)){
            bSendMessage.setVisibility(View.GONE);
        } else {
            bUpdateProductDetail.setVisibility(View.GONE);
            bDeleteProductDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSendMessage: {
                Intent intent = new Intent(this, SendMessageActivity.class);
                intent.putExtra("toUserID", userID);
                startActivity(intent);
                break;
            }

            case R.id.bUpdateProductDetail: {
                Intent intent = new Intent(this, UpdateETCProductActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("imagePath", imagePath);
                intent.putExtra("titleOfETCProduct", titleOfETCProduct);
                intent.putExtra("productExpireDateOfPurchase", productExpireDateOfPurchase);
                intent.putExtra("productDetailInform", productDetailInform);
                intent.putExtra("uploadProductsDate", uploadProductsDate);
                intent.putExtra("productManagementCode", productManagementCode);
                intent.putExtra("userManagementCode", userManagementCode);
                startActivity(intent);
                break;
            }

            case R.id.bDeleteProductDetail: {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("삭제 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteETCProduct(productManagementCode);
                            }
                        }).setNegativeButton("취소", null);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                break;
            }

            case R.id.tvBackMainPurchaseActivity:
                finish();
                break;

            case R.id.ivETCProductPicture: {
                Intent intent = new Intent(this, FullScreenActivity.class);
                intent.putExtra("productImageURL", imagePath);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void deleteETCProduct(int productManagementCode) {
        ETCProductServerRequests etcProductServerRequests = new ETCProductServerRequests(this);
        etcProductServerRequests.deleteETCProductsListInBackground(productManagementCode, new GetETCProductCallback() {
            @Override
            public void done(ArrayList<ETCProductConstructor> returnedETCProduct) {
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                MainPurchaseActivity mainPurchaseActivity = (MainPurchaseActivity)MainPurchaseActivity.mainPurchaseActivity;
                mainPurchaseActivity.finish();

                Intent intent = new Intent(getApplicationContext(), MainPurchaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
