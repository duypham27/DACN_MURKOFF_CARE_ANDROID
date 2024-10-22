package com.example.dacn_murkoff_android.HomePage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_android.Repository.DoctorRepository;
import com.example.dacn_murkoff_android.Repository.SpecialityRepository;

public class HomePageViewModel extends ViewModel {
    private MutableLiveData<Boolean> animation;
    private MutableLiveData<SpecialityReadAll> specialityReadAllResponse;
    private MutableLiveData<DoctorReadAll> doctorReadAllResponse;

    private SpecialityRepository specialityRepository;
    private DoctorRepository doctorRepository;
}
