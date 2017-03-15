package com.example.quanla.smartschool.networks.jsonModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by QuanLA on 3/10/2017.
 */

public class ResponseBody {


    @SerializedName("password")
    public String password;
    @SerializedName("_id")
    public ID _id;
    @SerializedName("name")
    public String name;

    public ResponseBody(String password, ResponseBody.ID _id, String name) {
        this.password = password;
        this._id = _id;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public ResponseBody.ID get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void set_id(ResponseBody.ID _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public class ID {
        @SerializedName("$oid")
        public String $oid;

        public ID(String $oid) {
            this.$oid = $oid;
        }

        public String get$oid() {
            return $oid;
        }

        @Override
        public String toString() {
            return "_id{" +
                    "$oid='" + $oid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "password='" + password + '\'' +
                ", _id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }
}
