package com.example.dacn_murkoff_care_android.ServicePage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_care_android.Container.ServiceReadByID;
import com.example.dacn_murkoff_care_android.Repository.DoctorRepository;
import com.example.dacn_murkoff_care_android.Repository.ServiceRepository;

import java.util.Map;

public class ServicePageViewModel extends ViewModel {

    private MutableLiveData<Boolean> animation;
    private MutableLiveData<ServiceReadByID> response;
    private ServiceRepository repository;
    private DoctorRepository doctorRepository;
    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    public MutableLiveData<ServiceReadByID> getResponse() {
        return response;
    }


    /** CREATE REPOSITORY **/
    public void instantiate()
    {
        if(repository == null)
        {
            repository = new ServiceRepository();
        }
        if( doctorRepository == null)
        {
            doctorRepository = new DoctorRepository();
        }
    }

    /** NOTE:
     Header is the header of HTTP request
     ServiceId is the id of service
     **/
    public void readById(Map<String, String> header, String serviceId)
    {
        response = repository.readByID(header, serviceId);
        animation = repository.getAnimation();
    }

    /** DOCTOR - READ ALL **/
    private MutableLiveData<DoctorReadAll> doctorReadAllResponse = new MutableLiveData<>();
    public MutableLiveData<DoctorReadAll> getDoctorReadAllResponse() {
        return doctorReadAllResponse;
    }
    public void doctorReadAll(Map<String, String> header, Map<String, String> parameters)
    {
        doctorReadAllResponse = doctorRepository.readAll(header, parameters);
        animation = doctorRepository.getAnimation();
    }
}
