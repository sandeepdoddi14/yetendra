package com.darwinbox.core.employee.objects;

import java.util.HashMap;
import java.util.Map;

public class Department {
    private String id;


    private  String departmentName;
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




    public Map<String,String> toMap(){

        Map<String,String> data = new HashMap<>();

        data.put("UserDepartments[department_name]",getDepartmentName());
        data.put("UserDepartments[department_email]", getDepartmentEmail());
        //get parent comany id
        data.put("UserDepartments[parent_company_id]","");

        data.put("UserDepartments[business_unit_id]", "");
        data.put("UserDepartments[parent_department]","");
        data.put("UserDepartments[departments_hod]", "");
        data.put("employee_search", "");
        data.put("UserDepartments[performance_hod]", "");
        data.put("employee_search", "");

        return data;
    }
}
