package com.example.quanla.smartschool.networks.services;

import com.example.quanla.smartschool.networks.jsonModels.ResponseBody;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by QuanLA on 3/10/2017.
 */

public interface UserService {
    @POST("login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @POST("register")
    Call<ResponseBody> register(@Body RequestBody requestBody);


}
