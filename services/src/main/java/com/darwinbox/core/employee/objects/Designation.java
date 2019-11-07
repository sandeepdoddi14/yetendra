package com.darwinbox.core.employee.objects;

import com.darwinbox.Services;
import com.darwinbox.core.services.LocationTypeServices;
import com.darwinbox.core.services.DepartmentServices;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.core.services.FunctionalAreaServices;

import java.util.HashMap;
import java.util.List;

public class Designation {
    private String id;

    private String designationName;
    private String groupCompany;
    private String departmentName;
    private String recruitmentHiringLead;
    private String jobEvaluationScore;
    private String jobDescription;
    private String functionalArea;
    private String numberOfPostions;
    private String staffingModel;


    public Boolean getOverHiringAllowed() {
        return overHiringAllowed;
    }

    public void setOverHiringAllowed(Boolean overHiringAllowed) {
        this.overHiringAllowed = overHiringAllowed;
    }

    public Boolean getIncludeEmployeesOnNotice() {
        return includeEmployeesOnNotice;
    }

    public void setIncludeEmployeesOnNotice(Boolean includeEmployeesOnNotice) {
        this.includeEmployeesOnNotice = includeEmployeesOnNotice;
    }

    public List<String> getOfficeLocations() {
        return officeLocations;
    }

    public void setOfficeLocations(List<String> officeLocations) {
        this.officeLocations = officeLocations;
    }

    public List<Integer> getNoOfPositions() {
        return noOfPositions;
    }

    public void setNoOfPositions(List<Integer> noOfPositions) {
        this.noOfPositions = noOfPositions;
    }

    private Boolean overHiringAllowed = false;
    private Boolean includeEmployeesOnNotice = false;
    private List<String> officeLocations;
    private List<Integer> noOfPositions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getDepartmentName() {
        return departmentName;
    }


    public HashMap<String, String> toMap() {
        HashMap<String, String> body = new HashMap<>();

        body.put("UserDesignationsForm[designation_name_id]", new DesignationNamesServices().getDesignationNames().get(getDepartmentName()));

        body.put("UserDesignationsForm[parent_company_id]", new Services().getGroupCompanyIds().get(getGroupCompany()));
        body.put("UserDesignationsForm[department_id]", new DepartmentServices().getDepartments().get(getDepartmentName()));

        //YTD
        body.put("UserDesignationsForm[recruitment_hiring_leads]", "");
        body.put("employee_search", "");

        body.put("UserDesignationsForm[je_score]", getJobEvaluationScore());
        //YTD
        body.put("UserDesignationsForm[jd_id]", "");

        body.put("UserDesignationsForm[functional_area]", new FunctionalAreaServices().getFunctionalAreas().get(getFunctionalArea()));
        body.put("UserDesignationsForm[allowed_positions]", getNumberOfPostions());


        //YTD
        body.put("UserDesignationsForm[staffing_model]", "");

        body.put("UserDesignationsForm[over_hiring_allowed]", getOverHiringAllowed() ? 1 + "" : 0 + "");
        body.put("UserDesignationsForm[cnt_notice_period_employee]", getIncludeEmployeesOnNotice() ? 1 + "" : 0 + "");

        for (String location : getOfficeLocations()) {
            body.put("Officelocationset[officelocation][]", new LocationTypeServices().getLocationTypeIdByName(location));
        }


        for (int noOfPositions : getNoOfPositions()) {
            body.put("Officelocationset[number_of_position][]", noOfPositions + "");

        }

    return body;
    }



    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRecruitmentHiringLead() {
        return recruitmentHiringLead;
    }

    public void setRecruitmentHiringLead(String recruitmentHiringLead) {
        this.recruitmentHiringLead = recruitmentHiringLead;
    }

    public String getJobEvaluationScore() {
        return jobEvaluationScore;
    }

    public void setJobEvaluationScore(String jobEvaluationScore) {
        this.jobEvaluationScore = jobEvaluationScore;
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

    public String getNumberOfPostions() {
        return numberOfPostions;
    }

    public void setNumberOfPostions(String numberOfPostions) {
        this.numberOfPostions = numberOfPostions;
    }

    public String getStaffingModel() {
        return staffingModel;
    }

    public void setStaffingModel(String staffingModel) {
        this.staffingModel = staffingModel;
    }
}
