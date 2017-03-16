package com.example.quanla.smartschool.eventbus;

import com.example.quanla.smartschool.database.model.ClassStudent;

/**
 * Created by tranh on 3/17/2017.
 */

public class OnLongClickEvent {
    private ClassStudent classStudent;

    @Override
    public String toString() {
        return "OnLongClickEvent{" +
                "classStudent=" + classStudent +
                '}';
    }

    public ClassStudent getClassStudent() {
        return classStudent;
    }

    public OnLongClickEvent(ClassStudent classStudent) {
        this.classStudent = classStudent;
    }
}
