package com.example.dacn_murkoff_care_android.Configuration;

import com.example.dacn_murkoff_care_android.Container.AppointmentQueue;
import com.example.dacn_murkoff_care_android.Container.AppointmentReadAll;
import com.example.dacn_murkoff_care_android.Container.AppointmentReadByID;
import com.example.dacn_murkoff_care_android.Container.BookingCancel;
import com.example.dacn_murkoff_care_android.Container.BookingCreate;
import com.example.dacn_murkoff_care_android.Container.BookingPhotoDelete;
import com.example.dacn_murkoff_care_android.Container.BookingPhotoReadAll;
import com.example.dacn_murkoff_care_android.Container.BookingPhotoUpload;
import com.example.dacn_murkoff_care_android.Container.BookingReadAll;
import com.example.dacn_murkoff_care_android.Container.BookingReadByID;
import com.example.dacn_murkoff_care_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_care_android.Container.DoctorReadByID;
import com.example.dacn_murkoff_care_android.Container.Login;
import com.example.dacn_murkoff_care_android.Container.NotificationCreate;
import com.example.dacn_murkoff_care_android.Container.NotificationMarkAllAsRead;
import com.example.dacn_murkoff_care_android.Container.NotificationMarkAsRead;
import com.example.dacn_murkoff_care_android.Container.NotificationReadAll;
import com.example.dacn_murkoff_care_android.Container.PatientProfile;
import com.example.dacn_murkoff_care_android.Container.PatientProfileChangeAvatar;
import com.example.dacn_murkoff_care_android.Container.PatientProfileChangePersonalInformation;
import com.example.dacn_murkoff_care_android.Container.RecordReadByID;
import com.example.dacn_murkoff_care_android.Container.ServiceReadAll;
import com.example.dacn_murkoff_care_android.Container.ServiceReadByID;
import com.example.dacn_murkoff_care_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_care_android.Container.SpecialityReadByID;
import com.example.dacn_murkoff_care_android.Container.TreatmentReadAll;
import com.example.dacn_murkoff_care_android.Container.TreatmentReadByID;
import com.example.dacn_murkoff_care_android.Container.WeatherForecast;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
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
    /** LOGIN WITH PHONE NUMBER **/
    @FormUrlEncoded
    @POST("login")
    Call<Login> login(@Field("phone") String phone, @Field("password") String password, @Field("type") String type);


    /** LOGIN WITH GOOGLE ACCOUNT **/
    @FormUrlEncoded
    @POST("login/google")
    Call<Login> loginWithGoogle(@Field("email") String email, @Field("password") String password, @Field("type") String type);

    /** LOGIN WITH GITHUB ACCOUNT **/
    @FormUrlEncoded
    @POST("api/login/github")
    Call<Login> loginWithGithub(@Field("email") String email, @Field("password") String password, @Field("type") String type);



    /** PATIENT PROFILE - GET - READ PERSONAL INFORMATION **/
    /* GET */
    @GET("patient/profile")
    Call<PatientProfile> readPersonalInformation(@HeaderMap Map<String, String> headers);

    /* POST */
    @FormUrlEncoded
    @POST("patient/profile")
    Call<PatientProfileChangePersonalInformation> changePersonalInformation(@HeaderMap Map<String, String> header ,
                                                                            @Field("action") String action,
                                                                            @Field("name") String name,
                                                                            @Field("gender") String gender,
                                                                            @Field("birthday") String birthday,
                                                                            @Field("address") String address);

    @Multipart
    @POST("patient/profile")
    Call<PatientProfileChangeAvatar> changeAvatar(@Header("Authorization") String accessToken,
                                                  @Header("Type") String type,
                                                  @Part MultipartBody.Part file,
                                                  @Part("action") RequestBody action);


    /** SPECIALITY **/
    @GET("specialities")
    Call<SpecialityReadAll> specialityReadAll(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameters);

    @GET("specialities/{id}")
    Call<SpecialityReadByID> specialityReadByID(@HeaderMap Map<String, String> headers, @Path("id") String id);

    /** DOCTOR **/
    @GET("doctors")
    Call<DoctorReadAll> doctorReadAll(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameters);

    @GET("doctors/{id}")
    Call<DoctorReadByID> doctorReadByID(@HeaderMap Map<String, String> headers, @Path("id") String id);


    /** SERVICE **/
    @GET("services")
    Call<ServiceReadAll> serviceReadAll(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> parameters);

    @GET("services/{id}")
    Call<ServiceReadByID> serviceReadByID(@HeaderMap Map<String, String> headers, @Path("id") String id);



    /** BOOKING **/
    @FormUrlEncoded
    @POST("patient/booking")
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

    @GET("patient/booking/{id}")
    Call<BookingReadByID> bookingReadByID(@HeaderMap Map <String, String> header, @Path("id") String bookingId);

    @DELETE("patient/booking/{id}")
    Call<BookingCancel> bookingCancel(@HeaderMap Map <String, String> header, @Path("id") String bookingId);

    @GET("patient/booking")
    Call<BookingReadAll> bookingReadAll(@HeaderMap Map<String, String> header, @HeaderMap Map<String, String> parameters);


    /** BOOKING PHOTO **/
    @GET("booking/photos/{id}")
    Call<BookingPhotoReadAll> bookingPhotoReadAll(@HeaderMap Map<String, String> headers, @Path("id") String id);


    @Multipart
    @POST("booking/upload-photo")
    Call<BookingPhotoUpload> bookingPhotoUpload(@Header("Authorization") String accessToken,
                                                @Header("Type") String type,
                                                @Part("booking_id") RequestBody bookingId,
                                                @Part MultipartBody.Part file);


    @DELETE("booking/photo/{id}")
    Call<BookingPhotoDelete> bookingPhotoDelete(@HeaderMap Map<String, String> header, @Path("id") int id);


    /** APPOINTMENTS **/
    @GET("patient/appointments")
    Call<AppointmentReadAll> appointmentReadAll(@HeaderMap Map <String, String> header, @QueryMap Map<String, String> parameters);


    @GET("patient/appointments/{id}")
    Call<AppointmentReadByID> appointmentReadByID(@HeaderMap Map <String, String> header, @Path("id") String appointmentId);



    /** NOTIFICATION **/
    @GET("patient/notifications")
    Call<NotificationReadAll> notificationReadAll(@HeaderMap Map<String, String> header);


    @POST("patient/notifications/mark-as-read/{id}")
    Call<NotificationMarkAsRead> notificationMarkAsRead(@HeaderMap Map<String, String> header, @Path("id") String notificationId);


    @POST("patient/notifications")
    Call<NotificationMarkAllAsRead> notificationMarkAllAsRead(@HeaderMap Map <String, String> header);


    @FormUrlEncoded
    @PUT("patient/notifications")
    Call<NotificationCreate> notificationCreate(@HeaderMap Map <String, String> header,
                                                @Field("message") String message,
                                                @Field("record_id") String recordId,
                                                @Field("record_type") String recordType);

    /** QUEUE **/
    @GET("appointment-queue")
    Call<AppointmentQueue> appointmentQueue(@HeaderMap Map <String, String> header, @QueryMap Map<String, String> parameters);


    /** RECORD **/
    @GET("patient/appointments/records/{id}")
    Call<RecordReadByID> recordReadById(@HeaderMap Map <String, String> header, @Path("id") String recordId);

    /** TREATMENT **/
    @GET("patient/treatments/{id}")
    Call<TreatmentReadAll> treatmentReadAll(@HeaderMap Map <String, String> header, @Path("id") String appointmentId);

    @GET("patient/treatment/{id}")
    Call<TreatmentReadByID> treatmentReadByID(@HeaderMap Map <String, String> header, @Path("id") String treatmentId);


    /** WEATHER FORCASE - OPEN WEATHER MAP.ORG **/
    @GET("https://api.openweathermap.org/data/2.5/weather")
    Call<WeatherForecast> getCurrentWeather(@QueryMap Map<String, String> parameters);

}
