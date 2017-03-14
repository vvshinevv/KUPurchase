package com.example.user.kupurchase1.Community;

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

import com.example.user.kupurchase1.Community.Notification.GetNotificationCallback;
import com.example.user.kupurchase1.Community.Notification.NotificationAdapter;
import com.example.user.kupurchase1.Community.Notification.NotificationConstructor;
import com.example.user.kupurchase1.Community.Notification.NotificationContentsActivity;
import com.example.user.kupurchase1.Community.Notification.Notifications;
import com.example.user.kupurchase1.Community.Notification.NotificationsServerRequests;
import com.example.user.kupurchase1.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-11.
 */
public class NotificationFragment extends Fragment {

    private String TAGLOG = "로그: NotificationFragment: ";

    public View view;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_notification_fragment, container, false);
        context = getContext();

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

        final ArrayList<Notifications> notificationsArrayList = new ArrayList<>();
        NotificationsServerRequests notificationsServerRequests = new NotificationsServerRequests(context);
        notificationsServerRequests.fetchNotificationInBackground(new GetNotificationCallback() {
            @Override
            public void done(ArrayList<NotificationConstructor> returnedNotifications) {
                for (int i = 0; i < returnedNotifications.size(); i++) {
                    Notifications notifications = new Notifications();
                    notifications.setNoticeOfTitle(returnedNotifications.get(i).noticeOfTitle);
                    notifications.setNoticeOfUploadDate(returnedNotifications.get(i).noticeOfUploadDate);
                    notifications.setNoticeOfContents(returnedNotifications.get(i).noticeOfContents);
                    notificationsArrayList.add(notifications);
                }
                NotificationAdapter notificationAdapter = new NotificationAdapter(notificationsArrayList, context.getApplicationContext());
                recyclerView.setAdapter(notificationAdapter);

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
                            showDetailNotification(notificationsArrayList.get(position).getNoticeOfTitle(), notificationsArrayList.get(position).getNoticeOfUploadDate(),
                                    notificationsArrayList.get(position).getNoticeOfContents());
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

    private void showDetailNotification(String noticeTitle, String noticeDate, String noticeContents) {
        Intent intent = new Intent(context.getApplicationContext(), NotificationContentsActivity.class);
        intent.putExtra("noticeTitle", noticeTitle);
        intent.putExtra("noticeDate", noticeDate);
        intent.putExtra("noticeContents", noticeContents);
        startActivity(intent);
        //((MainCommunityActivity)getActivity()).finish();
    }
}
