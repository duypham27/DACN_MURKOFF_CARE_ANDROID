package com.example.dacn_murkoff_care_android.DoctorPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.DoctorReadByID;
import com.example.dacn_murkoff_care_android.Repository.DoctorRepository;

import java.util.Map;

public class DoctorPageViewModel extends ViewModel {

    private MutableLiveData<DoctorReadByID> response;
    private MutableLiveData<Boolean> animation;
    private DoctorRepository repository;

    public MutableLiveData<DoctorReadByID> getResponse() {
        if( response == null)
        {
            response = new MutableLiveData<>();
        }
        return response;
    }

    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    /** CREATE REPOSITORY */
    public void instantiate()
    {
        if(repository == null)
        {
            repository = new DoctorRepository();
        }
    }

    /** READ BY ID **/
    public void readById(Map<String, String> headers, String id)
    {
        response = repository.readById(headers, id);
        animation = repository.getAnimation();
    }

}
