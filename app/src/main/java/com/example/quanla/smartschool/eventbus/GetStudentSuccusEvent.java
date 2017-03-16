package com.example.quanla.smartschool.eventbus;

import com.example.quanla.smartschool.database.model.Student;

/**
 * Created by tranh on 3/16/2017.
 */

public class GetStudentSuccusEvent {
    @Override
    public String toString() {
        return "GetStudentSuccusEvent{" +
                "student=" + student +
                '}';
    }

    public Student getStudent() {
        return student;
    }

    public GetStudentSuccusEvent(Student student) {

        this.student = student;
    }

    private Student student;
}
