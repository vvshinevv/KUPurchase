package com.example.user.kupurchase1.Purchase.KUPurchase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kupurchase1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-09.
 */
public class KuPurchaseAdapter extends RecyclerView.Adapter<KuPurchaseAdapter.ViewHolder> {

    private String TAGLOG = "로그: KuPurchaseAdapter: ";


    private ArrayList<KuProducts> kuProductsArrayList;
    private Context context;

    public KuPurchaseAdapter(ArrayList<KuProducts> kuProductsArrayList, Context context) {
        this.kuProductsArrayList = kuProductsArrayList;
        this.context = context;
    }

    @Override
    public KuPurchaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ku_purchase_content_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KuPurchaseAdapter.ViewHolder holder, int position) {

        try {
            Picasso.with(context).load("localhost/KUPurchase/"+kuProductsArrayList.get(position).getKuProductImageURL())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .into(holder.ivKuProductImage);
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
            Toast.makeText(context, "이미지 로딩 메모리 에러", Toast.LENGTH_LONG).show();

        }

        holder.tvKuProductName.setText(kuProductsArrayList.get(position).getKuProductName());
        holder.tvKuProductPrice.setText(String.valueOf(kuProductsArrayList.get(position).getKuProductPrice()));
        holder.tvKuProductExpireDate.setText(kuProductsArrayList.get(position).getKuProductExpireDate());
        holder.tvKuProductDepositStartDate.setText(kuProductsArrayList.get(position).getKuProductDepositStartDate());
        holder.tvKuProductDepositDueDate.setText(kuProductsArrayList.get(position).getKuProductDepositDueDate());
    }

    @Override
    public int getItemCount() {
        return kuProductsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivKuProductImage;
        public TextView tvKuProductName, tvKuProductPrice, tvKuProductExpireDate, tvKuProductDepositStartDate, tvKuProductDepositDueDate;

        public ViewHolder(View view) {
            super(view);

            ivKuProductImage = (ImageView) view.findViewById(R.id.ivKuProductImage);
            tvKuProductName = (TextView) view.findViewById(R.id.tvKuProductName);
            tvKuProductPrice = (TextView) view.findViewById(R.id.tvKuProductPrice);
            tvKuProductExpireDate = (TextView) view.findViewById(R.id.tvKuProductExpireDate);
            tvKuProductDepositStartDate = (TextView) view.findViewById(R.id.tvKuProductDepositStartDate);
            tvKuProductDepositDueDate = (TextView) view.findViewById(R.id.tvKuProductDepositDueDate);

        }
    }
}
