package com.example.quanla.smartschool.networks.jsonModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/11/2017.
 */

public class GetPersonGroupTrainingStatusJSON {
    @SerializedName("status")
    public String status;
    @SerializedName("createdDateTime")
    public String createddatetime;
    @SerializedName("lastActionDateTime")
    public String lastactiondatetime;
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        return "GetPersonGroupTrainingStatusJSON{" +
                "status='" + status + '\'' +
                ", createddatetime='" + createddatetime + '\'' +
                ", lastactiondatetime='" + lastactiondatetime + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateddatetime() {
        return createddatetime;
    }

    public void setCreateddatetime(String createddatetime) {
        this.createddatetime = createddatetime;
    }

    public String getLastactiondatetime() {
        return lastactiondatetime;
    }

    public void setLastactiondatetime(String lastactiondatetime) {
        this.lastactiondatetime = lastactiondatetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetPersonGroupTrainingStatusJSON(String status, String createddatetime, String lastactiondatetime, String message) {

        this.status = status;
        this.createddatetime = createddatetime;
        this.lastactiondatetime = lastactiondatetime;
        this.message = message;
    }
}
