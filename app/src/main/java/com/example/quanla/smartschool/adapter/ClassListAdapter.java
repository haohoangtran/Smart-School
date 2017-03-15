package com.example.quanla.smartschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanla.smartschool.R;
import com.example.quanla.smartschool.activities.StudentListActivity;
import com.example.quanla.smartschool.adapter.viewhodler.ClassListHodler;
import com.example.quanla.smartschool.database.DbClassContext;
import com.example.quanla.smartschool.database.DbStudentContext;
import com.example.quanla.smartschool.database.model.ClassStudent;
import com.example.quanla.smartschool.database.model.Student;
import com.example.quanla.smartschool.database.respon.IndentifyRespon;
import com.example.quanla.smartschool.eventbus.OnClassClickEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Vector;

/**
 * Created by tranh on 3/6/2017.
 */

public class ClassListAdapter extends RecyclerView.Adapter<ClassListHodler> {
    private final String TAG = ClassListAdapter.class.toString();
    private Context context;

    @Override
    public ClassListHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_class, parent, false);
        //2: create ViewHolder
        return new ClassListHodler(itemView);
    }

    public ClassListAdapter(Context context) {
        this.context = context;

    }


    @Override
    public void onBindViewHolder(ClassListHodler holder, int position) {
        final ClassStudent classStudent = DbClassContext.instance.getStudents().get(position);
        holder.bind(classStudent);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbStudentContext.instance.setIdGroup(classStudent.getPersongroupid());
                DbStudentContext.instance.setNameGroup(classStudent.getName());
                Intent intent=new Intent(context,StudentListActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DbClassContext.instance.getStudents().size();
    }


}
