package com.example.user.kupurchase1.Purchase.Shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.kupurchase1.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-14.
 */
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    private String TAGLOG = "로그: ShoppingAdapter: ";

    private ArrayList<ShoppingCarts> shoppingCartsArrayList;
    public Context context;

    public ShoppingAdapter(ArrayList<ShoppingCarts> shoppingCartsArrayList, Context context) {
        this.shoppingCartsArrayList = shoppingCartsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shopping_cart_contents, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvKuProductName.setText(shoppingCartsArrayList.get(position).getKuProductName());
        holder.tvKuProductPrice.setText(String.valueOf(shoppingCartsArrayList.get(position).getKuProductPrice()));
        holder.tvKuProductCount.setText(String.valueOf(shoppingCartsArrayList.get(position).getKuProductCount()));
        int calculatedPrice = shoppingCartsArrayList.get(position).getKuProductPrice() * shoppingCartsArrayList.get(position).getKuProductCount();
        holder.tvKuProductTotalPrice.setText(String.valueOf(calculatedPrice));
    }

    @Override
    public int getItemCount() {
        return shoppingCartsArrayList.size();
    }

    public void addItem(String productName, int productCount, int productPrice, int productManagementCode) {
        ShoppingCarts shoppingCarts = new ShoppingCarts();
        shoppingCarts.setKuProductName(productName);
        shoppingCarts.setKuProductCount(productCount);
        shoppingCarts.setKuProductPrice(productPrice);
        shoppingCarts.setKuProductManagementCode(productManagementCode);
        shoppingCartsArrayList.add(shoppingCarts);
        notifyItemInserted(shoppingCartsArrayList.size());
    }

    public void removeItem(int position) {
        shoppingCartsArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, shoppingCartsArrayList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvKuProductName, tvKuProductPrice, tvKuProductCount, tvKuProductTotalPrice;

        public ViewHolder(View view) {
            super(view);

            tvKuProductName = (TextView) view.findViewById(R.id.tvKuProductName);
            tvKuProductPrice = (TextView) view.findViewById(R.id.tvKuProductPrice);
            tvKuProductCount = (TextView) view.findViewById(R.id.tvKuProductCount);
            tvKuProductTotalPrice = (TextView) view.findViewById(R.id.tvKuProductTotalPrice);
        }
    }
}
