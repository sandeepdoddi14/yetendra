package com.darwinbox.core.company.objects;

import java.util.HashMap;

public class Project {
    private String id;

    private String projectTitle;
    private String projectCode;
    private String projectDescription;
    private Boolean isProjectChargable;
    private String projectLead;
    private String projectChampion;
    private String projectStatusID;
    private String projectStartDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Boolean getProjectChargable() {
        return isProjectChargable;
    }

    public void setProjectChargable(Boolean projectChargable) {
        isProjectChargable = projectChargable;
    }

    public String getProjectLead() {
        return projectLead;
    }

    public void setProjectLead(String projectLead) {
        this.projectLead = projectLead;
    }

    public String getProjectChampion() {
        return projectChampion;
    }

    public void setProjectChampion(String projectChampion) {
        this.projectChampion = projectChampion;
    }

    public String getProjectStatusID() {
        return projectStatusID;
    }

    public void setProjectStatusID(String projectStatusID) {
        this.projectStatusID = projectStatusID;
    }

    public String getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(String projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public HashMap<String,String> toMap(){
        HashMap<String,String> map=new HashMap<>();

        return  map;
    }
}
