package com.example.quanla.smartschool.adapter.viewhodler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.activities.StudentListActivity;
import com.example.quanla.smartschool.database.model.Student;
import com.example.quanla.smartschool.database.model.StudentIdCheckIn;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.width;

/**
 * Created by tranh on 3/11/2017.
 */

public class StudentListHolder extends RecyclerView.ViewHolder {
    private final String TAG=StudentListHolder.class.toString();
    Context context;
    @BindView(R.id.tv_nameStudent)
    TextView tvName;
    @BindView(R.id.tv_idStudent)
    TextView tvIdStudent;
    @BindView(R.id.cb_checkin)
    CheckBox cbCheckin;
    @BindView(R.id.iv_student_img)
    ImageView ivStudent;
    public StudentListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        context=itemView.getContext();

    }
    public void bind(final Student student){
        Picasso.with(context)
                .load(student.getUrl())
                .resize(100, 100)
                .centerCrop()
                .into(ivStudent);
        tvName.setText(student.getName());
        tvIdStudent.setText(student.getIdStudent());
        cbCheckin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    StudentListActivity.studentids.add(new StudentIdCheckIn(student.getIdStudent()));
                }
                else {
                    StudentListActivity.studentids.remove(new StudentIdCheckIn(student.getIdStudent()));
                }
            }
        });
    }
}
