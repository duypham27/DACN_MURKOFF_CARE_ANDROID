package com.example.dacn_murkoff_android.Configuration;

public class Constant {

    public static String APP_NAME()
    {
        return "Murkoff Care";
    }

    public static String accessToken;
    public static void setAccessToken(String accessToken)
    {
        Constant.accessToken = accessToken;
    }
    public static String getAccessToken()
    {
        return Constant.accessToken;
    }

    public static String UPLOAD_URI()
    {
        return "http://localhost/DACN_API/api/assets/uploads/";
    }

    public static String APP_PATH()
    {
        return "http://localhost/DACN_API/";
    }
}
