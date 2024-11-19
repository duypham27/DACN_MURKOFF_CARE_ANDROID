package com.example.dacn_murkoff_care_android.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dacn_murkoff_care_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_care_android.Configuration.HTTPService;
import com.example.dacn_murkoff_care_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_care_android.Container.DoctorReadByID;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoctorRepository {

    private final String TAG = "DoctorRepository";

    /*ANIMATION*/
    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();
    public MutableLiveData<Boolean> getAnimation()
    {
        return animation;
    }


    /** READ ALL (lấy toàn bộ liên quan đến Doctor) **/
    /*GETTER*/
    private final MutableLiveData<DoctorReadAll> readAllResponse = new MutableLiveData<>();
    public MutableLiveData<DoctorReadAll> getReadAllResponse()
    {
        return readAllResponse;
    }
    /*FUNCTION*/
    public MutableLiveData<DoctorReadAll> readAll(Map<String, String> headers,
                                                  Map<String,String> parameters)
    {
        /* Step 1: Bắt đầu animation */
        animation.setValue(true);


        /* Step 2: Tạo một đối tượng Retrofit -> để chuẩn bị gọi API */
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /* Step 3: Gọi API để lấy tất cả các bản ghi */
        Call<DoctorReadAll> container = api.doctorReadAll(headers, parameters);

        /* Step 4: Xử lý kết quả DoctorReadAll từ server */
        container.enqueue(new Callback<DoctorReadAll>() {
            @Override
            public void onResponse(@NonNull Call<DoctorReadAll> call, @NonNull Response<DoctorReadAll> response) {
                if(response.isSuccessful())
                {
                    DoctorReadAll content = response.body();
                    assert content != null;
                    readAllResponse.setValue(content);
                    animation.setValue(false);
                }
                if(response.errorBody() != null)
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    }
                    catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                    readAllResponse.setValue(null);
                    animation.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DoctorReadAll> call, @NonNull Throwable t) {
                System.out.println("Doctor Repository - Read All - error: " + t.getMessage());
                //readAllResponse.setValue(null);
                animation.setValue(false);
            }
        });

        return readAllResponse;
    }


    /** READ BY ID (lấy dữ liên quan đến Doctor nhưng lấy theo ID) **/
    /*GETTER*/
    private MutableLiveData<DoctorReadByID> readByIdResponse = new MutableLiveData<>();
    public MutableLiveData<DoctorReadByID> getReadByIdResponse()
    {
        return readByIdResponse;
    }
    /*FUNCTION*/
    public MutableLiveData<DoctorReadByID> readById(Map<String, String> headers,
                                                    String doctorId)
    {
        /* Step 1: Bắt đầu animation */
        animation.setValue(true);


        /* Step 2: Tạo một đối tượng Retrofit -> để chuẩn bị gọi API */
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /* Step 3: Gọi API để lấy tất cả các bản ghi */
        Call<DoctorReadByID> container = api.doctorReadByID(headers, doctorId);

        /* Step 4: Xử lý kết quả DoctorReadByID từ server */
        container.enqueue(new Callback<DoctorReadByID>() {
            @Override
            public void onResponse(@NonNull Call<DoctorReadByID> call, @NonNull Response<DoctorReadByID> response) {
                if(response.isSuccessful())
                {
                    DoctorReadByID content = response.body();
                    assert content != null;
                    readByIdResponse.setValue(content);
                    animation.setValue(false);
//                    System.out.println(TAG);
//                    System.out.println("result: " + content.getResult());
//                    System.out.println("msg: " + content.getMsg());
                }
                if(response.errorBody() != null)
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    }
                    catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                    readByIdResponse.setValue(null);
                    animation.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DoctorReadByID> call, @NonNull Throwable t) {
                System.out.println("Doctor Repository - Read By ID - error: " + t.getMessage());
                //readAllResponse.setValue(null);
                animation.setValue(false);
            }
        });

        return readByIdResponse;
    }

}
