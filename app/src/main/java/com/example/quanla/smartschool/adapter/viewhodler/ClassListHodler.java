package com.example.quanla.smartschool.adapter.viewhodler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.database.model.ClassStudent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tranh on 3/6/2017.
 */

public class ClassListHodler extends RecyclerView.ViewHolder {
    private String TAG = ClassListHodler.class.toString();
    @BindView(R.id.tv_classname)
    TextView tvClassName;
    @BindView(R.id.tv_classroom)
    TextView tvClassRoom;

    public ClassListHodler(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ClassStudent classStudent) {
        tvClassName.setText("Class: " + classStudent.getName());
        tvClassRoom.setText("Room: " + classStudent.getClassRoom());
    }
}
