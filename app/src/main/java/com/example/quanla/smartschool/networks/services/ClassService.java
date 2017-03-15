package com.example.quanla.smartschool.networks.services;

import com.example.quanla.smartschool.database.model.ClassStudent;
import com.example.quanla.smartschool.database.model.StudentRespon;
import com.example.quanla.smartschool.networks.jsonModels.AddNewGroupBody;
import com.example.quanla.smartschool.networks.jsonModels.GetPersionGroupResponJSON;
import com.example.quanla.smartschool.networks.jsonModels.GetPersonGroupTrainingStatusJSON;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by tranh on 3/11/2017.
 */

public interface ClassService {
    @PUT("persongroups/{personGroupId}")
    Call<String> addNewGroupFace(@Path("personGroupId") String personGroupId, @Body AddNewGroupBody addNewGroupBody);

    @DELETE("persongroups/{personGroupId}")
    Call<String> deleteGroupFace(@Path("personGroupId") String personGroupId);

    @GET("persongroups/{personGroupId}")
    Call<GetPersionGroupResponJSON> getGroupFace(@Path("personGroupId") String personGroupId);

    @GET("persongroups/{personGroupId}/training")
    Call<GetPersonGroupTrainingStatusJSON> getPersonGroupTrainingStatus(@Path("personGroupId") String personGroupId);

    @POST("persongroups/{personGroupId}/train")
    Call<Void> trainAI(@Path("personGroupId") String personGroupId);

    @GET("persongroups")
    Call<List<ClassStudent>> getAllGroup();
}
