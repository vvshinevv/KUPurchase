package com.example.user.kupurchase1.Purchase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.kupurchase1.Purchase.KUPurchase.GetKuProductCallback;
import com.example.user.kupurchase1.Purchase.KUPurchase.KuProductConstructor;
import com.example.user.kupurchase1.Purchase.KUPurchase.KuProducts;
import com.example.user.kupurchase1.Purchase.KUPurchase.KuProductsServerRequests;
import com.example.user.kupurchase1.Purchase.KUPurchase.KuPurchaseAdapter;
import com.example.user.kupurchase1.Purchase.Shopping.ShoppingDialogActivity;
import com.example.user.kupurchase1.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-09.
 */
public class KUPurchaseFragment extends Fragment {

    private String TAGLOG = "로그: KUPurchaseFragment: ";

    public View view;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ku_purchase_fragment, container, false);
        context = view.getContext();

        initView();
        return view;
    }

    private void initView() {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<KuProducts> kuProductsArrayList = new ArrayList<>();
        KuProductsServerRequests kuProductsServerRequests = new KuProductsServerRequests();
        kuProductsServerRequests.fetchKuProductsListInBackground(new GetKuProductCallback() {
            @Override
            public void done(ArrayList<KuProductConstructor> returnedKuProducts) {
                for (int i = 0; i < returnedKuProducts.size(); i++) {
                    KuProducts kuProducts = new KuProducts();
                    kuProducts.setKuProductManagementCode(returnedKuProducts.get(i).kuProductManagementCode);
                    kuProducts.setKuProductName(returnedKuProducts.get(i).kuProductName);
                    kuProducts.setKuProductPrice(returnedKuProducts.get(i).kuProductPrice);
                    kuProducts.setKuProductExpireDate(returnedKuProducts.get(i).kuProductExpireDate);
                    kuProducts.setKuProductDepositStartDate(returnedKuProducts.get(i).kuProductDepositStartDate);
                    kuProducts.setKuProductDepositDueDate(returnedKuProducts.get(i).kuProductDepositDueDate);
                    kuProducts.setKuProductImageURL(returnedKuProducts.get(i).kuProductImageURL);
                    kuProductsArrayList.add(kuProducts);
                }
                KuPurchaseAdapter kuPurchaseAdapter = new KuPurchaseAdapter(kuProductsArrayList, context.getApplicationContext());
                recyclerView.setAdapter(kuPurchaseAdapter);


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
                            alertDialogKUProducts(kuProductsArrayList.get(position).getKuProductManagementCode(), kuProductsArrayList.get(position).getKuProductName(),
                                    kuProductsArrayList.get(position).getKuProductPrice(), kuProductsArrayList.get(position).getKuProductExpireDate(), kuProductsArrayList.get(position).getKuProductDepositStartDate(),
                                    kuProductsArrayList.get(position).getKuProductDepositDueDate(), kuProductsArrayList.get(position).getKuProductImageURL());
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

    private void alertDialogKUProducts(int kuProductManagementCode, String kuProductName, int kuProductPrice, String kuProductExpireDate, String kuProductDepositStartDate,
                                       String kuProductDepositDueDate, String kuProductImageURL) {
        Intent intent = new Intent(context.getApplicationContext(), ShoppingDialogActivity.class);
        intent.putExtra("kuProductManagementCode", kuProductManagementCode);
        intent.putExtra("kuProductName", kuProductName);
        intent.putExtra("kuProductPrice", kuProductPrice);
        intent.putExtra("kuProductExpireDate", kuProductExpireDate);
        intent.putExtra("kuProductDepositStartDate", kuProductDepositStartDate);
        intent.putExtra("kuProductDepositDueDate", kuProductDepositDueDate);
        intent.putExtra("kuProductImageURL", kuProductImageURL);
        startActivity(intent);
    }
}
