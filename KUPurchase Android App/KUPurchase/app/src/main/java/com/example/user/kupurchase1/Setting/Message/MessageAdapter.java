package com.example.user.kupurchase1.Setting.Message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

import java.util.ArrayList;

/**
 * Created by user on 2016-03-17.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private String TAGLOG = "로그: MessageAdapter: ";

    private ArrayList<Messages> messagesArrayList;
    public Context context;
    public UserLocalStore userLocalStore;

    public MessageAdapter(ArrayList<Messages> messagesArrayList, Context context) {
        this.messagesArrayList = messagesArrayList;
        this.context = context;
        userLocalStore = new UserLocalStore(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_content_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userLocalStore.getLoggedInUser();
        ViewGroup.LayoutParams layoutParams = holder.linMessageBox.getLayoutParams();
        LinearLayout.LayoutParams castLayoutParams = (LinearLayout.LayoutParams) layoutParams;

        if(user.userID.equals(messagesArrayList.get(position).getFromUserID())) { // 나 자신이면
            holder.linMessageBox.setBackgroundResource(R.drawable.chat_box_for_me);
            castLayoutParams.gravity = Gravity.RIGHT;

            holder.bAnswerMessage.setVisibility(View.GONE);
            holder.tvToUserID.setText(messagesArrayList.get(position).getToUserID());
            holder.tvFromOrTo.setText(" 에게 보냄");
            holder.tvSendMessageTime.setText(messagesArrayList.get(position).getSendMessageTime());
            holder.tvMessage.setText(messagesArrayList.get(position).getSendMessage());
        } else { // 상대방이면
            holder.linMessageBox.setBackgroundResource(R.drawable.chat_box_for_you);
            castLayoutParams.gravity = Gravity.LEFT;

            holder.bAnswerMessage.setVisibility(View.VISIBLE);
            holder.tvToUserID.setText(messagesArrayList.get(position).getFromUserID());
            holder.tvFromOrTo.setText(" 에게 받음");
            holder.tvSendMessageTime.setText(messagesArrayList.get(position).getSendMessageTime());
            holder.tvMessage.setText(messagesArrayList.get(position).getSendMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvToUserID, tvFromOrTo, tvSendMessageTime, tvMessage;
        public Button bAnswerMessage;
        public LinearLayout linMessageBox;


        public ViewHolder(final View view) {
            super(view);

            tvToUserID = (TextView) view.findViewById(R.id.tvToUserID);
            tvFromOrTo = (TextView) view.findViewById(R.id.tvFromOrTo);
            tvSendMessageTime = (TextView) view.findViewById(R.id.tvSendMessageTime);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            bAnswerMessage = (Button) view.findViewById(R.id.bAnswerMessage);

            linMessageBox = (LinearLayout) view.findViewById(R.id.linMessageBox);
            bAnswerMessage.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bAnswerMessage:
                    Intent intent = new Intent(context, SendMessageActivity.class);
                    intent.putExtra("toUserID", tvToUserID.getText().toString());
                    Log.e(TAGLOG, "toUserID : " + tvToUserID.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//                    MessageActivity messageActivity = (MessageActivity)MessageActivity.messageActivity;
//                    messageActivity.finish();
                    break;
            }
        }
    }
}
