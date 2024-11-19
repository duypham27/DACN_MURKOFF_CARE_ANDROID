package com.example.dacn_murkoff_care_android.NotificationPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.NotificationReadAll;
import com.example.dacn_murkoff_care_android.Repository.NotificationRepository;

import java.util.Map;

public class NotificationViewModel extends ViewModel {
    private NotificationRepository repository;
    private MutableLiveData<Boolean> animation = new MutableLiveData<>();
    private MutableLiveData<NotificationReadAll> readAllResponse = new MutableLiveData<>();

    /*ANIMATION*/
    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    public void instantiate()
    {
        if( repository == null)
        {
            repository = new NotificationRepository();
        }
    }

    /*GETTER*/
    public MutableLiveData<NotificationReadAll> getReadAllResponse() {
        return readAllResponse;
    }
    public void readAll(Map<String, String> header)
    {
        animation = repository.getAnimation();
        readAllResponse = repository.readAll(header);
    }

}

