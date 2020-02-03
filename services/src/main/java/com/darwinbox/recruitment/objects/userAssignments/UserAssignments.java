package com.darwinbox.recruitment.objects.userAssignments;

import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DesignationNamesServices;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class UserAssignments extends Services {

    /*Request URL: https://automation.qa.darwinbox.io/settings/company/assignment
     yt0: SAVE
TenantAssignmentFramework[name]: testing
TenantAssignmentFramework[description]: testing
TenantAssignmentFramework[business_unit][]: BU_5a92ff2be39b6
TenantAssignmentFramework[employee_type][]: TYP_5aaf864bb6748
TenantAssignmentFramework[department][]: DEPT_5b891844db6f3
TenantAssignmentFramework[department][]: DEPT_5dea71caa92fd
TenantAssignmentFramework[designation_name][]: 5a92ff7b769ba
TenantAssignmentFramework[designation_name][]: 5ab624221acc9
TenantAssignmentFramework[job_level][]: 5bf7e89a0f908
TenantAssignmentFramework[locations][]: LOC_5aeab90953002
TenantAssignmentFramework[locations][]: LOC_5af86a749bfe2
TenantAssignmentFramework[division][]: 5debac4369c1f
TenantAssignmentFramework[location_type][]: 5ad1ce74c0980*/


    private String assignmentName;
    private String assignmentDesc;
    private String businessUnit;
    private String employeeType;
    private String department;
    private String designation;
    private String jobLevels;
    private String location;
    private String division;
    private String locationType;

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDesc() {
        return assignmentDesc;
    }

    public void setAssignmentDesc(String assignmentDesc) {
        this.assignmentDesc = assignmentDesc;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
    public String getJobLevels() {
        return jobLevels;
    }

    public void setJobLevels(String jobLevels) {
        this.jobLevels = jobLevels;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void toObject(Map<String, String> body) {

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        DesignationNames designationNames = new DesignationNames();
        DesignationNamesServices designationNamesServices = new DesignationNamesServices();

      JSONArray  names= designationNamesServices.getAllDesignationNames();
      //names.getJSONObject(0).keySet();
        //designationNames=designationNamesServices.getDesignationNamesID();

  /* Todo:
   create a fixed designation name
        find its corresponding id in designations page and user assignments page
  */
       designationNames=designationNamesServices.getDesignationNamesID("Manager");

        Object[] location;
        location = getOfficeLocations(id).values().toArray();

        Object[] empTypes = getEmployeeTypes().values().toArray();

        Object[] jobLevelIDS=getJobLevelIDS().values().toArray();

        HashMap departments = getDepartments(id);
        ArrayList  valuesList = new ArrayList<String>(departments.values());
        int randomIndex = new Random().nextInt(valuesList.size());
        Object departmentValue = valuesList.get(randomIndex);

        setAssignmentName("User Assignment::"+new Date());
        setAssignmentDesc("Created by Automation");
        setBusinessUnit("");
        setEmployeeType("TYP_"+empTypes[new Random().nextInt(empTypes.length)]);
        setDepartment((String) departmentValue);
        setDesignation(designationNames.getId().replace("\" ",""));
        setJobLevels((String) jobLevelIDS[new Random().nextInt(jobLevelIDS.length)]);
        setLocation("LOC_"+location[new Random().nextInt(location.length)]);
        setDivision("");
        setLocationType("");

    }

    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();

        body.put("yt0","SAVE");
        body.put("TenantAssignmentFramework[name]",getAssignmentName());
        body.put("TenantAssignmentFramework[description]",getAssignmentDesc());
        body.put("TenantAssignmentFramework[business_unit][]",getBusinessUnit());
        body.put("TenantAssignmentFramework[employee_type][]",getEmployeeType());
        body.put("TenantAssignmentFramework[department][]",getDepartment());
        body.put("TenantAssignmentFramework[designation_name][]",getDesignation());
        body.put("TenantAssignmentFramework[job_level][]",getJobLevels());
        body.put("TenantAssignmentFramework[locations][]",getLocation());
        body.put("TenantAssignmentFramework[division][]",getDivision());
        body.put("TenantAssignmentFramework[location_type][]",getLocationType());

        return body;
    }
    }
