package com.example.quanla.smartschool.networks.services;

import com.example.quanla.smartschool.database.request.UrlImage;
import com.example.quanla.smartschool.database.respon.FaceId;
import com.example.quanla.smartschool.database.respon.IndentifyRespon;
import com.example.quanla.smartschool.database.respon.VerifyRespon;
import com.example.quanla.smartschool.networks.jsonModels.IndentifyBody;
import com.example.quanla.smartschool.networks.jsonModels.VerifyBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tranh on 3/11/2017.
 */

public interface FaceService {
    @POST("detect")
    Call<List<FaceId>> detectFace(@Body UrlImage url);
    @POST("identify")
    Call<List<IndentifyRespon>> identifyFace(@Body IndentifyBody indentifyBody);
    @POST("verify")
    Call<VerifyRespon> verifyFace(@Body VerifyBody verifyBody);
}
