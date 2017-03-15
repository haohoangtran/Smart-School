package com.example.quanla.smartschool.networks.jsonModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by QuanLA on 3/10/2017.
 */

public class JsonBody {
    @SerializedName("name")
    private String username;
    @SerializedName("password")
    private String password;


    public JsonBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
