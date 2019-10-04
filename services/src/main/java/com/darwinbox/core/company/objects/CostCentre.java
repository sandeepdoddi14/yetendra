package com.darwinbox.core.company.objects;

import com.darwinbox.attendance.objects.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostCentre {

    private String mongoid;
    private String costcenter_name;
    private String costcenter_id;
    private List<Employee> costcenter_heads = new ArrayList<>();

    public String getName() {
        return costcenter_name;
    }

    public void setName(String costcenter_name) {
        this.costcenter_name = costcenter_name;
    }

    public String getId() {
        return costcenter_id;
    }

    public void setId(String costcenter_id) {
        this.costcenter_id = costcenter_id;
    }

    public List<Employee> getCostcenter_heads() {
        return costcenter_heads;
    }

    public String getMongoid() {
        return mongoid;
    }

    public void setMongoid(String mongoid) {
        this.mongoid = mongoid;
    }

    public void addHeads(Employee employee) {
        costcenter_heads.add (employee);
    }

    public void toObject(Map<String,String> data) {
        setName(data.get("Name"));
        setId(data.get("Id"));
    }

    public Map<String,String> toMap(){

        Map<String,String> data = new HashMap<>();

        data.put("TenantCostCenters[cost_center_name]",getName());
        data.put("TenantCostCenters[cost_center_id]", getId());

        String heads = "";

        for (Employee emp : costcenter_heads) {
            heads = heads + ","  + emp.getUserID();
        }

        if ( heads.length() !=0 )
            heads = heads.substring(1);

        data.put("TenantCostCenters[cc_heads]",heads);

        return data;
    }

}
