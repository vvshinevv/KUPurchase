package com.example.user.kupurchase1.Community.FreeBoard;

/**
 * Created by user on 2016-03-12.
 */
public class FreeBoardConstructor {

    public String freeBoardTitle,userID,freeBoardContents,freeBoardUploadDate, isImageEmpty, freeBoardImageURL;
    public int freeBoardManagementCode, freeBoardCount;

    public FreeBoardConstructor(String freeBoardTitle, String freeBoardContents, String freeBoardUploadDate, String userID
            , int freeBoardManagementCode, int freeBoardCount, String freeBoardImageURL) {
        this.freeBoardTitle = freeBoardTitle;
        this.freeBoardContents = freeBoardContents;
        this.freeBoardUploadDate = freeBoardUploadDate;
        this.userID = userID;
        this.freeBoardManagementCode = freeBoardManagementCode;
        this.freeBoardCount = freeBoardCount;
        this.freeBoardImageURL = freeBoardImageURL;
    }

    public FreeBoardConstructor(String userID, String freeBoardTitle, String freeBoardContents, String isImageEmpty) {
        this.freeBoardTitle = freeBoardTitle;
        this.freeBoardContents = freeBoardContents;
        this.freeBoardUploadDate = "";
        this.userID = userID;
        this.isImageEmpty = isImageEmpty;
    }

    public FreeBoardConstructor(String freeBoardTitle, String freeBoardContents, int freeBoardManagementCode, String isImageEmpty, String freeBoardImageURL, String userID){
        this.freeBoardTitle = freeBoardTitle;
        this.freeBoardContents = freeBoardContents;
        this.freeBoardUploadDate = "";
        this.userID = userID;
        this.freeBoardManagementCode = freeBoardManagementCode;
        this.isImageEmpty = isImageEmpty;
        this.freeBoardImageURL = freeBoardImageURL;
    }

}
