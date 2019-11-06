package com.darwinbox.recruitment.objects.jobsPages;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HiringWFThirdPageBody {

    String stage;
    String stepType;
    String evalType;
    String assessmentOptions;
    String assessmentIDs;
    String expiry;
    String triggerType;
    String cutOffPercentage;
    String assessmentEvaluators;
    String phases;

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getEvalType() {
        return evalType;
    }

    public void setEvalType(String evalType) {
        this.evalType = evalType;
    }

    public String getAssessmentOptions() {
        return assessmentOptions;
    }

    public void setAssessmentOptions(String assessmentOptions) {
        this.assessmentOptions = assessmentOptions;
    }

    public String getAssessmentIDs() {
        return assessmentIDs;
    }

    public void setAssessmentIDs(String assessmentIDs) {
        this.assessmentIDs = assessmentIDs;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getCutOffPercentage() {
        return cutOffPercentage;
    }

    public void setCutOffPercentage(String cutOffPercentage) {
        this.cutOffPercentage = cutOffPercentage;
    }

    public String getAssessmentEvaluators() {
        return assessmentEvaluators;
    }

    public void setAssessmentEvaluators(String assessmentEvaluators) {
        this.assessmentEvaluators = assessmentEvaluators;
    }

    public String getPhases() {
        return phases;
    }

    public void setPhases(String phases) {
        this.phases = phases;
    }

    public List<NameValuePair> toMapThirdPageBody() {

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("state_stage[]",getStage()));
        list.add(new BasicNameValuePair("step_type[]",getStepType()));
        list.add(new BasicNameValuePair("eval_type[]",getEvalType()));
        list.add(new BasicNameValuePair("eval_percentage[]","")); //left blank, as there is no functionality
        list.add(new BasicNameValuePair("assessment_options[]",getAssessmentOptions()));
        list.add(new BasicNameValuePair("assessment_ids[]",getAssessmentIDs()));
        list.add(new BasicNameValuePair("expiry[]",getExpiry()));
        list.add(new BasicNameValuePair("trigger_type[]",getTriggerType()));
        list.add(new BasicNameValuePair("cutoff_percentage[]",getCutOffPercentage()));
        list.add(new BasicNameValuePair("assessment_evaluators[]",getAssessmentEvaluators()));
        list.add(new BasicNameValuePair("phases[]",getPhases())); //first,second,1,2,last

        return list;
    }
    public void toObjectThirdPageBody(Map<String, String> body) {

        setStage(body.get("Stage"));
        setStepType(body.get("StepType"));
        setEvalType(body.get("EvalType"));
        setAssessmentOptions(body.get("assessmentOptions"));
        setAssessmentIDs(body.get("assessmentIDs"));
        setExpiry(body.get("expiry"));
        setTriggerType(body.get("triggerType"));
        setCutOffPercentage(body.get("cutOffPercent"));
        setAssessmentEvaluators(body.get("assessmentEvaluators"));
        setPhases(body.get("Phases"));

    }
    }
