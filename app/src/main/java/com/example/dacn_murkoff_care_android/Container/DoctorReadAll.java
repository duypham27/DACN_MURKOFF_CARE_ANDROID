package com.example.dacn_murkoff_care_android.Container;

import com.example.dacn_murkoff_care_android.Model.Doctor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorReadAll {
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
    private List<Doctor> data;

    public int getResult() {
        return result;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Doctor> getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
