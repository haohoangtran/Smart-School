package com.example.quanla.smartschool.database.respon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/5/2017.
 */

public class PersionId {

    @SerializedName("personId")
    public String personid;

    public String getPersonid() {
        return personid;
    }

    public PersionId(String personid) {

        this.personid = personid;
    }

    @Override
    public String toString() {
        return "PersionId{" +
                "personid='" + personid + '\'' +
                '}';
    }
}
