package com.example.quanla.smartschool.eventbus;

import com.example.quanla.smartschool.database.model.Student;

/**
 * Created by QuanLA on 3/12/2017.
 */

public class UploadPersonFaceToServerEvent {
    private Student student;

    public Student getStudent() {
        return student;
    }

    public UploadPersonFaceToServerEvent(Student student) {

        this.student = student;
    }
}
