package com.darwinbox.recruitment.objects;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RequisitionPositionLinewise extends Services {

    String positionNumber;
    String positionType;
    String positionID;
    String location;
    String replacementFor;
    String reportingManager;
    String empType;
    String joblevel;
    String recruiter;
    String desAlias;

    public String getJoblevel() {
        return joblevel;
    }

    public void setJoblevel(String joblevel) {
        this.joblevel = joblevel;
    }

    public String getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(String positionNumber) {
        this.positionNumber = positionNumber;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReplacementFor() {
        return replacementFor;
    }

    public void setReplacementFor(String replacementFor) {
        this.replacementFor = replacementFor;
    }

    public String getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(String reportingManager) {
        this.reportingManager = reportingManager;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(String recruiter) {
        this.recruiter = recruiter;
    }

    public String getDesAlias() {
        return desAlias;
    }

    public void setDesAlias(String desAlias) {
        this.desAlias = desAlias;
    }

    public List<NameValuePair> toMap(int lineItem) {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][position_number]",""+(lineItem+1)));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][position_type]",getPositionType()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][position_id]",getPositionID()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][location]",getLocation()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][rep_for]",getReplacementFor()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][reporting_manager]",getReportingManager()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][emp_type]",getEmpType()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][job_level]",getJoblevel()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][recruiter]",getRecruiter()));
        formData.add(new BasicNameValuePair("matrix[" + lineItem + "][designation_alias]",getDesAlias()));

        return formData;
    }

    public void toObject(Map<String, String> data) {

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        Object[] location;
        location = getOfficeLocations(id).values().toArray();

        Object[] empTypes = getEmployeeTypes().values().toArray();

         setPositionType("new");
         setLocation((String) location[new Random().nextInt(location.length)]);
         setEmpType((String) empTypes[new Random().nextInt(empTypes.length)]);
         setRecruiter(data.get("HiringLead"));


    }
    }
