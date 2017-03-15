package com.example.quanla.smartschool.networks.jsonModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/11/2017.
 */

public class VerifyBody {

    @SerializedName("faceId")
    public String faceid;
    @SerializedName("peronId")
    public String peronid;
    @SerializedName("personGroupId")
    public String persongroupid;

    public String getFaceid() {
        return faceid;
    }

    public String getPeronid() {
        return peronid;
    }

    public String getPersongroupid() {
        return persongroupid;
    }

    public VerifyBody(String faceid, String peronid, String persongroupid) {
        this.faceid = faceid;
        this.peronid = peronid;
        this.persongroupid = persongroupid;

    }
}
