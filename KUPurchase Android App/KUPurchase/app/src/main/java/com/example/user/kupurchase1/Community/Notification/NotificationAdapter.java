package com.example.user.kupurchase1.Community.Notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.kupurchase1.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-11.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private String TAGLOG = "로그: NotificationAdapter: ";

    private ArrayList<Notifications> notificationsArrayList;
    public Context context;

    public NotificationAdapter(ArrayList<Notifications> notificationsArrayList, Context context) {
        this.notificationsArrayList = notificationsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_content_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvNotificationTitle.setText(notificationsArrayList.get(position).getNoticeOfTitle());
        holder.tvNotificationUploadDate.setText(notificationsArrayList.get(position).getNoticeOfUploadDate());
    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNotificationTitle, tvNotificationUploadDate;

        public ViewHolder(View view) {
            super(view);

            tvNotificationTitle = (TextView) view.findViewById(R.id.tvNotificationTitle);
            tvNotificationUploadDate = (TextView) view.findViewById(R.id.tvNotificationUploadDate);
        }
    }
}
