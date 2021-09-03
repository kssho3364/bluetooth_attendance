package com.example.smartattendance;

import android.app.Application;

public class UserInfoData extends Application {
    private String myID;
    private String myPW;
    private String myName;
    private String myComp;

    public String getMyID() {
        return myID;
    }

    public void setMyID(String myID) {
        this.myID = myID;
    }

    public String getMyPW() {
        return myPW;
    }

    public void setMyPW(String myPW) {
        this.myPW = myPW;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyComp() {
        return myComp;
    }

    public void setMyComp(String myComp) {
        this.myComp = myComp;
    }
}
