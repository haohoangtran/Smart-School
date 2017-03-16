package com.example.quanla.smartschool.database;

import android.util.Log;

import com.example.quanla.smartschool.database.model.Student;
import com.example.quanla.smartschool.database.model.StudentRespon;
import com.example.quanla.smartschool.eventbus.GetDataFaildedEvent;
import com.example.quanla.smartschool.eventbus.GetDataSuccusEvent;
import com.example.quanla.smartschool.networks.NetContextMicrosoft;
import com.example.quanla.smartschool.networks.services.StudentService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tranh on 3/11/2017.
 */

public class DbStudentContext {

    public static DbStudentContext instance = new DbStudentContext();
    private final String TAG = DbStudentContext.class.toString();
    private List<Student> students;
    private List<StudentRespon> studentRespon;
    private String idGroup;
    private String nameGroup;

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
        Log.e(TAG, String.format("setIdGroup: %s", idGroup));
        getAllStudentInGroup();
    }

    private DbStudentContext() {
        this.students = new Vector<>();
        studentRespon = new Vector<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void getAllStudentInGroup() {
        students.clear();
        studentRespon.clear();
        StudentService studentService = NetContextMicrosoft.instance.create(StudentService.class);
        studentService.getAllPersonInGroup(idGroup).enqueue(new Callback<List<StudentRespon>>() {
            @Override
            public void onResponse(Call<List<StudentRespon>> call, Response<List<StudentRespon>> response) {
                studentRespon = response.body();
                for (int i = 0; i < studentRespon.size(); i++) {
                    students.add(new Student(studentRespon.get(i)));
                }
                for (int i = 0; i < students.size(); i++) {
                    Log.e(TAG, String.format("onResponse: %s", students.get(i).toString()));
                }
                EventBus.getDefault().postSticky(new GetDataSuccusEvent());
                Log.e(TAG, "onResponse: Thành");

            }

            @Override
            public void onFailure(Call<List<StudentRespon>> call, Throwable t) {
                EventBus.getDefault().postSticky(new GetDataFaildedEvent());
                ;
            }
        });
    }

    public Student findStudent(String personId) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getPersonid().equals(personId)) {
                return students.get(i);
            }
        }
        return null;
    }

    public void setStudentIsHere(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (student.getPersonid().equals(students.get(i).getPersonid()) ) {
                students.get(i).setHere(true);
            }
        }
    }
}
