package com.example.dacn_murkoff_android.Helper;

import android.app.Application;

import com.example.dacn_murkoff_android.Model.Option;
import com.example.dacn_murkoff_android.Model.User;
import com.example.dacn_murkoff_android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalVariable extends Application {
    private String accessToken;
    private User AuthUser;
    private final String SHARED_PREFERENCE_KEY = "dacnmurkoff";
    private String contentType = "application/x-www-form-urlencoded";

    private Map<String, String> headers;


    /*Thiết lập và trả về một HTTP request header khi ứng dụng gửi yêu cầu HTTP đến máy chủ
    * các Type được dùng để phân biệt giữa Doctor và Patient */
    public Map<String, String> getHeaders() {

        this.headers = new HashMap<>();
        this.headers.put("Content-Type", contentType );
        this.headers.put("Authorization", accessToken);
        this.headers.put("type", "patient");

        return headers;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getAuthUser() {
        return AuthUser;
    }

    public void setAuthUser(User authUser) {
        AuthUser = authUser;
    }

    public String getSharedReferenceKey() {
        return SHARED_PREFERENCE_KEY;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /*Tùy Chọn Lọc*/
    public List<Option> getFilterOptions()
    {
        List<Option> list = new ArrayList<>();

        //Option option0 = new Option();
        //option0.setIcon(R.drawable.ic_all);
        //option0.setName(getString(R.string.all));

        Option option1 = new Option();
        option1.setIcon(R.drawable.ic_service);
        option1.setName(getString(R.string.service));

        Option option2 = new Option();
        option2.setIcon(R.drawable.ic_speciality);
        option2.setName(getString(R.string.speciality));

        Option option3 = new Option();
        option3.setIcon(R.drawable.ic_doctor);
        option3.setName(getString(R.string.doctor));

        list.add(option1);
        list.add(option2);
        list.add(option3);

        return list;
    }

}
