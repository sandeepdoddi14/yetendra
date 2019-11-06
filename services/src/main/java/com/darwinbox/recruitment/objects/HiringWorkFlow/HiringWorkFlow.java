package com.darwinbox.recruitment.objects.HiringWorkFlow;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.Services;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlowBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class HiringWorkFlow extends Services {

         private String firstStageName;
         private boolean firstStageStatus;
         private String secondStageName;

          List<String> assessmentName;
          List<String> assessmentMeasure;
          List<String> stepType;
         private String lastStageName;
    private List<HiringWorkFlowLineItems> hiringWorkFlowLineItems = new ArrayList<>();


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


    public void add(HiringWorkFlowLineItems hiringWorkFlowLineItem){
        hiringWorkFlowLineItems.add(hiringWorkFlowLineItem);
    }

    public List<NameValuePair> toMap() {

        List<NameValuePair> list = new ArrayList<>();
        Map<String, String> body = new HashMap<>();

        body.put("mode","HiringWorkFlow");
        body.put("HiringWorkflow[first_stage_name]",getFirstStageName());
        body.put("HiringWorkflow[first_stage_status]", LeaveDeductionsBase.parseToPHP(isFirstStageStatus()));
        body.put("HiringWorkflow[second_stage_name]",getSecondStageName());
        body.put("HiringWorkflow[last_stage_name]",getLastStageName());

        int count = 0;
        for (HiringWorkFlowLineItems HWbody : hiringWorkFlowLineItems) {

            list.addAll(list.size(), (HWbody.toMap(count)));
            count++;
        }


        list.addAll(mapToFormData(body)) ;
        return list;
    }

        public void toObject(Map<String, String> body) {

        setFirstStageName(body.get("firstStageName"));
        setFirstStageStatus(LeaveDeductionsBase.getFilter(body,"setFirstStageNameStatus","true"));
        setSecondStageName(body.get("secondStageName"));
        setLastStageName(body.get("lastStageName"));

    }


    }
