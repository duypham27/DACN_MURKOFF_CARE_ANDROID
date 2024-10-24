package com.example.dacn_murkoff_android.Container;

import com.example.dacn_murkoff_android.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientProfile {
    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("msg")
    @Expose
    private String msg;


    @SerializedName("data")
    @Expose
    private User data;

    public Integer getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public User getData() {
        return data;
    }
}
