package com.example.quanla.smartschool.database;

import android.util.Log;

import com.example.quanla.smartschool.database.model.Checkin;
import com.example.quanla.smartschool.networks.NetContextLogin;
import com.example.quanla.smartschool.networks.services.CheckinService;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tranh on 3/12/2017.
 */

public class DbListCheckin {
    public static final DbListCheckin instance=new DbListCheckin();
    private final String TAG=DbListCheckin.class.toString();
    private List<Checkin> checkins;

    private DbListCheckin() {
        checkins=new Vector<>();
    }
    public void getAllListCheckin(){
        CheckinService checkinService= NetContextLogin.instance.create(CheckinService.class);
        checkinService.getListCheckin().enqueue(new Callback<List<Checkin>>() {
            @Override
            public void onResponse(Call<List<Checkin>> call, Response<List<Checkin>> response) {
                checkins=response.body();
                for (Checkin checkin : checkins) {
                    Log.e(TAG, String.format("onResponse: %s", checkin.toString()) );
                }
            }

            @Override
            public void onFailure(Call<List<Checkin>> call, Throwable t) {
                Log.e(TAG, "onFailure: Lỗi" );
            }
        });
    }
    public List<Checkin> getCheckins() {
        return checkins;
    }
}
