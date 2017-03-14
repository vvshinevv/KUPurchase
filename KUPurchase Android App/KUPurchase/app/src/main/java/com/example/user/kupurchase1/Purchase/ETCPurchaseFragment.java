package com.example.user.kupurchase1.Purchase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.kupurchase1.OnLoadMoreListener;
import com.example.user.kupurchase1.Purchase.ETCPurchase.ETCProductConstructor;
import com.example.user.kupurchase1.Purchase.ETCPurchase.ETCProductContentsActivity;
import com.example.user.kupurchase1.Purchase.ETCPurchase.ETCProductServerRequests;
import com.example.user.kupurchase1.Purchase.ETCPurchase.ETCProducts;
import com.example.user.kupurchase1.Purchase.ETCPurchase.ETCPurchaseAdapter;
import com.example.user.kupurchase1.Purchase.ETCPurchase.GetETCProductCallback;
import com.example.user.kupurchase1.Purchase.ETCPurchase.RegisterETCProductActivity;
import com.example.user.kupurchase1.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-09.
 */
public class ETCPurchaseFragment extends Fragment{

    private String TAGLOG = "로그: ETCPurchaseFragment: ";

    public View view;
    public Context context;

    protected Handler handler;

    public ArrayList<ETCProductConstructor> etcProductConstructorArrayList;

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_etc_purchase_fragment, container, false);
        context = view.getContext();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterETCProductActivity.class);
                startActivity(intent);
                ((MainPurchaseActivity) getActivity()).finish();
            }
        });

        initView();
        return view;
    }

    private void initView() {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        handler = new Handler();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<ETCProducts> etcProductsArrayList = new ArrayList<>();

        ETCProductServerRequests etcProductServerRequests = new ETCProductServerRequests();

        etcProductServerRequests.fetchETCProductsListInBackground(new GetETCProductCallback() {
            @Override
            public void done(ArrayList<ETCProductConstructor> returnedETCProduct) {
                etcProductConstructorArrayList = returnedETCProduct; //16

                for (int i = 0 ; i > returnedETCProduct.size() ; i++) {
                    ETCProducts etcProducts = new ETCProducts();
                    etcProducts.setUploadProductsDate(returnedETCProduct.get(i).uploadProductsDate);
                    etcProducts.setUserID(returnedETCProduct.get(i).userID);
                    etcProducts.setTitleOfETCProduct(returnedETCProduct.get(i).titleOfETCProduct);
                    etcProducts.setProductDetailInform(returnedETCProduct.get(i).productDetailInform);
                    etcProducts.setProductExpireDateOfPurchase(returnedETCProduct.get(i).productExpireDateOfPurchase);
                    etcProducts.setProductImageURL(returnedETCProduct.get(i).productImageURL);
                    etcProducts.setUserManagementCode(returnedETCProduct.get(i).userManagementCode);
                    etcProducts.setProductManagementCode(returnedETCProduct.get(i).productManagementCode);
                    etcProductsArrayList.add(etcProducts);

                }
                Log.e(TAGLOG, "etcProductsArrayList.size() : " + etcProductsArrayList.size());
                final ETCPurchaseAdapter etcPurchaseAdapter = new ETCPurchaseAdapter(etcProductsArrayList, context.getApplicationContext(), recyclerView);
                recyclerView.setAdapter(etcPurchaseAdapter);

                etcPurchaseAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        etcProductsArrayList.add(null);
                        etcPurchaseAdapter.notifyItemInserted(etcProductsArrayList.size() - 1);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                etcProductsArrayList.remove(etcProductsArrayList.size() - 1);
                                etcPurchaseAdapter.notifyItemRemoved(etcProductsArrayList.size());

                                int start = etcProductConstructorArrayList.size()-etcProductsArrayList.size()-1;
                                Log.e(TAGLOG, "Start : " + start);
                                int end = (start-5 < 0) ? 0 : start-5;
                                Log.e(TAGLOG, "End : " + end );

                                for (int i = start; i >= end; i--) {
                                    ETCProducts etcProducts = new ETCProducts();
                                    etcProducts.setUploadProductsDate(etcProductConstructorArrayList.get(i).uploadProductsDate);
                                    etcProducts.setUserID(etcProductConstructorArrayList.get(i).userID);
                                    etcProducts.setTitleOfETCProduct(etcProductConstructorArrayList.get(i).titleOfETCProduct);
                                    etcProducts.setProductDetailInform(etcProductConstructorArrayList.get(i).productDetailInform);
                                    etcProducts.setProductExpireDateOfPurchase(etcProductConstructorArrayList.get(i).productExpireDateOfPurchase);
                                    etcProducts.setProductImageURL(etcProductConstructorArrayList.get(i).productImageURL);
                                    etcProducts.setUserManagementCode(etcProductConstructorArrayList.get(i).userManagementCode);
                                    etcProducts.setProductManagementCode(etcProductConstructorArrayList.get(i).productManagementCode);
                                    etcProductsArrayList.add(etcProducts);
                                    etcPurchaseAdapter.notifyItemInserted(etcProductsArrayList.size());
                                }
                                etcPurchaseAdapter.setLoaded();
                            }
                        }, 2000);
                    }
                });

                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                    GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null & gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);
                            showDetailETCProduct(etcProductsArrayList.get(position).getUploadProductsDate(), etcProductsArrayList.get(position).getUserID(),
                                    etcProductsArrayList.get(position).getTitleOfETCProduct(), etcProductsArrayList.get(position).getProductDetailInform(),
                                    etcProductsArrayList.get(position).getProductExpireDateOfPurchase(), etcProductsArrayList.get(position).getProductImageURL(),
                                    etcProductsArrayList.get(position).getProductManagementCode(), etcProductsArrayList.get(position).getUserManagementCode());

                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
            }
        });
    }

    private void showDetailETCProduct(String uploadProductsDate, String userID, String titleOfETCProduct, String productDetailInform,
                                      String productExpireDateOfPurchase, String productImageURL, int productManagementCode, int userManagementCode) {
        Intent intent = new Intent(context.getApplicationContext(), ETCProductContentsActivity.class);
        intent.putExtra("uploadProductsDate", uploadProductsDate);
        intent.putExtra("userID", userID);
        intent.putExtra("titleOfETCProduct", titleOfETCProduct);
        intent.putExtra("productDetailInform", productDetailInform);
        intent.putExtra("productExpireDateOfPurchase", productExpireDateOfPurchase);
        intent.putExtra("productImageURL", productImageURL);
        intent.putExtra("productManagementCode", productManagementCode);
        intent.putExtra("userManagementCode", userManagementCode);
        startActivity(intent);
        //((MainPurchaseActivity) getActivity()).finish();
    }

}
