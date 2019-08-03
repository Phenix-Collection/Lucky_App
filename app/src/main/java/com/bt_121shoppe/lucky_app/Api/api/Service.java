package com.bt_121shoppe.lucky_app.Api.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
//    @GET("/allposts/?page=")
//    Call<AllResponse> getAllPost(@Query("page") String page);
//    @GET("/bestdeal/")
//    Call<AllResponse> getBestdeal();
//    @GET("/countview/?post=")
//    Call<AllResponse> getCount(@Query("post") String post, @Header("Authorization") String authorization);
    @GET("/api/v1/users/{id}")
    Call<AllResponse> getUser(@Path("id") String id, @Header("Authorization") String authorization);
    @GET("/api/v1/users/{id}")
    Call<User.Profile> getProfile(@Path("id") String id, @Header("Authorization") String authorization);
//    @GET("/postbyuser/?status=")
//    Call<AllResponse> getPost_Active(@Query("id") String id, @Header("Authorization") String authorization);
//    @GET("user")
//    Call<AllResponse> getUser(@Header("Authorization") String authorization);
}
