package com.example.dacn_murkoff_care_android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("booking_id")
    @Expose
    private int bookingId;

    @SerializedName("url")
    @Expose
    private String url;

    public int getId() {
        return id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getUrl() {
        return url;
    }

}
