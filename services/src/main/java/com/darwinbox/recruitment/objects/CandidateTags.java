package com.darwinbox.recruitment.objects;

import java.util.HashMap;
import java.util.Map;

public class CandidateTags {

    private String candidateDecisionType;
    private String candidateDecisionReason;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getCandidateDecisionType() {
        return candidateDecisionType;
    }

    public void setCandidateDecisionType(String candidateDecisionType) {
        this.candidateDecisionType = candidateDecisionType;
    }

    public String getCandidateDecisionReason() {
        return candidateDecisionReason;
    }

    public void setCandidateDecisionReason(String candidateDecisionReason) {
        this.candidateDecisionReason = candidateDecisionReason;
    }

    public void toObject(Map<String, String> body) {

        setCandidateDecisionType(body.get("CandidateType"));
        setCandidateDecisionReason(body.get("CandidateReason"));

    }
    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();
        body.put("yt0", "SAVE");
        body.put("RecruitmentCandidateDescision[type]",getCandidateDecisionType());
        body.put("RecruitmentCandidateDescision[value]",getCandidateDecisionReason());

        return body;
    }

    }
