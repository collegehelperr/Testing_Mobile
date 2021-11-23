package com.co.coller.api;

import com.co.coller.model.note;
import com.co.coller.model.schedule;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @FormUrlEncoded
    @POST("edit_profil.php")
    Call<JsonObject> updateUser(@Field("uid") String uid,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("nama") String nama,
                              @Field("nohp") String nohp,
                              @Field("profimg") String profimg);

    @Multipart
    @POST("upload_profil.php")
    Call<JsonObject> profImg(@Part MultipartBody.Part file,
                             @Part("file") RequestBody name);

    @GET("note.php")
    Call<List<note>> getNote(@Query("uid") String uid);

    @FormUrlEncoded
    @POST("note.php")
    Call<JsonObject> addNote(@Field("uid") String uid,
                               @Field("judul") String judul,
                               @Field("note") String note);

    @FormUrlEncoded
    @POST("note.php")
    Call<JsonObject> updateNote(@Field("id_note") String id_note,
                             @Field("judul") String judul,
                             @Field("note") String note);

    @FormUrlEncoded
    @POST("note.php")
    Call<JsonObject> deleteNote(@Field("id_note_delete") String id_note);

    @GET("schedule.php")
    Call<List<schedule>> getSchedule(@Query("uid") String uid,
                                     @Query("hari") String hari);
}
