package com.example.quanla.smartschool.database.respon;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Vector;

/**
 * Created by tranh on 3/11/2017.
 */

public class IndentifyRespon {

    @SerializedName("faceId")
    private String faceid;
    @SerializedName("candidates")
    private List<Candidates> candidates;

    public class Candidates {
        @SerializedName("personId")
        private String personid;
        @SerializedName("confidence")
        private double confidence;

        public String getPersonid() {
            return personid;
        }

        public double getConfidence() {
            return confidence;
        }

        public Candidates(String personid, double confidence) {

            this.personid = personid;
            this.confidence = confidence;
        }

        @Override
        public String toString() {
            return "Candidates{" +
                    "personid='" + personid + '\'' +
                    ", confidence=" + confidence +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "IndentifyRespon{" +
                "faceid='" + faceid + '\'' +
                ", candidates=" + candidates +
                '}';
    }

    public String getFaceid() {
        return faceid;
    }

    public List<Candidates> getCandidates() {
        return candidates;
    }
    public List<PersionId> getPersonsList(){
        List<PersionId> persionIds=new Vector<>();
        if (candidates.size()==0){
            return persionIds;
        }
        for (int i = 0; i < candidates.size(); i++) {
            persionIds.add(new PersionId(candidates.get(i).getPersonid()));
        }
        return persionIds;
    }
    public IndentifyRespon(String faceid, List<Candidates> candidates) {

        this.faceid = faceid;
        this.candidates = candidates;
    }
}
