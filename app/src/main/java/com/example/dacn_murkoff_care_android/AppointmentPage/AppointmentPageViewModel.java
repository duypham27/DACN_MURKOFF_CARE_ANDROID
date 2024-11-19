package com.example.dacn_murkoff_care_android.AppointmentPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.AppointmentQueue;
import com.example.dacn_murkoff_care_android.Container.AppointmentReadAll;
import com.example.dacn_murkoff_care_android.Container.AppointmentReadByID;
import com.example.dacn_murkoff_care_android.Repository.AppointmentQueueRepository;
import com.example.dacn_murkoff_care_android.Repository.AppointmentRepository;

import java.util.Map;

public class AppointmentPageViewModel extends ViewModel {

    private MutableLiveData<Boolean> animation = new MutableLiveData<>();
    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    private AppointmentRepository repository;
    private AppointmentQueueRepository queueRepository;
    public void instantiate()
    {
        if( repository == null)
        {
            repository = new AppointmentRepository();
        }
        if( queueRepository == null)
        {
            queueRepository = new AppointmentQueueRepository();
        }
    }

    /** APPOINTMENTS - READ ALL **/
    private MutableLiveData<AppointmentReadAll> readAllResponse = new MutableLiveData<>();
    public MutableLiveData<AppointmentReadAll> getReadAllResponse() {
        return readAllResponse;
    }
    public void readAll(Map<String, String> header, Map<String, String> parameters)
    {
        animation = repository.getAnimation();
        readAllResponse = repository.readAll(header, parameters);
    }

    /** APPOINTMENTS - READ BY ID **/
    private MutableLiveData<AppointmentReadByID> readByIDResponse = new MutableLiveData<>();
    public MutableLiveData<AppointmentReadByID> getReadByIDResponse(){return readByIDResponse;}
    public void readByID(Map<String, String> header, String appointmentID)
    {
        //animation = repository.getAnimation();
        readByIDResponse = repository.readByID(header, appointmentID);
    }

    /** QUEUE - READ BY ID **/
    private MutableLiveData<AppointmentQueue> appointmentQueueResponse = new MutableLiveData<>();
    public MutableLiveData<AppointmentQueue> getAppointmentQueueResponse(){ return appointmentQueueResponse;}
    public void getQueue(Map<String, String> header, Map<String, String> parameter)
    {
//        animation = repository.getAnimation();
        appointmentQueueResponse = queueRepository.getAppointmentQueue(header, parameter);
    }

}
