package com.darwinbox.core.employee.objects;

import java.util.HashMap;

public class FunctionalArea {
    private String id;

    private String  functionalAreaName;
    private String description;

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    private String functionalAreaCode;
    private String costCenter;
    private String effectiveDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFunctionalAreaName() {
        return functionalAreaName;
    }

    public void setFunctionalAreaName(String functionalAreaName) {
        this.functionalAreaName = functionalAreaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFunctionalAreaCode() {
        return functionalAreaCode;
    }

    public void setFunctionalAreaCode(String functionalAreaCode) {
        this.functionalAreaCode = functionalAreaCode;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }



    public HashMap<String,String> toMap(){
        HashMap<String,String> body=new HashMap<>();
        body.put("FunctionalArea[name]",getFunctionalAreaName());
        body.put("FunctionalArea[description]",getDescription());
        //YTD
        body.put("FunctionalArea[code]","");
        body.put("FunctionalArea[cost_center]","");

        return body;
    }}
