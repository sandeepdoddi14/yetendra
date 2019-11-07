package com.darwinbox.recruitment.objects;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationForms extends Services {


    private String evaluationName;
    private List<String> assessment;
    private List<String> unitOfMeasure;
    private List<String> weight;
    private String overallRating;
    private String id;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
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

        setEvaluationName(body.get("Name"));
        setOverallRating(body.get("Overall rating"));

        List<String> assess = new ArrayList<>();
        assess.add(body.get("assessment"));
        setAssessment(assess);

        List<String> unit = new ArrayList<>();
        unit.add(body.get("unit of measure"));
        setUnitOfMeasure(unit);

        List<String> weight = new ArrayList<>();
        weight.add(body.get("weight"));
        //weight.add(body.get("weightSet2"));
        setWeight(weight);

    }

    public List<NameValuePair> toMap() {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> list = new ArrayList<>();

        body.put("mode","create");
        body.put("RecruitmentEvaluation[name]",getEvaluationName());

        for (String assessment  : getAssessment()) {
            list.add(new BasicNameValuePair("Recruitment_set[assessment][]",assessment));
        }
        for (String measure  : getUnitOfMeasure()) {
            list.add(new BasicNameValuePair("Recruitment_set[measure][]",measure));
        }
        for (String weigh  : getWeight()) {
            list.add(new BasicNameValuePair("Recruitment_set[weight][]",weigh));
        }

        body.put("RecruitmentEvaluation[overall_measure_type]",getOverallRating());

        list.addAll(mapToFormData(body)) ;
        return list;
    }

    public List<NameValuePair> toEdit(String set) {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> list = new ArrayList<>();

        body.put("RecruitmentEvaluation[name]",getEvaluationName());
        body.put("RecruitmentEvaluation[overall_measure_type]",getOverallRating());

        for (String assessment  : getAssessment()) {
            list.add(new BasicNameValuePair("Recruitment_set["+set+"][assessment]",assessment));
        }
        for (String measure  : getUnitOfMeasure()) {
            list.add(new BasicNameValuePair("Recruitment_set["+set+"][measure]",measure));
        }
        for (String weigh  : getWeight()) {
            list.add(new BasicNameValuePair("Recruitment_set["+set+"][weight]",weigh));
        }
        return list;
    }
}
