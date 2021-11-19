package com.co.coller.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface api {

    @FormUrlEncoded
    @POST("login2.php")
    Call<JsonObject> postLogin(@Field("email") String email,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<JsonObject> register(@Field("email") String email,
                                @Field("password") String password,
                                @Field("nama") String nama,
                                @Field("nohp") String nohp);
}
