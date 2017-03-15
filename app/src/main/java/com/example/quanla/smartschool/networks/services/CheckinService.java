package com.example.quanla.smartschool.networks.services;

import com.example.quanla.smartschool.database.model.Checkin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by tranh on 3/11/2017.
 */

public interface CheckinService {
    @GET("checkin")
    Call<List<Checkin>> getListCheckin();
    @POST("checkin")
    Call<Checkin> addNewCheckin(@Body Checkin checkin);

}
