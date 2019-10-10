package com.darwinbox.reimbursement.objects.ReimbCreation;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReimbUnits {

    private String unitType;
    private String unitLabel;
    private String unitDesc;

    public String getUnitType() {    return unitType;  }

    public void setUnitType(String unitType) { this.unitType = unitType;  }

    public String getUnitLabel() {  return unitLabel;  }

    public void setUnitLabel(String unitLabel) {    this.unitLabel = unitLabel;   }

    public String getUnitDesc() {  return unitDesc;  }

    public void setUnitDesc(String unitDesc) { this.unitDesc = unitDesc; }

    public void toObject(Map<String,String> data)
    {
    setUnitType(data.get("Unit Type"));
    setUnitLabel(data.get("Label"));
    setUnitDesc(data.get("Unit Description"));
    }

    public List<NameValuePair> toMap()
    {
        List<NameValuePair> formdata = new ArrayList<>();
        formdata.add(new BasicNameValuePair("TenantReimbursementUnits[unit_name]", getUnitType()));
        formdata.add(new BasicNameValuePair("TenantReimbursementUnits[label]", getUnitLabel()));
        formdata.add(new BasicNameValuePair("TenantReimbursementUnits[unit_desc]" , getUnitDesc()));

    return formdata;
    }
}
