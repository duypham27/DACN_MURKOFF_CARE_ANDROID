package com.example.dacn_murkoff_android.Configuration;

import com.example.dacn_murkoff_android.Container.DoctorReadAll;
import com.example.dacn_murkoff_android.Container.DoctorReadByID;
import com.example.dacn_murkoff_android.Container.Login;
import com.example.dacn_murkoff_android.Container.PatientProfile;
import com.example.dacn_murkoff_android.Container.PatientProfileChangePersonalInformation;
import com.example.dacn_murkoff_android.Container.SpecialityReadAll;
import com.example.dacn_murkoff_android.Container.SpecialityReadByID;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
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

}
