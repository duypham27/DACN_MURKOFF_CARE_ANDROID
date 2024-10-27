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
        return "http://192.168.1.221:8080/DACN_MURKOFF_CARE/api/assets/uploads/";
    }

    public static String APP_PATH()
    {
        return "http://192.168.1.221:8080/DACN_MURKOFF_CARE/";
    }
}
