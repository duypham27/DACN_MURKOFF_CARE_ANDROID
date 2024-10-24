package com.example.dacn_murkoff_android.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dacn_murkoff_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_android.Configuration.HTTPService;
import com.example.dacn_murkoff_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_android.Container.SpecialityReadByID;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SpecialityRepository {
    private final String TAG = "SpecialityRepository";

    /*ANIMATION*/
    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();
    public MutableLiveData<Boolean> getAnimation()
    {
        return animation;
    }


    /** READ ALL (lấy toàn bộ liên quan đến Speciality) **/

    /*GETTER*/
    private final MutableLiveData<SpecialityReadAll> readAllResponse = new MutableLiveData<>();
    /*FUNCTION*/
    public MutableLiveData<SpecialityReadAll> readAll(Map<String, String> headers,
                                                      Map<String,String> parameters)
    {
        /* Step 1: Bắt đầu animation */
        animation.setValue(true);


        /* Step 2: Tạo một đối tượng Retrofit -> để chuẩn bị gọi API */
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /* Step 3: Gọi API để lấy tất cả các bản ghi */
        Call<SpecialityReadAll> container = api.specialityReadAll(headers, parameters);


        /* Step 4: Xử lý kết quả SpecialityReadAll từ server */
        container.enqueue(new Callback<SpecialityReadAll>() {
            @Override
            public void onResponse(@NonNull Call<SpecialityReadAll> call, @NonNull Response<SpecialityReadAll> response) {
                if(response.isSuccessful())
                {
                    SpecialityReadAll content = response.body();
                    assert content != null;
                    readAllResponse.setValue(content);
                    animation.setValue(false);
//                    System.out.println(TAG);
//                    System.out.println("result: " + content.getResult());
//                    System.out.println("quantity: " + content.getQuantity());
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
            public void onFailure(@NonNull Call<SpecialityReadAll> call, @NonNull Throwable t) {
                System.out.println("Speciality Repository - Read All - error: " + t.getMessage());
                readAllResponse.setValue(null);
                animation.setValue(false);
            }
        });

        return readAllResponse;
    }


    /** READ BY ID (lấy dữ liên quan đến Speciality nhưng lấy theo ID) **/

    /*GETTER*/
    private final MutableLiveData<SpecialityReadByID> readByID = new MutableLiveData<>();
    /*FUNCTION*/
    public MutableLiveData<SpecialityReadByID> readById(Map<String, String> headers,
                                                        String specialityId)
    {
        /* Step 1: Bắt đầu animation */
        animation.setValue(true);


        /* Step 2: Tạo một đối tượng Retrofit -> để chuẩn bị gọi API */
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /* Step 3: Gọi API để lấy tất cả các bản ghi */
        Call<SpecialityReadByID> container = api.specialityReadByID(headers, specialityId);


        /* Step 4: Xử lý kết quả SpecialityReadByID từ server */
        container.enqueue(new Callback<SpecialityReadByID>() {
            @Override
            public void onResponse(@NonNull Call<SpecialityReadByID> call, @NonNull Response<SpecialityReadByID> response) {
                if(response.isSuccessful())
                {
                    SpecialityReadByID content = response.body();
                    assert content != null;
                    readByID.setValue(content);
                    animation.setValue(false);
//                    System.out.println(TAG);
//                    System.out.println("result: " + content.getResult());
//                    System.out.println("quantity: " + content.getQuantity());
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
                    readByID.setValue(null);
                    animation.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpecialityReadByID> call, @NonNull Throwable t) {
                System.out.println("Speciality Repository - Read By ID - error: " + t.getMessage());
                readByID.setValue(null);
                animation.setValue(false);
            }
        });

        return readByID;
    }

}
