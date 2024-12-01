package com.example.dacn_murkoff_care_android.Container;

import com.example.dacn_murkoff_care_android.Model.Record;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordReadByID {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    private Record data;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public Record getData() {
        return data;
    }
}
