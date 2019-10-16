package com.darwinbox.core.services;
import com.darwinbox.core.Services;
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.employee.objects.Department;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DepartmentServices extends Services {

    public HashMap<String,String> defaultBody(){
        HashMap<String,String> body= new HashMap<>();

        body.put("UserDepartments[department_name]","");
        body.put("UserDepartments[department_email]","");
        body.put("UserDepartments[parent_company_id]","");
        body.put("UserDepartments[business_unit_id]","");
        body.put("UserDepartments[parent_department]","");
        body.put("UserDepartments[departments_hod]","");
        body.put("employee_search","");

        return body;
    }


    public void updateDepartment(Department department) {
        Map<String, String> body = defaultBody();
        body.putAll(department.toMap());


        HashMap<String,String> departments=getDepartments();
        String  id=departments.get(department.getDepartmentName());

        if(id!=null){
            body.put("UserDepartments[id]",id);
        }
        else
            throw new RuntimeException("There is no department to update department Name="+department.getDepartmentName());

        String url = getData("@@url") + "/settings/editDept";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }

    public void createDepartment(Department department){


            Map<String, String> body = defaultBody();

            body.putAll(department.toMap());


            String url = getData("@@url") + "/settings/company/departments";

            Map headers = new HashMap();
            headers.put("x-requested-with", "XMLHttpRequest");

            doPost(url, headers, mapToFormData(body));

    }


    /*
  get departments
   */
    public HashMap<String, String> getDepartments() {
        String url = data.get("@@url") + "/settings/getDepartments";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String value = arr.getJSONArray(i).getString(1).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }

    public void deleteDepartment(Department department){
        String departmentID=getGrades().get(department.getDepartmentName());

        String url = getData("@@url") + "/company/departments";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",departmentID);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));

    }


}


