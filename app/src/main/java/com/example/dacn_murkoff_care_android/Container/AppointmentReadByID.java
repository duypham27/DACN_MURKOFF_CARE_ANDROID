package com.example.dacn_murkoff_care_android.Container;

import com.example.dacn_murkoff_care_android.Model.Appointment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentReadByID {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private Appointment data;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public Appointment getData() {
        return data;
    }
}
