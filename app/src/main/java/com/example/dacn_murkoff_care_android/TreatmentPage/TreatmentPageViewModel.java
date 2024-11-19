package com.example.dacn_murkoff_care_android.TreatmentPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.AppointmentReadAll;
import com.example.dacn_murkoff_care_android.Container.TreatmentReadAll;
import com.example.dacn_murkoff_care_android.Container.TreatmentReadByID;
import com.example.dacn_murkoff_care_android.Repository.AppointmentRepository;
import com.example.dacn_murkoff_care_android.Repository.TreatmentRepository;

import java.util.Map;

public class TreatmentPageViewModel extends ViewModel {
    private AppointmentRepository appointmentRepository;
    private TreatmentRepository treatmentRepository;
    public void instantiate()
    {
        if( treatmentRepository == null)
        {
            treatmentRepository = new TreatmentRepository();
        }
        if( appointmentRepository == null)
        {
            appointmentRepository = new AppointmentRepository();
        }
    }

    private MutableLiveData<Boolean> animation = new MutableLiveData<>();
    public MutableLiveData<Boolean> getAnimation(){
        return animation;
    }

    /** APPOINTMENT - READ ALL **/
    private MutableLiveData<AppointmentReadAll> appointmentReadAllResponse = new MutableLiveData<>();
    public MutableLiveData<AppointmentReadAll> getAppointmentReadAllResponse()
    {
        return appointmentReadAllResponse;
    }
    public void appointmentReadAll(Map<String, String> header, Map<String, String> parameters)
    {
        animation = appointmentRepository.getAnimation();
        appointmentReadAllResponse = appointmentRepository.readAll(header, parameters);
    }

    /** TREATMENT - READ ALL OF AN APPOINTMENT **/
    private MutableLiveData<TreatmentReadAll> treatmentReadAllResponse = new MutableLiveData<>();
    public MutableLiveData<TreatmentReadAll> getTreatmentReadAllResponse() {
        return treatmentReadAllResponse;
    }
    public void treatmentReadAll(Map<String, String> header, String appointmentId)
    {
        animation = treatmentRepository.getAnimation();
        treatmentReadAllResponse = treatmentRepository.readAll(header, appointmentId);
    }

    /** TREATMENT - READ BY ID OF A TREATMENT FROM AN APPOINTMENT **/
    private MutableLiveData<TreatmentReadByID> treatmentReadByIDResponse = new MutableLiveData<>();
    public MutableLiveData<TreatmentReadByID> getTreatmentReadByIDResponse() {
        return treatmentReadByIDResponse;
    }
    public void treatmentReadByID(Map<String, String> header, String treatmentId)
    {
        animation = treatmentRepository.getAnimation();
        treatmentReadByIDResponse = treatmentRepository.readByID(header, treatmentId);
    }

}
