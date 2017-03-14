package com.example.user.kupurchase1.Setting.AskAdministrator;

/**
 * Created by user on 2016-03-21.
 */
public class AskAdminConstructor {

    public String askTitle, askContents, fromUserID;

    public AskAdminConstructor(String askTitle, String askContents, String fromUserID) {
        this.askTitle = askTitle;
        this.askContents = askContents;
        this.fromUserID = fromUserID;
    }
}
