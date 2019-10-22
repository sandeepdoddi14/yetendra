package com.darwinbox.core.employee.objects;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.core.Services;
import com.darwinbox.core.services.CostCenterServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusinessUnit {
    private String id;
    private  String businessunitType;
    private String businessUnitAddress;
    private String groupCompany;
    private String costCenter;

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    private String businessUnitName;

    public List<Employee> getHeadOFBusniessUnits() {
        return headOFBusniessUnits;
    }

    public void setHeadOFBusniessUnits(List<Employee> headOFBusniessUnits) {
        this.headOFBusniessUnits = headOFBusniessUnits;
    }

    List<Employee> headOFBusniessUnits=new ArrayList<>();

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessunitType() {
        return businessunitType;
    }

    public void setBusinessunitType(String businessunitType) {
        this.businessunitType = businessunitType;
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
        body.put("TenantBusinessUnitForm[unit_name]",getBusinessunitType());

        if(getBusinessUnitAddress()!=null)
        body.put("TenantBusinessUnitForm[unit_address]",getBusinessUnitAddress());


        if(new Services().getGroupCompanyIds().get(getGroupCompany())==null)
            body.put("TenantBusinessUnitForm[parent_company_id][]","");
        else
            body.put("TenantBusinessUnitForm[parent_company_id][]",new Services().getGroupCompanyIds().get(getGroupCompany()));

        body.put("TenantBusinessUnitForm[cost_center]",new CostCenterServices().getCostCenters().get(getCostCenter()));

        String heads="";
        for (Employee employee: getHeadOFBusniessUnits())
        {
            heads = heads +"," + employee.getCandidateID();

        }

        if(heads.length()!=0)
        heads = heads.substring(1);
        body.put("TenantBusinessUnitForm[bu_heads]",heads);

        return  body;
    }
}
