package com.example.user.kupurchase1.Community.FreeBoard;

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
 * Created by user on 2016-03-12.
 */
public class FreeBoardAdapter extends RecyclerView.Adapter{

    private String TAGLOG = "로그: FreeBoardAdapter: ";
    private ArrayList<FreeBoards> freeBoardsArrayList;
    public Context context;

    public FreeBoardAdapter(ArrayList<FreeBoards> freeBoardsArrayList, Context context) {
        this.freeBoardsArrayList = freeBoardsArrayList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freeboard_content_layout, parent, false);
        viewHolder = new FreeBoardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof FreeBoardViewHolder) {
            FreeBoards freeBoards = (FreeBoards) freeBoardsArrayList.get(position);
            ((FreeBoardViewHolder) holder).tvFreeBoardTitle.setText("제목 : " + freeBoards.getFreeBoardTitle());
            ((FreeBoardViewHolder) holder).tvFreeBoardUploadDate.setText(freeBoards.getFreeBoardUploadDate());
            ((FreeBoardViewHolder) holder).tvFreeBoardUserID.setText(freeBoards.getUserID());
            ((FreeBoardViewHolder) holder).tvFreeBoardCount.setText("조회수 : " + String.valueOf(freeBoards.getFreeBoardCount()));
            ((FreeBoardViewHolder) holder).tvFreeBoardTempContents.setText(freeBoards.getFreeBoardContents());

            if(freeBoards.getFreeBoardImageURL().equals("ImageEmpty")) {
                ((FreeBoardViewHolder) holder).ivFreeBoardImage.setVisibility(View.GONE);
            } else {
                try {
                    ((FreeBoardViewHolder) holder).ivFreeBoardImage.setVisibility(View.VISIBLE);
                    Picasso.with(context).invalidate(freeBoards.getFreeBoardImageURL());
                    Picasso.with(context).load(freeBoards.getFreeBoardImageURL())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .fit()
                            .into(((FreeBoardViewHolder) holder).ivFreeBoardImage);
                } catch (OutOfMemoryError outOfMemoryError) {
                    outOfMemoryError.printStackTrace();
                    Toast.makeText(context, "이미지 로딩 메모리 에러", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return freeBoardsArrayList.size();
    }


    public class FreeBoardViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFreeBoardTitle, tvFreeBoardUploadDate, tvFreeBoardUserID, tvFreeBoardCount, tvFreeBoardReplyCount, tvFreeBoardTempContents;
        public ImageView ivFreeBoardImage;

        public FreeBoardViewHolder(View view) {
            super(view);

            tvFreeBoardTitle = (TextView) view.findViewById(R.id.tvFreeBoardTitle);
            tvFreeBoardUploadDate = (TextView) view.findViewById(R.id.tvFreeBoardUploadDate);
            tvFreeBoardUserID = (TextView) view.findViewById(R.id.tvFreeBoardUserID);
            tvFreeBoardCount = (TextView) view.findViewById(R.id.tvFreeBoardCount);
//            tvFreeBoardReplyCount = (TextView) view.findViewById(R.id.tvFreeBoardReplyCount);
            tvFreeBoardTempContents = (TextView) view.findViewById(R.id.tvFreeBoardTempContents);
            ivFreeBoardImage = (ImageView) view.findViewById(R.id.ivFreeBoardImage);
        }
    }
}
