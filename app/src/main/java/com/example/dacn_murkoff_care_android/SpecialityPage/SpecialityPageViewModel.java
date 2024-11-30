package com.example.dacn_murkoff_care_android.SpecialityPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_care_android.Container.SpecialityReadByID;
import com.example.dacn_murkoff_care_android.Repository.DoctorRepository;
import com.example.dacn_murkoff_care_android.Repository.SpecialityRepository;

import java.util.Map;

public class SpecialityPageViewModel extends ViewModel {

    private MutableLiveData<SpecialityReadByID> specialityReadByIdResponse;
    private MutableLiveData<DoctorReadAll> doctorReadAllResponse;
    private MutableLiveData<Boolean> animation;

    private SpecialityRepository specialityRepository;
    private DoctorRepository doctorRepository;


    public MutableLiveData<SpecialityReadByID> getSpecialityReadByIdResponse() {
        if( specialityReadByIdResponse == null)
        {
            specialityReadByIdResponse = new MutableLiveData<>();
        }
        return specialityReadByIdResponse;
    }


    public MutableLiveData<DoctorReadAll> getDoctorReadAllResponse()
    {
        if( doctorReadAllResponse == null)
        {
            doctorReadAllResponse = new MutableLiveData<>();
        }
        return doctorReadAllResponse;
    }

    public MutableLiveData<Boolean> getAnimation() {
        return animation;
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
    }


    public void specialityReadById(Map<String, String> headers, String id)
    {
        specialityReadByIdResponse = specialityRepository.readById(headers, id);
        animation = specialityRepository.getAnimation();
    }

    public void doctorReadAll(Map<String, String> headers, Map<String, String> parameters)
    {
        doctorReadAllResponse = doctorRepository.readAll(headers, parameters);
    }

}
