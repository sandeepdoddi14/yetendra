package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetApplicableTo extends Services {


    public Map<String, List<String>> getAllApplicableTo(String companyId) {

        Map<String, List<String>> data = new HashMap();

        String url = data.get("@@url") + "/Customflow/loadRestrictOtherReimbApplicable?id=" + companyId + "&all=1&is_assignment=1";

        JSONObject objResponse = new JSONObject(doGet(url, null)).getJSONObject("update");

        List<String> grades = (List<String>) getGrades().values();
        List<String> band = (List<String>) getBands().values();
        List<String> employeeTypes = (List<String>) getEmployeeTypes().values();

        List<String> dept = (List<String>) getDepartments(companyId).values();
        // TODO    List<String> desg= (List<String>) getDesignations(companyId).values();
        // TODO   data.put("desg",grades);
        List<String> loc = (List<String>) getOfficeLocations(companyId).values();

        data.put("grade", grades);
        data.put("band", band);
        data.put("empType", employeeTypes);
        data.put("dept", dept);
        data.put("loc", loc);


        return data;
    }
}




