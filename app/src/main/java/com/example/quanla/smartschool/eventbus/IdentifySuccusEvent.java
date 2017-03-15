package com.example.quanla.smartschool.eventbus;

import com.example.quanla.smartschool.database.respon.IndentifyRespon;

import java.util.List;

/**
 * Created by tranh on 3/11/2017.
 */

public class IdentifySuccusEvent {
    private List<IndentifyRespon> indentifyRespons;

    @Override
    public String toString() {
        return "IdentifySuccusEvent{" +
                "indentifyRespons=" + indentifyRespons +
                '}';
    }

    public List<IndentifyRespon> getIndentifyRespons() {
        return indentifyRespons;
    }

    public IdentifySuccusEvent(List<IndentifyRespon> indentifyRespons) {

        this.indentifyRespons = indentifyRespons;
    }
}
