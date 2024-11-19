package com.example.dacn_murkoff_care_android.Container;

import com.example.dacn_murkoff_care_android.Model.Appointment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentReadAll {

    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("data")
    @Expose
    private List<Appointment> data;

    public int getResult() {
        return result;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMsg() {
        return msg;
    }

    public List<Appointment> getData() {
        return data;
    }

}
