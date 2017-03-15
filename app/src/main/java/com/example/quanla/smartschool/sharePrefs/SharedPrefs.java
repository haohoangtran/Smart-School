package com.example.quanla.smartschool.sharePrefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by QuanLA on 3/10/2017.
 */

public class SharedPrefs {
    private static final String LOGIN_KEY = "login";
    private static final String SHARED_PREFS_NAME = "spn";

    private static SharedPrefs instance;
    private SharedPreferences sharedPreferences;


    public static SharedPrefs getInstance() {
        return instance;
    }

    public static void init(Context context){
        instance = new SharedPrefs(context);
    }

    public SharedPrefs(Context context){
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void putLogin(LoginCredentials loginCredentials){
        String loginJSON = (new Gson()).toJson(loginCredentials);

        this.sharedPreferences.edit().putString(LOGIN_KEY, loginJSON).commit();
    }

    public LoginCredentials getLoginCredentials(){
        String loginJSON = this.sharedPreferences.getString(LOGIN_KEY, null);

        if(loginJSON==null) return null;
        LoginCredentials loginCredentials = (new Gson()).fromJson(loginJSON, LoginCredentials.class);
        return loginCredentials;
    }

    public String getAccessID(){
        LoginCredentials loginCred = getLoginCredentials();
        if(loginCred!=null){
            return loginCred.getIdLogin();
        }
        return null;
    }

}
