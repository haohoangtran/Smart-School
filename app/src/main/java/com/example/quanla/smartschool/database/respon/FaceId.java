package com.example.quanla.smartschool.database.respon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/11/2017.
 */

public class FaceId {

    @SerializedName("faceId")
    public String faceid;

    public String getFaceid() {
        return faceid;
    }

    public FaceId(String faceid) {

        this.faceid = faceid;
    }

    @Override
    public String toString() {
        return "FaceId{" +
                "faceid='" + faceid + '\'' +
                '}';
    }
}
