package com.darwinbox.recruitment.objects;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recruiters extends Services {

    private String recruiterName;
    private String recruiterEmail;
    private String recruiterPassword;
    private List<String> allowedOpenings;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }

    public String getRecruiterPassword() {
        return recruiterPassword;
    }

    public void setRecruiterPassword(String recruiterPassword) {
        this.recruiterPassword = recruiterPassword;
    }

    public List<String> getAllowedOpenings() {
        return allowedOpenings;
    }

    public void setAllowedOpenings(List<String> allowedOpenings) {
        this.allowedOpenings = allowedOpenings;
    }

    public void toObject(Map<String, String> body) {

        setRecruiterName("");
        setRecruiterEmail("");
        setRecruiterPassword("");
        List<String> data = new ArrayList<>();
        setAllowedOpenings(data);

    }

    public List<NameValuePair> toMap() {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> list = new ArrayList<>();

        body.put("yt0","SAVE");
        body.put("RecruitmentRecruiters[recruiter_name]",getRecruiterName());
        body.put("RecruitmentRecruiters[recruiter_email]",getRecruiterEmail());
        body.put("RecruitmentRecruiters[recruiter_password]",getRecruiterPassword());
        for (String getOpenings  : getAllowedOpenings()) {
            list.add(new BasicNameValuePair("RecruitmentRecruiters[allowed_openings][]",getOpenings));
        }
        list.addAll(mapToFormData(body));
        return list;

        //RecruitmentRecruiters[id] for edit/delete
    }
}