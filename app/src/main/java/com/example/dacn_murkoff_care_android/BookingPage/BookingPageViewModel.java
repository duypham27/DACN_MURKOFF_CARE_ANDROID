package com.example.dacn_murkoff_care_android.BookingPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacn_murkoff_care_android.Container.BookingPhotoReadAll;
import com.example.dacn_murkoff_care_android.Container.BookingReadByID;
import com.example.dacn_murkoff_care_android.Container.DoctorReadByID;
import com.example.dacn_murkoff_care_android.Container.ServiceReadByID;
import com.example.dacn_murkoff_care_android.Repository.BookingPhotoRepository;
import com.example.dacn_murkoff_care_android.Repository.BookingRepository;
import com.example.dacn_murkoff_care_android.Repository.DoctorRepository;
import com.example.dacn_murkoff_care_android.Repository.ServiceRepository;

import java.util.Map;

public class BookingPageViewModel extends ViewModel {

    private MutableLiveData<ServiceReadByID> serviceReadByIdResponse;
    private MutableLiveData<BookingReadByID> bookingReadByIdResponse;

    private MutableLiveData<Boolean> animation;
    private ServiceRepository serviceRepository;
    private BookingRepository bookingRepository;
    private BookingPhotoRepository bookingPhotoRepository;
    private DoctorRepository doctorRepository;

    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }


    /** INSTANTIATE REPOSITORY **/
    public void instantiate()
    {
        if( serviceRepository == null)
        {
            serviceRepository = new ServiceRepository();
        }
        if( bookingRepository == null)
        {
            bookingRepository = new BookingRepository();
        }
        if( bookingPhotoRepository == null)
        {
            bookingPhotoRepository = new BookingPhotoRepository();
        }
        if( doctorRepository == null)
        {
            doctorRepository = new DoctorRepository();
        }
    }


    /** SERVICE READ BY ID **/
    public MutableLiveData<ServiceReadByID> getServiceReadByIdResponse() {
        if( serviceReadByIdResponse == null)
        {
            serviceReadByIdResponse = new MutableLiveData<>();
        }
        return serviceReadByIdResponse;
    }

    public void serviceReadById(Map<String, String> header, String serviceId)
    {
        serviceReadByIdResponse = serviceRepository.readByID(header, serviceId);
        animation = serviceRepository.getAnimation();
    }


    /** BOOKING READ BY ID **/
    public MutableLiveData<BookingReadByID> getBookingReadByIdResponse() {
        if( bookingReadByIdResponse == null )
        {
            bookingReadByIdResponse = new MutableLiveData<>();
        }
        return bookingReadByIdResponse;
    }
    public void bookingReadByID(Map<String, String> header, String bookingId)
    {
        bookingReadByIdResponse = bookingRepository.readByID(header, bookingId);
        animation = bookingRepository.getAnimation();
    }


    /** BOOKING PHOTO - READ ALL **/
    private MutableLiveData<BookingPhotoReadAll> bookingPhotoReadAllResponse;
    public MutableLiveData<BookingPhotoReadAll> getBookingPhotoReadAllResponse(){
        if( bookingPhotoReadAllResponse == null )
        {
            bookingPhotoReadAllResponse = new MutableLiveData<>();
        }
        return bookingPhotoReadAllResponse;
    }
    public void bookingPhotoReadAll(Map<String, String> header, String bookingId)
    {
        bookingPhotoReadAllResponse = bookingPhotoRepository.readAll(header, bookingId);
        animation = bookingPhotoRepository.getAnimation();
    }


    /** DOCTOR - READ BY ID **/
    private MutableLiveData<DoctorReadByID> doctorReadById = new MutableLiveData<>();
    public MutableLiveData<DoctorReadByID> getDoctorReadByIdResponse() {
        return doctorReadById;
    }
    public void doctorReadByID(Map<String, String> header, String doctorId)
    {
        animation = doctorRepository.getAnimation();
        doctorReadById = doctorRepository.readById(header, doctorId);
    }
}
