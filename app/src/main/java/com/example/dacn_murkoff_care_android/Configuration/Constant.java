package com.example.dacn_murkoff_care_android.Configuration;

public class Constant {

    public static String APP_NAME()
    {
        return "MurkOff Care";
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
        return "http://192.168.1.3/DACN_API/api/assets/uploads/";
    }

    public static String APP_PATH()
    {
        return "http://192.168.1.3/DACN_API/";
    }

    public static String APP_PATH_EMULATOR()
    {
        return "http://10.0.2.2:80/DACN_API/";
    }

    public static String VIDEO_PATH()
    {
        return "https://www.youtube.com/watch?v=rnVOM_WJmN8";
    }

    /** NOTE:
     * API KEY FROM https://openweathermap.org/
     * Sử dụng API này để lấy nhiệt độ của TP.HCM
     * https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
     * lat là kinh độ của thành phố
     * lon là vĩ độ của thành phố
     */
    public static String OPEN_WEATHER_MAP_API_KEY()
    {
        return "b4c95b60d0242db70e8636dd1ca11b15";
    }


    public static String OPEN_WEATHER_MAP_PATH()
    {
        return "https://api.openweathermap.org/data/2.5/weather/";
    }

}
