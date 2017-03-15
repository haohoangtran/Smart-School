package com.example.quanla.smartschool.database.respon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/5/2017.
 */

public class PersionFaceId {

    public String getPersistedfaceid() {
        return persistedfaceid;
    }

    public PersionFaceId(String persistedfaceid) {

        this.persistedfaceid = persistedfaceid;
    }

    @SerializedName("persistedFaceId")
    public String persistedfaceid;
}
