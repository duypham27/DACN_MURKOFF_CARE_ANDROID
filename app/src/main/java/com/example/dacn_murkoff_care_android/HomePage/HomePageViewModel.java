package com.example.dacn_murkoff_care_android.HomePage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_care_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_care_android.Repository.DoctorRepository;
import com.example.dacn_murkoff_care_android.Repository.SpecialityRepository;

import java.util.Map;

public class HomePageViewModel extends ViewModel {
    private MutableLiveData<Boolean> animation;
    private MutableLiveData<SpecialityReadAll> specialityReadAllResponse;
    private MutableLiveData<DoctorReadAll> doctorReadAllResponse;

    private SpecialityRepository specialityRepository;
    private DoctorRepository doctorRepository;


    /*  MutableLiveData<Boolean> getAnimation theo dõi trạng thái hoạt động của app */
    public MutableLiveData<Boolean> getAnimation() {
        if( animation == null )
        {
            animation = new MutableLiveData<>();
        }
        return animation;
    }

    /* Create Speciality Repository */
    public void instantiate()
    {
        if(specialityRepository == null)
        {
            specialityRepository = new SpecialityRepository();
        }
        if(doctorRepository == null)
        {
            doctorRepository = new DoctorRepository();
        }
    }


    /** SPECIALITY **/
    /* Lấy dữ liệu của tất cả SPECIALITY(chuyên ngành) */
    public MutableLiveData<SpecialityReadAll> getSpecialityReadAllResponse() {
        if( specialityReadAllResponse == null )
        {
            specialityReadAllResponse = new MutableLiveData<>();
        }
        return specialityReadAllResponse;
    }

    /* Gửi một HTTP request để lấy tất cả dữ liệu về SPECIALITY(chuyên ngành) từ server */
    public void specialityReadAll(Map<String, String> headers, Map<String, String> parameters)
    {
        specialityReadAllResponse = specialityRepository.readAll(headers, parameters);
        animation = specialityRepository.getAnimation();
    }

    /** DOCTOR **/
    /* Lấy dữ liệu của tất cả DOCTOR(bác sĩ) */
    public MutableLiveData<DoctorReadAll> getDoctorReadAllResponse() {
        if( doctorReadAllResponse == null )
        {
            doctorReadAllResponse = new MutableLiveData<>();
        }
        return doctorReadAllResponse;
    }

    /* Gửi một HTTP request để lấy tất cả dữ liệu về DOCTOR(bác sĩ) từ server */
    public void doctorReadAll(Map<String, String> headers, Map<String, String> parameters)
    {
        doctorReadAllResponse = doctorRepository.readAll(headers, parameters);
        animation = doctorRepository.getAnimation();
    }

}
