package com.example.user.kupurchase1.Order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

import java.util.ArrayList;

/**
 * Created by dahye on 2016-03-23.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private  String TAGLOG = "로그 : OrderAdapter : ";

    private ArrayList<OrderKuProducts> orderKuProductsArrayList;
    public Context context;
    private UserLocalStore userLocalStore;
    public int autoIncrement;
    public int userManagementCode;

    public OrderAdapter(ArrayList<OrderKuProducts> orderKuProductses, Context context){
        this.orderKuProductsArrayList = orderKuProductses;
        this.context = context;
        userLocalStore = new UserLocalStore(context);
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_content_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, final int position) {
        holder.tvKuProductName.setText("상품명 : " + orderKuProductsArrayList.get(position).getKuProductName());
        holder.tvKuProductCount.setText("상품 개수 : " + orderKuProductsArrayList.get(position).getKuProductCount());
        holder.tvKuProductPrice.setText("상품 금액 : " + orderKuProductsArrayList.get(position).getKuProductPrice());
        int calculatedPrice = orderKuProductsArrayList.get(position).getKuProductPrice() * orderKuProductsArrayList.get(position).getKuProductCount();
        holder.tvProductTotalPrice.setText("금액 : " + String.valueOf(calculatedPrice));
        int kuProductDepositState = orderKuProductsArrayList.get(position).getKuProductDepositState();

        if(orderKuProductsArrayList.get(position).getKuProductDepositState()==1) {
            holder.tvProductDeposit.setText("입금상태 : 완료");
        }
        else{
            holder.tvProductDeposit.setText("입금상태 : 미완료");
        }

        holder.deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoIncrement = orderKuProductsArrayList.get(position).getAutoIncrement();
                User user = userLocalStore.getLoggedInUser();
                userManagementCode = user.userManagementCode;
                if(orderKuProductsArrayList.get(position).getKuProductDepositState()==0){
                    OrderActivity orderActivity = (OrderActivity) OrderActivity.orderActivity;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderActivity);
                    alertDialog.setTitle("삭제 하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OrderConstructor orderConstructor = new OrderConstructor(autoIncrement, userManagementCode);

                                    OrderServerRequests orderServerRequests = new OrderServerRequests(autoIncrement, userManagementCode);
                                    orderServerRequests.deleteOrder(orderConstructor, new GetOrderProductCallback() {
                                        @Override
                                        public void done(ArrayList<OrderConstructor> returnedOrders) {
                                            Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show();

                                            //   Intent intent = new Intent(context.getApplicationContext(), OrderActivity.class);
                                            // context.startActivity(intent);

                                            OrderActivity orderActivity = (OrderActivity) OrderActivity.orderActivity;
                                            orderActivity.OpenActivity();
                                            // context.startActivity(new Intent(context, OrderActivity.class));


                                            System.out.println("성공!!");

                                        }
                                    });
                                }
                            }).setNegativeButton("취소", null);
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();

                }else{
                    Toast.makeText(context, "이미 결제 되어 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return orderKuProductsArrayList.size();    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvKuProductName, tvKuProductCount, tvKuProductPrice, tvProductDeposit, tvProductTotalPrice;
        public ImageView deleteOrder;

        public ViewHolder(View view) {
            super(view);

            tvKuProductName = (TextView)view.findViewById(R.id.tvKuProductName);
            tvKuProductCount = (TextView)view.findViewById(R.id.tvKuProductCount);
            tvKuProductPrice = (TextView)view.findViewById(R.id.tvKuProductPrice);
            tvProductDeposit = (TextView)view.findViewById(R.id.tvKuProductDepositState);
            tvProductTotalPrice = (TextView)view.findViewById(R.id.tvKuProductTotalPrice);
            deleteOrder = (ImageView)view.findViewById(R.id.ivTrashIcon);
        }
    }
}

