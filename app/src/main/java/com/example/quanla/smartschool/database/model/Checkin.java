package com.example.quanla.smartschool.database.model;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by tranh on 3/11/2017.
 */

public class Checkin {
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;
    @SerializedName("students")
    private List<String> studentIds;

    public Checkin(String name,List<String> strings) {
        this.name = name;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH");
        Date d = new Date();
        date=dateFormat.format(d);
        this.studentIds = strings;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    @Override
    public String toString() {
        return "Checkin{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", studentIds=" + studentIds +
                '}';
    }
}
