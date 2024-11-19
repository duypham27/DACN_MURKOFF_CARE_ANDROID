package com.example.dacn_murkoff_care_android.Container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationCreate {

    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }
}
