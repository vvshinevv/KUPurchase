package com.example.user.kupurchase1.Setting.Message;

/**
 * Created by user on 2016-03-17.
 */
public class MessageConstructor {

    public String toUserID, fromUserID, sendMessageTime, sendMessage;

    public MessageConstructor(String toUserID, String fromUserID, String sendMessageTime, String sendMessage) {
        this.toUserID = toUserID;
        this.fromUserID = fromUserID;
        this.sendMessage = sendMessage;
        this.sendMessageTime = sendMessageTime;
    }

    public MessageConstructor(String toUserID, String sendMessage) {
        this.toUserID = toUserID;
        this.sendMessage = sendMessage;
    }
}
