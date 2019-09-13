package com.darwinbox.recruitment.objects;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationForms {


    private String evaluationName;
    private List<String> assessment;
    private List<String> unitOfMeasure;
    private List<String> weight;
    private String overallRating;

    public String getEvaluationName() {
        return evaluationName;
    }

    public void setEvaluationName(String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public List<String> getAssessment() {
        return assessment;
    }

    public void setAssessment(List<String> assessment) {
        this.assessment = assessment;
    }

    public List<String> getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(List<String> unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public List<String> getWeight() {
        return weight;
    }

    public void setWeight(List<String> weight) {
        this.weight = weight;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public void toObject(Map<String, String> body) {

        setEvaluationName("");
        setOverallRating("");

        List<String> data = new ArrayList<>();
        setAssessment(data);
        setUnitOfMeasure(data);
        setWeight(data);

    }

    public List<NameValuePair> toMap() {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> list = new ArrayList<>();

        body.put("mode","create");
        body.put("RecruitmentEvaluation[name]","");
        body.put("RecruitmentEvaluation[overall_measure_type]","");

        body.put("Recruitment_set[assessment][]","");
        body.put("Recruitment_set[measure][]","");
        body.put("Recruitment_set[weight][]","");


        return list;
    }
}
