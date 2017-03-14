package com.example.user.kupurchase1.Community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.kupurchase1.Community.FreeBoard.FreeBoardAdapter;
import com.example.user.kupurchase1.Community.FreeBoard.FreeBoardConstructor;
import com.example.user.kupurchase1.Community.FreeBoard.FreeBoardContentsActivity;
import com.example.user.kupurchase1.Community.FreeBoard.FreeBoardServerRequests;
import com.example.user.kupurchase1.Community.FreeBoard.FreeBoards;
import com.example.user.kupurchase1.Community.FreeBoard.GetFreeBoardCallback;
import com.example.user.kupurchase1.Community.FreeBoard.RegisterFreeBoardActivity;
import com.example.user.kupurchase1.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-09.
 */
public class FreeBoardFragment extends Fragment {

    private String TAGLOG = "로그: FreeBoardFragment: ";

    public View view;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_freeboard_fragment, container, false);
        context = view.getContext();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterFreeBoardActivity.class);
                startActivity(intent);
                ((MainCommunityActivity) getActivity()).finish();
            }
        });

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

        final ArrayList<FreeBoards> freeBoardsArrayList = new ArrayList<>();

        FreeBoardServerRequests freeBoardServerRequests = new FreeBoardServerRequests(context);
        freeBoardServerRequests.fetchFreeBoardInBackground(new GetFreeBoardCallback() {
            @Override
            public void done(ArrayList<FreeBoardConstructor> returnedFreeBoard) {
                for (int i = 0; i < returnedFreeBoard.size(); i++) {
                    FreeBoards freeBoards = new FreeBoards();
                    freeBoards.setFreeBoardTitle(returnedFreeBoard.get(i).freeBoardTitle);
                    freeBoards.setFreeBoardContents(returnedFreeBoard.get(i).freeBoardContents);
                    freeBoards.setFreeBoardUploadDate(returnedFreeBoard.get(i).freeBoardUploadDate);
                    freeBoards.setUserID(returnedFreeBoard.get(i).userID);
                    freeBoards.setFreeBoardManagementCode(returnedFreeBoard.get(i).freeBoardManagementCode);
                    freeBoards.setFreeBoardCount(returnedFreeBoard.get(i).freeBoardCount);
                    freeBoards.setFreeBoardImageURL(returnedFreeBoard.get(i).freeBoardImageURL);
                    freeBoardsArrayList.add(freeBoards);
                }

                FreeBoardAdapter freeBoardAdapter = new FreeBoardAdapter(freeBoardsArrayList, context.getApplicationContext());
                recyclerView.setAdapter(freeBoardAdapter);

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
                            showDetailFreeBoard(freeBoardsArrayList.get(position).getFreeBoardTitle(), freeBoardsArrayList.get(position).getFreeBoardContents(),
                                    freeBoardsArrayList.get(position).getFreeBoardUploadDate(),freeBoardsArrayList.get(position).getUserID(),
                                    freeBoardsArrayList.get(position).getFreeBoardManagementCode(), freeBoardsArrayList.get(position).getFreeBoardCount(),
                                    freeBoardsArrayList.get(position).getFreeBoardImageURL());

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

    private void showDetailFreeBoard(String freeBoardTitle, String freeBoardContents, String freeBoardUploadDate,
                                     String userID, int freeBoardManagementCode, int freeBoardCount, String freeBoardImageURL) {
        Intent intent = new Intent(context.getApplicationContext(), FreeBoardContentsActivity.class);
        intent.putExtra("freeBoardTitle", freeBoardTitle);
        intent.putExtra("freeBoardContents", freeBoardContents);
        intent.putExtra("freeBoardUploadDate", freeBoardUploadDate);
        intent.putExtra("userID",userID);
        intent.putExtra("freeBoardManagementCode", freeBoardManagementCode);
        intent.putExtra("freeBoardCount", freeBoardCount);
        intent.putExtra("freeBoardImageURL", freeBoardImageURL);

        FreeBoardServerRequests freeBoardServerRequests = new FreeBoardServerRequests(context);
        freeBoardServerRequests.updateFreeBoardCountInBackground(freeBoardManagementCode);

        startActivity(intent);
    }
}
