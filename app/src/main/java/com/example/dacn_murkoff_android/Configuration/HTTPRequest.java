package com.example.dacn_murkoff_android.Configuration;

import com.example.dacn_murkoff_android.Container.AppointmentQueue;
import com.example.dacn_murkoff_android.Container.AppointmentReadAll;
import com.example.dacn_murkoff_android.Container.AppointmentReadByID;
import com.example.dacn_murkoff_android.Container.BookingCreate;
import com.example.dacn_murkoff_android.Container.BookingReadAll;
import com.example.dacn_murkoff_android.Container.BookingReadByID;
import com.example.dacn_murkoff_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_android.Container.DoctorReadByID;
import com.example.dacn_murkoff_android.Container.Login;
import com.example.dacn_murkoff_android.Container.NotificationCreate;
import com.example.dacn_murkoff_android.Container.NotificationReadAll;
import com.example.dacn_murkoff_android.Container.PatientProfile;
import com.example.dacn_murkoff_android.Container.PatientProfileChangeAvatar;
import com.example.dacn_murkoff_android.Container.PatientProfileChangePersonalInformation;
import com.example.dacn_murkoff_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_android.Container.SpecialityReadByID;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface HTTPRequest {
    /* LOGIN WITH PHONE NUMBER */
    @FormUrlEncoded
    @POST("api/login")
    Call<Login> login(@Field("phone") String phone, @Field("password") String password, @Field("type") String type);


    /* LOGIN WITH GOOGLE ACCOUNT */
    @FormUrlEncoded
    @POST("api/login/google")
    Call<Login> loginWithGoogle(@Field("email") String email, @Field("password") String password, @Field("type") String type);

    /* LOGIN WITH GITHUB ACCOUNT */
    @FormUrlEncoded
    @POST("api/login/github")
    Call<Login> loginWithGithub(@Field("email") String email, @Field("password") String password, @Field("type") String type);



    /* PATIENT PROFILE - GET - READ PERSONAL INFORMATION */
    /* GET */
    @GET("api/patient/profile")
    Call<PatientProfile> readPersonalInformation(@HeaderMap Map<String, String> headers);

    /* POST */
    @FormUrlEncoded
    @POST("api/patient/profile")
    Call<PatientProfileChangePersonalInformation> changePersonalInformation(@HeaderMap Map<String, String> header ,
                                                                            @Field("action") String action,
                                                                            @Field("name") String name,
                                                                            @Field("gender") String gender,
                                                                            @Field("birthday") String birthday,
                                                                            @Field("address") String address);

    @Multipart
    @POST("api/patient/profile")
    Call<PatientProfileChangeAvatar> changeAvatar(@Header("Authorization") String accessToken,
                                                  @Header("Type") String type,
                                                  @Part MultipartBody.Part file,
                                                  @Part("action") RequestBody action);


    /** SPECIALITY **/
    @GET("api/specialities")
    Call<SpecialityReadAll> specialityReadAll(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameters);

    @GET("api/specialities/{id}")
    Call<SpecialityReadByID> specialityReadByID(@HeaderMap Map<String, String> headers, @Path("id") String id);

    /** DOCTOR **/
    @GET("api/doctors")
    Call<DoctorReadAll> doctorReadAll(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameters);

    @GET("api/doctors/{id}")
    Call<DoctorReadByID> doctorReadByID(@HeaderMap Map<String, String> headers, @Path("id") String id);






    /** BOOKING **/
    @FormUrlEncoded
    @POST("api/patient/booking")
    Call<BookingCreate> bookingCreate(@HeaderMap Map<String, String> headers,
                                      @Field("doctor_id") String doctorId,
                                      @Field("service_id") String serviceId,
                                      @Field("booking_name") String bookingName,
                                      @Field("booking_phone") String bookingPhone,
                                      @Field("name") String name,
                                      @Field("gender") String gender,
                                      @Field("address") String address,
                                      @Field("reason") String reason,
                                      @Field("birthday") String birthday,
                                      @Field("appointment_time") String appointmentTime,
                                      @Field("appointment_date") String appointmentDate);

    @GET("api/patient/booking/{id}")
    Call<BookingReadByID> bookingReadByID(@HeaderMap Map <String, String> header, @Path("id") String bookingId);

    @GET("api/patient/booking")
    Call<BookingReadAll> bookingReadAll(@HeaderMap Map<String, String> header, @HeaderMap Map<String, String> parameters);



    /** APPOINTMENTS **/
    @GET("api/patient/appointments")
    Call<AppointmentReadAll> appointmentReadAll(@HeaderMap Map <String, String> header, @QueryMap Map<String, String> parameters);

    @GET("api/patient/appointments/{id}")
    Call<AppointmentReadByID> appointmentReadByID(@HeaderMap Map <String, String> header, @Path("id") String appointmentId);


    /** NOTIFICATION **/
    @GET("api/patient/notifications")
    Call<NotificationReadAll> notificationReadAll(@HeaderMap Map<String, String> header);





    @FormUrlEncoded
    @PUT("api/patient/notifications")
    Call<NotificationCreate> notificationCreate(@HeaderMap Map <String, String> header,
                                                @Field("message") String message,
                                                @Field("record_id") String recordId,
                                                @Field("record_type") String recordType);

    /************** QUEUE **********/
    @GET("api/appointment-queue")
    Call<AppointmentQueue> appointmentQueue(@HeaderMap Map <String, String> header, @QueryMap Map<String, String> parameters);

}
