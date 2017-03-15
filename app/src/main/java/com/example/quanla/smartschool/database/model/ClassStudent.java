package com.example.quanla.smartschool.database.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Vector;

/**
 * Created by tranh on 3/11/2017.
 */

public class ClassStudent {
    @SerializedName("personGroupId")
    private String persongroupid;

    @SerializedName("name")

    private String name;
    @SerializedName("userData")
    private String classRoom;


    public ClassStudent(String persongroupid, String name, String userdata) {
        this.persongroupid = persongroupid;
        this.name = name;
        this.classRoom = userdata;

    }

    public ClassStudent(String name, String classRoom) {
        this.name = name;
        this.classRoom = classRoom;
    }

    public void setPersongroupid(String persongroupid) {
        this.persongroupid = persongroupid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getPersongroupid() {

        return persongroupid;
    }

    public String getName() {
        return name;
    }

    public String getClassRoom() {
        return classRoom;
    }
}
