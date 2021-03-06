package com.darwinbox.core.employee.objects;

import com.darwinbox.Services;
import com.darwinbox.core.services.BusinessUnitServices;

import java.util.HashMap;
import java.util.Map;

public class Department {
    private String id;


    private  String departmentName;
    private String departmentCode;
    private  String businessUnit;
    private String departmentEmail;
    private String parentDepartment;
    private String groupCompany;
    private String headOfDepartment;
    private String performanceHeadOfDepartment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getDepartmentEmail() {
        return departmentEmail;
    }

    public void setDepartmentEmail(String departmentEmail) {
        this.departmentEmail = departmentEmail;
    }

    public String getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(String parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public String getPerformanceHeadOfDepartment() {
        return performanceHeadOfDepartment;
    }

    public void setPerformanceHeadOfDepartment(String performanceHeadOfDepartment) {
        this.performanceHeadOfDepartment = performanceHeadOfDepartment;
    }




    public Map<String,String> toMap() {

        Map<String, String> data = new HashMap<>();

        data.put("UserDepartments[department_name]", getDepartmentName());
        data.put("UserDepartments[department_email]", getDepartmentEmail());
        data.put("UserDepartments[department_code]",getDepartmentCode());
        //data.put("UserDepartments[parent_company_id]", "");

       // data.put("UserDepartments[business_unit_id]", new BusinessUnitServices().getBusinessUnits().get(getBusinessUnit()));
        //YTD
        data.put("UserDepartments[parent_department]", "");
        data.put("UserDepartments[departments_hod]", getHeadOfDepartment());
        //data.put("UserDepartments[performance_hod]", "");

        return data;
    }

    public void toObject(Map<String, String> data){


        setDepartmentName(data.get("DepartmentName"));
        setDepartmentCode("");
        setDepartmentEmail("");
        setParentDepartment("");
        setHeadOfDepartment("");
    }

}
