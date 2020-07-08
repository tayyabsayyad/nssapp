package com.test.nss.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @POST("/api/token/login/?format=json")
    @FormUrlEncoded
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/diary/api/dailyactivity")
    Call<ResponseBody> getAct(@Header("Authorization") String token);
}
