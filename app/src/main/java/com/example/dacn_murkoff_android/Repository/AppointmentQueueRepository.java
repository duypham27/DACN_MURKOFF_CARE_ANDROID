package com.example.dacn_murkoff_android.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dacn_murkoff_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_android.Configuration.HTTPService;
import com.example.dacn_murkoff_android.Container.AppointmentQueue;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppointmentQueueRepository {
    private final String TAG = "Appointment_Queue_Repository";


    /*ANIMATION*/
    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();

    /*GET APPOINTMENT QUEUE*/
    private final MutableLiveData<AppointmentQueue> appointmentQueue = new MutableLiveData<>();
    public MutableLiveData<AppointmentQueue> getAppointmentQueue(Map<String, String> header, Map<String, String> parameters)
    {
        /*Step 1*/
        animation.setValue(true);


        /*Step 2*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 3*/
        Call<AppointmentQueue> container = api.appointmentQueue(header, parameters);

        /*Step 4*/
        container.enqueue(new Callback<AppointmentQueue>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentQueue> call, @NonNull Response<AppointmentQueue> response) {
                if(response.isSuccessful())
                {
                    AppointmentQueue content = response.body();
                    assert content != null;
                    appointmentQueue.setValue(content);
                    animation.setValue(false);
//                    System.out.println("=================================");
//                    System.out.println(TAG);
//                    System.out.println("result: " + content.getResult());
//                    System.out.println("msg: " + content.getMsg());
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
                    appointmentQueue.setValue(null);
                    animation.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentQueue> call, @NonNull Throwable t) {
                System.out.println("Appointment Repository - Read All - error: " + t.getMessage());
                animation.setValue(false);
            }
        });

        return appointmentQueue;
    }
}
