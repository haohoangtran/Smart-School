package com.example.quanla.smartschool.database.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranh on 3/11/2017.
 */

public class StudentIdCheckIn {
    @SerializedName("student_id")
    private String id;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "StudentIdCheckIn{" +
                "id='" + id + '\'' +
                '}';
    }

    public StudentIdCheckIn(String id) {
        this.id=id;
    }
}
