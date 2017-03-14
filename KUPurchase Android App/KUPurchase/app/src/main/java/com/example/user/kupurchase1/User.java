package com.example.user.kupurchase1;

/**
 * Created by user on 2016-03-08.
 */
public class User {

    public String userID, userPW, userName, userPhoneNum, userMailAddress, userToken;
    public int userManagementCode;

    public User(String userID, String userPW, String userName, String userPhoneNum, String userMailAddress, int userManagementCode) {
        this.userID = userID;
        this.userPW = userPW;
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.userMailAddress = userMailAddress;
        this.userManagementCode = userManagementCode;
    }

    public User(String userID, String userPW, String userName, String userPhoneNum, String userMailAddress, String userToken) {
        this.userID = userID;
        this.userPW = userPW;
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.userMailAddress = userMailAddress;
        this.userToken = userToken;
        this.userManagementCode = -1;
    }

    public User(String userID, String userPW) {
        this.userID = userID;
        this.userPW = userPW;
        this.userName = "";
        this.userPhoneNum = "";
        this.userMailAddress = "";
        this.userToken = "";
        this.userManagementCode = -1;
    }
}
