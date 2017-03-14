package com.example.user.kupurchase1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2016-03-08.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("userID", user.userID);
        spEditor.putString("userPW", user.userPW);
        spEditor.putString("userName", user.userName);
        spEditor.putString("userPhoneNum", user.userPhoneNum);
        spEditor.putString("userMailAddress", user.userMailAddress);
        spEditor.putInt("userManagementCode", user.userManagementCode);
        spEditor.commit();
    }

    public User getLoggedInUser() {
        if(userLocalDatabase.getBoolean("LoggedIn", false) == false) {
            return null;
        }

        String userID = userLocalDatabase.getString("userID", "");
        String userPW = userLocalDatabase.getString("userPW", "");
        String userName = userLocalDatabase.getString("userName", "");
        String userPhoneNum = userLocalDatabase.getString("userPhoneNum", "");
        String userMailAddress = userLocalDatabase.getString("userMailAddress", "");
        int userManagementCode = userLocalDatabase.getInt("userManagementCode", 0);

        User storedUser = new User(userID, userPW, userName, userPhoneNum, userMailAddress, userManagementCode);

        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if( userLocalDatabase.getBoolean("LoggedIn", false) == true) { // 로그인 되어있다면
            return true;
        } else { // 로그 아웃 되어있다면
            return false;
        }
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
