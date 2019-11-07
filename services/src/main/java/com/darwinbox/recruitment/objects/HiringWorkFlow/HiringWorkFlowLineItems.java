package com.darwinbox.recruitment.objects.HiringWorkFlow;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HiringWorkFlowLineItems  {

    String assessmentName;
    String assessmentMeasure;
    String stepType;

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getAssessmentMeasure() {
        return assessmentMeasure;
    }

    public void setAssessmentMeasure(String assessmentMeasure) {
        this.assessmentMeasure = assessmentMeasure;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public List<NameValuePair> toMap(int lineItem) {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("Recruitment_set[" + lineItem + "][assessment]", getAssessmentName()));
        formData.add(new BasicNameValuePair("Recruitment_set[" + lineItem + "][measure]", getAssessmentMeasure()));
        formData.add(new BasicNameValuePair("Recruitment_set[" + lineItem + "][step_type]", getStepType()));

        return formData;
    }

    public void toObject(Map<String, String> data) {

         setAssessmentName(data.get("AssessmentName"));
         setAssessmentMeasure(data.get("Measure"));
         setStepType(data.get("Step"));


    }

    }
