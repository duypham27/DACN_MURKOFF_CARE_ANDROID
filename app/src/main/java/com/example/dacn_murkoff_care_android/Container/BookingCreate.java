package com.example.dacn_murkoff_care_android.Container;

import com.example.dacn_murkoff_care_android.Model.Booking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingCreate {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private Booking data;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public Booking getData() {
        return data;
    }
}
