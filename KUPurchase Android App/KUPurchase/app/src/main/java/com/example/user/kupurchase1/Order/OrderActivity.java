package com.example.user.kupurchase1.Order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kupurchase1.MainActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private String TAGLOG = "로그 : OrderActivity : ";
    RecyclerView recyclerView;
    ArrayList<OrderKuProducts> orderKuProductsArrayList;
    OrderServerRequests orderServerRequests;
    OrderAdapter orderAdapter;
    public TextView tvTotalPrice, tvTotalDepositState;
    public int totalPrice=0;
    public boolean totalDeposit = true;
    public boolean tempDeposit = true;
    public ImageView trash;
    public static Activity orderActivity;

    public UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderActivity = this;
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();


        tvTotalPrice = (TextView)findViewById(R.id.tvOrderTotalPrice);
        tvTotalDepositState = (TextView)findViewById(R.id.tvOrderTotalDepositState);

        trash = (ImageView) findViewById(R.id.ivTrashIcon);

        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        orderKuProductsArrayList = new ArrayList<>();
        orderServerRequests = new OrderServerRequests(user.userManagementCode);
        orderServerRequests.fetchOrderKuProductInBackground(new GetOrderProductCallback() {
            @Override
            public void done(ArrayList<OrderConstructor> returnedOrders) {
                for(int i=0; i<returnedOrders.size(); i++){
                    OrderKuProducts orderProducts = new OrderKuProducts();
                    orderProducts.setKuProductName(returnedOrders.get(i).kuProductName);
                    orderProducts.setKuProductCount(returnedOrders.get(i).kuProductCount);
                    orderProducts.setKuProductPrice(returnedOrders.get(i).kuProductPrice);
                    orderProducts.setKuProductDepositState(returnedOrders.get(i).kuProductDepositState);
                    orderProducts.setAutoIncrement(returnedOrders.get(i).autoIncrement);
                    totalPrice += returnedOrders.get(i).kuProductCount*returnedOrders.get(i).kuProductPrice;
                    tempDeposit = (returnedOrders.get(i).kuProductDepositState != 0);
                    totalDeposit=  (tempDeposit && totalDeposit);
                    orderKuProductsArrayList.add(orderProducts);

                }
                tvTotalPrice.setText("총 금액 : " + totalPrice + " 원");
                if (totalDeposit){
                    tvTotalDepositState.setText("입금상태 : 완료");
                }else{
                    tvTotalDepositState.setText("입금상태 : 미완료");
                }
                orderAdapter = new OrderAdapter(orderKuProductsArrayList, getApplicationContext());
                recyclerView.setAdapter(orderAdapter);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent backMainActivity = new Intent(this, MainActivity.class);
        startActivity(backMainActivity);
        finish();}

    public void BackMainActivity(View v){
        Intent backMainActivity = new Intent(this, MainActivity.class);
        startActivity(backMainActivity);
        finish();
    }

    public void OpenActivity() {
        Intent orderActiviy = new Intent(this, OrderActivity.class);
        startActivity(orderActiviy);
    }

}
