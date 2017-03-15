package com.example.quanla.smartschool.database;

import android.support.v7.view.ActionBarPolicy;
import android.util.Log;

import com.example.quanla.smartschool.database.model.ClassStudent;
import com.example.quanla.smartschool.eventbus.GetDataFaildedEvent;
import com.example.quanla.smartschool.eventbus.GetDataSuccusEvent;
import com.example.quanla.smartschool.networks.NetContextMicrosoft;
import com.example.quanla.smartschool.networks.services.ClassService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tranh on 3/11/2017.
 */

public class DbClassContext {
    private final String TAG=DbClassContext.class.toString();
    public static final DbClassContext instance=new DbClassContext();
    private List<ClassStudent> students;

    public List<ClassStudent> getStudents() {
        return students;
    }

    private DbClassContext() {
        this.students = new Vector<>();
        getAllGroup();
    }
    public void getAllGroup() {
        students.clear();
        ClassService classService = NetContextMicrosoft.instance.create(ClassService.class);
        classService.getAllGroup().enqueue(new Callback<List<ClassStudent>>() {
            @Override
            public void onResponse(Call<List<ClassStudent>> call, Response<List<ClassStudent>> response) {
                students = response.body();
                for (int i = 0; i < students.size(); i++) {
                    Log.e(TAG, String.format("onResponse: %s", students.get(i)) );
                }
                Log.e(TAG, "onResponse: load hết group");
                EventBus.getDefault().postSticky(new GetDataSuccusEvent());
            }

            @Override
            public void onFailure(Call<List<ClassStudent>> call, Throwable t) {
                Log.e(TAG, String.format("onFailure: %s", t.toString()));
                EventBus.getDefault().postSticky(new GetDataFaildedEvent());
            }
        });
    }


}
