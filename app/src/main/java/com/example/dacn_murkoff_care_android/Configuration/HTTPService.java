package com.example.dacn_murkoff_care_android.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPService {

    private static final String APP_PATH = Constant.APP_PATH();
    private static Retrofit retrofit;

    // Hàm getInstance() sẽ trả về một instance Retrofit duy nhất
    public static Retrofit getInstance() {
        if (retrofit == null) {
            // Tạo một đối tượng Gson với cấu hình lenient (tùy chọn linh động với dữ liệu JSON)
            Gson gson = new GsonBuilder()
                    .setLenient()  // Tùy chọn này giúp Retrofit bỏ qua một số lỗi khi phân tích cú pháp JSON không chính xác
                    .create();

            // Khởi tạo Retrofit với cấu hình cơ bản
            retrofit = new Retrofit.Builder()
                    .baseUrl(APP_PATH)  // Đặt URL cơ bản của API
                    .addConverterFactory(GsonConverterFactory.create(gson))  // Cấu hình Gson converter để phân tích cú pháp JSON
                    .build();
        }

        return retrofit;  // Trả về instance Retrofit đã khởi tạo
    }

    /** NOTE:
     * Only for Homepage/HomeFragment function
     **/
    public static final String OPEN_WEATHER_MAP = Constant.OPEN_WEATHER_MAP_PATH();
    public static Retrofit getOpenWeatherMapInstance()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_MAP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}
