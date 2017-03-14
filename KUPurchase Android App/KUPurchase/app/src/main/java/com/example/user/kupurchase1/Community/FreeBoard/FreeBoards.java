package com.example.user.kupurchase1.Community.FreeBoard;

/**
 * Created by user on 2016-03-12.
 */
public class FreeBoards {

    private String freeBoardTitle;
    private String freeBoardContents;
    private String freeBoardTempContents;
    private String freeBoardUploadDate;
    private String userID;
    private String freeBoardImageURL;
    private int freeBoardManagementCode;
    private int freeBoardCount;

    public String getFreeBoardTitle() {
        return freeBoardTitle;
    }
    public void setFreeBoardTitle(String freeBoardTitle) {
        this.freeBoardTitle = freeBoardTitle;
    }

    public String getFreeBoardContents() {
        return freeBoardContents;
    }
    public void setFreeBoardContents(String freeBoardContents) {
        this.freeBoardContents = freeBoardContents;
    }

    public String getFreeBoardUploadDate() {
        return freeBoardUploadDate;
    }
    public void setFreeBoardUploadDate(String freeBoardUploadDate) {
        this.freeBoardUploadDate = freeBoardUploadDate;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getFreeBoardManagementCode() {
        return freeBoardManagementCode;
    }
    public void setFreeBoardManagementCode(int freeBoardManagementCode) {
        this.freeBoardManagementCode = freeBoardManagementCode;
    }

    public String getFreeBoardTempContents() {
        return freeBoardTempContents;
    }
    public void setFreeBoardTempContents(String freeBoardTempContents) {
        this.freeBoardTempContents = freeBoardTempContents;
    }

    public String getFreeBoardImageURL() {
        return freeBoardImageURL;
    }
    public void setFreeBoardImageURL(String freeBoardImageURL) {
        this.freeBoardImageURL = freeBoardImageURL;
    }

    public int getFreeBoardCount() {
        return freeBoardCount;
    }
    public void setFreeBoardCount(int freeBoardCount) {
        this.freeBoardCount = freeBoardCount;
    }


}
