package com.example.quanla.smartschool.networks.jsonModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tranh on 3/11/2017.
 */

public class IndentifyBody {

    @SerializedName("personGroupId")
    public String persongroupid;

    public IndentifyBody(String persongroupid, List<String> faceids) {
        this.persongroupid = persongroupid;
        this.faceids = faceids;
        this.maxnumofcandidatesreturned = 1;
        this.confidencethreshold = 0.6;
    }

    public String getPersongroupid() {

        return persongroupid;
    }

    public List<String> getFaceids() {
        return faceids;
    }

    public int getMaxnumofcandidatesreturned() {
        return maxnumofcandidatesreturned;
    }

    public double getConfidencethreshold() {
        return confidencethreshold;
    }

    @SerializedName("faceIds")
    public List<String> faceids;
    @SerializedName("maxNumOfCandidatesReturned")
    public int maxnumofcandidatesreturned;
    @SerializedName("confidenceThreshold")
    public double confidencethreshold;
}
