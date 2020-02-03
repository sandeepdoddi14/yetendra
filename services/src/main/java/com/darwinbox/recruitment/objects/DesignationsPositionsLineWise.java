package com.darwinbox.recruitment.objects;

import com.darwinbox.Services;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class DesignationsPositionsLineWise extends Services {

    String positionName;
    String manager;
    String location;
    String empType;
    String needToHire;
    String reason;
    String budgeted;
    String effectiveDate;

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getNeedToHire() {
        return needToHire;
    }

    public void setNeedToHire(String needToHire) {
        this.needToHire = needToHire;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBudgeted() {
        return budgeted;
    }

    public void setBudgeted(String budgeted) {
        this.budgeted = budgeted;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<NameValuePair> toMap(int lineItem) {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][position_id]",getPositionName() ));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][manager]",getManager() ));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][location]",getLocation() ));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][employee_type]", getEmpType()));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][need_to_hire]", getNeedToHire()));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][reason]",getReason() ));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][bugeted]", getBudgeted()));
        formData.add(new BasicNameValuePair("PositionsSet[" + lineItem + "][effective_date]",getEffectiveDate() ));

        return formData;
    }

    public void toObject(Map<String, String> data) {

        Date date = new Date();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        date= dateTimeHelper.addDays(date,-7);
        String effectiveDate = dateTimeHelper.formatDateTo(date,"dd-MM-yyyy");

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        Object[] location;
        location = getOfficeLocations(id).values().toArray();

        Object[] empTypes = getEmployeeTypes().values().toArray();
        if (location.length == 0 || empTypes.length == 0) {
            log.error("ERROR: Unable to fetch " + ((location.length == 0) ? "locations" : "employee types"));
        }


        setPositionName(data.get("PositionName"));
        setManager("");
        setLocation((String) location[new Random().nextInt(location.length)]);
        setEmpType((String) empTypes[new Random().nextInt(empTypes.length)]);
        setNeedToHire(data.get("NeedToHire"));
        setReason("");
        setBudgeted("1");
        setEffectiveDate(effectiveDate);


    }
    }