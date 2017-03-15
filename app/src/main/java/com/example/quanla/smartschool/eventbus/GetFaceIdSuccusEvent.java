package com.example.quanla.smartschool.eventbus;

import com.example.quanla.smartschool.database.respon.FaceId;

import java.util.List;

/**
 * Created by tranh on 3/11/2017.
 */

public class GetFaceIdSuccusEvent {
    private List<FaceId> faceIds;

    public List<FaceId> getFaceIds() {
        return faceIds;
    }

    public GetFaceIdSuccusEvent(List<FaceId> faceIds) {

        this.faceIds = faceIds;
    }
}
