package com.example.quanla.smartschool.database.respon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/11/2017.
 */

public class VerifyRespon {

    @SerializedName("isIdentical")
    public boolean isidentical;
    @SerializedName("confidence")
    public double confidence;

    public boolean isidentical() {
        return isidentical;
    }

    public double getConfidence() {
        return confidence;
    }

    public VerifyRespon(boolean isidentical, double confidence) {

        this.isidentical = isidentical;
        this.confidence = confidence;
    }
}
