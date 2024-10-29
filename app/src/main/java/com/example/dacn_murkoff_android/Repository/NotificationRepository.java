package com.example.dacn_murkoff_android.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dacn_murkoff_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_android.Configuration.HTTPService;
import com.example.dacn_murkoff_android.Container.NotificationReadAll;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationRepository {
    private final String TAG = "Notification_Repository";

    /*ANIMATION*/
    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();
    public MutableLiveData<Boolean> getAnimation()
    {
        return animation;
    }

    /** READ ALL **/

    /*GETTER*/
    private final MutableLiveData<NotificationReadAll> readAllResponse = new MutableLiveData<>();
    public MutableLiveData<NotificationReadAll> getReadAllResponse()
    {
        return readAllResponse;
    }
    /*FUNCTION*/
    public MutableLiveData<NotificationReadAll> readAll(Map<String, String> header)
    {
        /*Step 1*/
        animation.setValue(true);


        /*Step 2*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 3*/
        Call<NotificationReadAll> container = api.notificationReadAll(header);

        /*Step 4*/
        container.enqueue(new Callback<NotificationReadAll>() {
            @Override
            public void onResponse(@NonNull Call<NotificationReadAll> call, @NonNull Response<NotificationReadAll> response) {
                if(response.isSuccessful())
                {
                    NotificationReadAll content = response.body();
                    assert content != null;
                    readAllResponse.setValue(content);
//                    System.out.println(TAG);
//                    System.out.println("result: " + content.getResult());
//                    System.out.println("msg: " + content.getMsg());
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
            public void onFailure(@NonNull Call<NotificationReadAll> call, @NonNull Throwable t) {
                System.out.println("Notification Repository - Read All - error: " + t.getMessage());
                //readAllResponse.setValue(null);
                animation.setValue(false);
            }
        });

        return readAllResponse;
    }
}
