package com.test.nss.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @POST("/api/token/login")
    @FormUrlEncoded
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/diary/api/campdetails")
    Call<ResponseBody> getCampDetails(@Header("Authorization") String token);

    @GET("/diary/api/helpData/?format=json")
    Call<ResponseBody> getHelpData(@Header("Authorization") String token);

    @POST("/diary/api/selfRegistration/")
    @FormUrlEncoded
    Call<ResponseBody> signup(
            @Header("Authorization") String token,
            @Field("First_name") String f_name,
            @Field("Father_name") String fath_name,
            @Field("Mother_name") String mom_name,
            @Field("Last_name") String last_name,
            @Field("DateOfRegistration") String vec,
            @Field("Email") String email,
            @Field("college_name") String college_name
    );

    @GET("/diary/api/collegenames/")
    Call<ResponseBody> getClgList(@Header("Authorization") String token);
}
