package com.darwinbox.core.company.objects;

import com.darwinbox.core.employee.objects.BusinessUnit;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Division {


    private String id;

    private String divisionName;
    private List<String> selectBussinessUnitsID;
    private String costCentreID;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public List<String> getSelectBussinessUnitsID() {
        return selectBussinessUnitsID;
    }

    public void setSelectBussinessUnitsID(List<String> selectBussinessUnitsID) {
        this.selectBussinessUnitsID = selectBussinessUnitsID;
    }

    public String getCostCentreID() {
        return costCentreID;
    }

    public void setCostCentreID(String costCentreID) {
        this.costCentreID = costCentreID;
    }

    public List<NameValuePair> toMap(){
        List<NameValuePair> map =new ArrayList<>();

        map.add(new BasicNameValuePair("TenantDivision[division_name]",getDivisionName()));

        if (selectBussinessUnitsID != null) {
            for (String businessUnitID : selectBussinessUnitsID) {
                map.add(new BasicNameValuePair("TenantDivision[business_units][]",businessUnitID));
            }
        }

        if(costCentreID!=null)
        {
            map.add(new BasicNameValuePair("TenantDivision[cost_center]",costCentreID));
        }

        return  map;
    }

}
