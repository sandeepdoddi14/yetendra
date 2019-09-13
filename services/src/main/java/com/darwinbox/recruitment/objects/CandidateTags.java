package com.darwinbox.recruitment.objects;

import com.darwinbox.recruitment.services.CandidateTagsService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateTags {

    public enum candidateDecisionType {
        DUMMY,
        REJECTED,
        ONHOLD,
        CANDIDATEDECLINEOFFER,
        OFFERWITHDRAWN,
        CUSTOMTAGS;
    }

    public static candidateDecisionType candidateDecisionType;
    private String candidateDecisionReason;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public candidateDecisionType getCandidateDecisionType() {
        return candidateDecisionType;
    }

    public void setCandidateDecisionType(candidateDecisionType candidateDecisionType) {
        this.candidateDecisionType = candidateDecisionType;
    }

    public String getCandidateDecisionReason() {
        return candidateDecisionReason;
    }

    public void setCandidateDecisionReason(String candidateDecisionReason) {
        this.candidateDecisionReason = candidateDecisionReason;
    }


    public void toObject(Map<String, String> body) {

        /*setCandidateDecisionType(candidateDecisionType.valueOf(body.get("CandidateType")));
        setCandidateDecisionReason(body.get("CandidateReason"));*/

        setCandidateDecisionType(candidateDecisionType.valueOf("ONHOLD"));
        setCandidateDecisionReason("check enum");
    }
    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();

        body.put("yt0", "SAVE");
        body.put("RecruitmentCandidateDescision[type]",""+getCandidateDecisionType().ordinal());
        body.put("RecruitmentCandidateDescision[value]",getCandidateDecisionReason());
        return body;

    }

    }
