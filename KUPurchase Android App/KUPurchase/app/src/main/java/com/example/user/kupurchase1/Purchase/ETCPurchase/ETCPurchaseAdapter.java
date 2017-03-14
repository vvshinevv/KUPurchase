package com.example.user.kupurchase1.Purchase.ETCPurchase;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kupurchase1.OnLoadMoreListener;
import com.example.user.kupurchase1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-12.
 */
public class ETCPurchaseAdapter extends RecyclerView.Adapter {

    private String TAGLOG = "로그: ETCPurchaseAdapter: ";

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;
    private ArrayList<ETCProducts> etcProductsArrayList;
    private Context context;

    public ETCPurchaseAdapter(ArrayList<ETCProducts> etcProductsArrayList, Context context, RecyclerView recyclerView) {
        this.etcProductsArrayList = etcProductsArrayList;
        this.context = context;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return etcProductsArrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if(viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.etc_purchase_content_layout, parent, false);
            viewHolder = new ETCProductViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar_layout, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if ( holder instanceof ETCProductViewHolder ) {
            ETCProducts etcProducts = (ETCProducts)etcProductsArrayList.get(position);

            try {
                Picasso.with(context).invalidate(etcProducts.getProductImageURL());
                Picasso.with(context).load(etcProducts.getProductImageURL())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .fit()
                        .into(((ETCProductViewHolder) holder).ivETCProductImage);
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
                Toast.makeText(context, "이미지 로딩 메모리 에러", Toast.LENGTH_LONG).show();
            }


            ((ETCProductViewHolder)holder).tvETCProductTitle.setText(etcProducts.getTitleOfETCProduct());
            ((ETCProductViewHolder)holder).tvUserID.setText(etcProducts.getUserID());
            ((ETCProductViewHolder)holder).tvETCProductExpireDate.setText(etcProducts.getProductExpireDateOfPurchase());
            ((ETCProductViewHolder)holder).tvETCProductUploadDate.setText(etcProducts.getUploadProductsDate());
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return etcProductsArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public static class ETCProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivETCProductImage;
        public TextView tvETCProductTitle, tvUserID, tvETCProductExpireDate, tvETCProductUploadDate;

        public ETCProductViewHolder(View view) {
            super(view);

            ivETCProductImage = (ImageView) view.findViewById(R.id.ivETCProductImage);
            tvETCProductTitle = (TextView) view.findViewById(R.id.tvETCProductTitle);
            tvUserID = (TextView) view.findViewById(R.id.tvUserID);
            tvETCProductExpireDate = (TextView) view.findViewById(R.id.tvETCProductExpireDate);
            tvETCProductUploadDate = (TextView) view.findViewById(R.id.tvETCProductUploadDate);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}
