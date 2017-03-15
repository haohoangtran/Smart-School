package com.example.quanla.smartschool.eventbus;

/**
 * Created by tranh on 3/11/2017.
 */

public class UploadImageSuccusEvent {
    private String url;

    public String getUrl() {
        return url;
    }

    public UploadImageSuccusEvent(String url) {

        this.url = url;
    }
}
