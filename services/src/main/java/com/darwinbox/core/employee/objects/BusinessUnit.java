package com.darwinbox.core.employee.objects;

import com.darwinbox.core.Services;

import java.util.HashMap;

public class BusinessUnit {
    private String id;
    private  String businessUnitName;
    private String businessUnitAddress;
    private String groupCompany;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public String getBusinessUnitAddress() {
        return businessUnitAddress;
    }

    public void setBusinessUnitAddress(String businessUnitAddress) {
        this.businessUnitAddress = businessUnitAddress;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }




    public HashMap<String,String> toMap(){
        HashMap<String,String> body= new HashMap<>();
        body.put("TenantBusinessUnitForm[unit_name]",getBusinessUnitName());

        if(getBusinessUnitAddress()!=null)
        body.put("TenantBusinessUnitForm[unit_address]",getBusinessUnitAddress());


        body.put("TenantBusinessUnitForm[parent_company_id][]",new Services().getGroupCompanyIds().get(getGroupCompany()));



        return  body;
    }
}
