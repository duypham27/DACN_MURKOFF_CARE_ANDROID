package com.example.dacn_murkoff_android;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_android.Configuration.HTTPService;
import com.example.dacn_murkoff_android.Container.PatientProfile;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/** This function only get patient's profile when Access Token exists in application **/
public class MainViewModel extends ViewModel {
    private final String TAG = "MainViewModel";
    private MutableLiveData<PatientProfile> response = new MutableLiveData<>();

    public MutableLiveData<PatientProfile> getResponse() {
        return response;
    }

    /** Headers is the JWT Token which is attached to HTTPS headers **/
    public void readPersonalInformation(Map<String, String> headers){
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        Call<PatientProfile> container = api.readPersonalInformation(headers);


        /*Step 3*/
        container.enqueue(new Callback<PatientProfile>() {
            @Override
            public void onResponse(@NonNull Call<PatientProfile> call, @NonNull Response<PatientProfile> returnedResponse) {
                if(returnedResponse.isSuccessful() )
                {
                    PatientProfile content = returnedResponse.body();
                    response.setValue(content);
//                    System.out.println(TAG);
//                    System.out.println("result: " + content.getResult());
//                    System.out.println("msg: " + content.getMsg());
                }
                if(returnedResponse.errorBody() != null)
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(returnedResponse.errorBody().string());
                        System.out.println( jObjError );
                    }
                    catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                    response.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PatientProfile> call, @NonNull Throwable t) {
                System.out.println(TAG);
                System.out.println(t);
                response.setValue(null);
            }
        });
    }
}
