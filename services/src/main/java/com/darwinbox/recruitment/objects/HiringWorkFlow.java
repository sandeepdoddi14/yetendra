package com.darwinbox.recruitment.objects;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class HiringWorkFlow extends Services {

         private String firstStageName;
         private boolean firstStageStatus;
         private String secondStageName;

         private List<String> assessmentName;
         private List<String> assessmentMeasure;
         private List<String> stepType;
         private String lastStageName;

    public List<String> getStepType() {
        return stepType;
    }

    public void setStepType(List<String> stepType) {
        this.stepType = stepType;
    }

    public String getFirstStageName() {
        return firstStageName;
    }

    public void setFirstStageName(String firstStageName) {
        this.firstStageName = firstStageName;
    }

    public boolean isFirstStageStatus() {
        return firstStageStatus;
    }

    public void setFirstStageStatus(boolean firstStageStatus) {
        this.firstStageStatus = firstStageStatus;
    }

    public String getSecondStageName() {
        return secondStageName;
    }

    public void setSecondStageName(String secondStageName) {
        this.secondStageName = secondStageName;
    }

    public List<String> getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(List<String> assessmentName) {
        this.assessmentName = assessmentName;
    }

    public List<String> getAssessmentMeasure() {
        return assessmentMeasure;
    }

    public void setAssessmentMeasure(List<String> assessmentMeasure) {
        this.assessmentMeasure = assessmentMeasure;
    }

    public String getLastStageName() {
        return lastStageName;
    }

    public void setLastStageName(String lastStageName) {
        this.lastStageName = lastStageName;
    }

    public List<NameValuePair> toMap() {

        List<NameValuePair> list = new ArrayList<>();
        Map<String, String> body = new HashMap<>();

        body.put("mode","HiringWorkFlow");
        body.put("HiringWorkflow[first_stage_name]",getFirstStageName());
        body.put("HiringWorkflow[first_stage_status]", LeaveDeductionsBase.parseToPHP(isFirstStageStatus()));
        body.put("HiringWorkflow[second_stage_name]",getSecondStageName());
        body.put("HiringWorkflow[last_stage_name]",getLastStageName());

        for(int count = 0;count <=1;count++) {
            for (String assessmentName : getAssessmentName()) {
                list.add(new BasicNameValuePair("Recruitment_set[" + count + "][assessment]", assessmentName));
            }
            for (String assessmentMeasure : getAssessmentMeasure()) {
                list.add(new BasicNameValuePair("Recruitment_set[" + count + "][measure]", assessmentMeasure));
            }
            for (String step : getStepType()) {
                list.add(new BasicNameValuePair("Recruitment_set[" + count + "][step_type]", step));
            }
        }
        list.addAll(mapToFormData(body)) ;
        return list;
    }

    public void toObject(Map<String, String> body) {

        setFirstStageName(body.get("firstStageName"));
        setFirstStageStatus(LeaveDeductionsBase.getFilter(body,"setFirstStageNameStatus","true"));
        setSecondStageName(body.get("secondStageName"));
        setLastStageName(body.get("lastStageName"));

           List<String> firstSetName = new ArrayList<>();
           List<String> firstSetMeasure = new ArrayList<>();
           List<String> firstSetstepType = new ArrayList<>();
           firstSetName.add(body.get("firstSetName"));
           firstSetMeasure.add(body.get("firstSetMeasure"));
           firstSetstepType.add(body.get("firstSetStepType"));
        /* firstSetName.add("Manager Round");
           firstSetMeasure.add("12");
        */    setAssessmentName(firstSetName);
            setStepType(firstSetstepType);
            setAssessmentMeasure(firstSetMeasure);

    }


    }
