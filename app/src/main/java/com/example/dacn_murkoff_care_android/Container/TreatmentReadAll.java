package com.example.dacn_murkoff_care_android.Container;

import com.example.dacn_murkoff_care_android.Model.Treatment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreatmentReadAll {

    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("data")
    @Expose
    private List<Treatment> data;

    public int getResult() {
        return result;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Treatment> getData() {
        return data;
    }
}
