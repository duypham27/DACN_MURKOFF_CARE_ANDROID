package com.example.dacn_murkoff_care_android.RecordPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.RecordReadByID;
import com.example.dacn_murkoff_care_android.Repository.RecordRepository;

import java.util.Map;

public class RecordPageViewModel extends ViewModel {
    private RecordRepository repository;

    public void instantiate()
    {
        if( repository == null)
        {
            repository = new RecordRepository();
        }
    }


    /** ANIMATION **/
    private MutableLiveData<Boolean> animation = new MutableLiveData<>();
    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }


    /** READ BY ID **/
    private MutableLiveData<RecordReadByID> readByIDResponse = new MutableLiveData<>();
    public MutableLiveData<RecordReadByID> getReadByIDResponse() {
        return readByIDResponse;
    }
    public void readByID(Map<String, String> header, String appointmentId)
    {
        animation = repository.getAnimation();
        readByIDResponse = repository.readByID(header, appointmentId);
    }
}
