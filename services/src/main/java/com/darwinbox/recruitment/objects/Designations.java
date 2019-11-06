package com.darwinbox.recruitment.objects;

import com.darwinbox.Services;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Designations extends Services {

private String designation;
private String groupCompany;
private String department;
private List<String> hiringLead;
private String jobEvaluation;
private String jobDescription;
private String functionalArea;
private String numberOfPositions;
private String staffingModel;
private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getHiringLead() {
        return hiringLead;
    }

    public void setHiringLead(List<String> hiringLead) {
        this.hiringLead = hiringLead;
    }

    public String getJobEvaluation() {
        return jobEvaluation;
    }

    public void setJobEvaluation(String jobEvaluation) {
        this.jobEvaluation = jobEvaluation;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }

    public String getNumberOfPositions() {
        return numberOfPositions;
    }

    public void setNumberOfPositions(String numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
    }

    public String getStaffingModel() {
        return staffingModel;
    }

    public void setStaffingModel(String staffingModel) {
        this.staffingModel = staffingModel;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Map<String,String> getDefaultForDesignation() {

        Map<String, String> body = new HashMap<>();

        body.put("yt0","SAVE");
        body.put("UserDesignationsForm[designation_name_id]",getDesignation());
        body.put("UserDesignationsForm[parent_company_id]",getGroupCompany());
        body.put("UserDesignationsForm[department_id]",getDepartment());
        body.put("UserDesignationsForm[recruitment_hiring_leads]","");
        body.put("UserDesignationsForm[je_score]","");
        body.put("UserDesignationsForm[jd_id]","");
        body.put("UserDesignationsForm[functional_area]","");
        body.put("UserDesignationsForm[allowed_positions]",getNumberOfPositions());
        body.put("UserDesignationsForm[staffing_model]","");
        body.put("Officelocationset[officelocation][]","");
        body.put("Officelocationset[number_of_position][]","");
        body.put("UserDesignationsForm[auto_numbering]","1");
        body.put("UserDesignationsForm[prefix]","");
        body.put("UserDesignationsForm[next_number]","");

        return body;
    }

    public void setDefaultForDesignation(Map<String, String> body){

        String gcName ="Vikas InfoTech"; // body.get("companyName");
        String  id = getGroupCompanyIds().get(gcName);

        JSONObject obj = getDesignations(id);
        Object[] keys = obj.keySet().toArray();
        JSONObject kv = obj.getJSONObject((String) keys[new Random().nextInt(keys.length)]);
        Object[] designations = kv.keySet().toArray();

        Object[] departments= getDepartments(id).values().toArray();

       // setDesignation("5ab62438e5190");
        setDesignation((String) designations[new Random().nextInt(designations.length)]);
        setGroupCompany(id);
        setDepartment(String.valueOf(departments[new Random().nextInt(departments.length)]));
        setNumberOfPositions("500");

    }


    }
