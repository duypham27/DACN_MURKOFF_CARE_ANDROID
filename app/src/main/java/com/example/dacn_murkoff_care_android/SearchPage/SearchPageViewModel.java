package com.example.dacn_murkoff_care_android.SearchPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_care_android.Container.ServiceReadAll;
import com.example.dacn_murkoff_care_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_care_android.Repository.DoctorRepository;
import com.example.dacn_murkoff_care_android.Repository.ServiceRepository;
import com.example.dacn_murkoff_care_android.Repository.SpecialityRepository;

import java.util.Map;

public class SearchPageViewModel extends ViewModel {

    private MutableLiveData<SpecialityReadAll> specialityReadAll = new MutableLiveData<>();
    private MutableLiveData<DoctorReadAll> doctorReadAllResponse = new MutableLiveData<>();
    private MutableLiveData<ServiceReadAll> serviceReadAllResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> animation = new MutableLiveData<>();


    private SpecialityRepository specialityRepository;
    private DoctorRepository doctorRepository;
    private ServiceRepository serviceRepository;


    /** GETTER **/
    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    public MutableLiveData<SpecialityReadAll> getSpecialityReadAll() {
        return specialityReadAll;
    }

    public MutableLiveData<DoctorReadAll> getDoctorReadAllResponse() {
        return doctorReadAllResponse;
    }

    public MutableLiveData<ServiceReadAll> getServiceReadAllResponse() {
        return serviceReadAllResponse;
    }


    /** CREATE REPOSITORY **/
    public void instantiate()
    {
        if(specialityRepository == null)
        {
            specialityRepository = new SpecialityRepository();
        }
        if( doctorRepository == null)
        {
            doctorRepository = new DoctorRepository();
        }
        if( serviceRepository == null )
        {
            serviceRepository = new ServiceRepository();
        }
    }


    /** DOCTOR READ ALL **/
    public void doctorReadAll(Map<String, String> headers, Map<String, String> parameters)
    {
        doctorReadAllResponse = doctorRepository.readAll(headers, parameters);
        animation = doctorRepository.getAnimation();
    }


    /** SPECIALITY READ ALL **/
    public void specialityReadAll(Map<String, String> headers, Map<String, String> parameters)
    {
        specialityReadAll = specialityRepository.readAll(headers, parameters);
        animation = specialityRepository.getAnimation();
    }


    /** SERVICE READ ALL **/
    public void serviceReadAll(Map<String, String> headers, Map<String, String> parameters)
    {
        serviceReadAllResponse = serviceRepository.readAll(headers, parameters);
        animation = serviceRepository.getAnimation();
    }
}
