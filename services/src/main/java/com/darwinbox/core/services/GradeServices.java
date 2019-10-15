package com.darwinbox.core.services;
import com.darwinbox.core.Services;
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.employee.objects.Grade;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GradeServices extends Services {

    public Map<String,String> getDefaultforBand(){
        Map<String,String> body = new HashMap<>();

        body.put("UserGrade[grade_name]","");
        body.put("UserGrade[descriptions]","Automation Created Band");
        body.put("UserGrade[band_name]","");

        return  body;
    }


    public void createService(Grade grade) {

        Map<String, String> body = getDefaultforBand();

        body.putAll(grade.toMap());


        String url = getData("@@url") + "/settings/company/grades";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateGrade(Grade grade) {
        Map<String, String> body = getDefaultforBand();
        body.putAll(grade.toMap());


        HashMap<String,String> grades=getGrades();
        String  id=grades.get(grade.getGradeName());

        if(id!=null){
            body.put("UserGrade[id]",id);
        }
        else
            throw new RuntimeException("There is no Grade to update Grade Name="+grade.getGradeName());

        String url = getData("@@url") + "/settings/editGrades";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }


    /*
     gets grades
      */
    public HashMap<String, String> getGrades() {
        String url = data.get("@@url") + "/settings/GetGrades";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String value = arr.getJSONArray(i).getString(2).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }


    public void deleteGrade(Grade grade){
       String gradeId=getGrades().get(grade.getGradeName());

       String url = getData("@@url") + "/settings/editGrades";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",gradeId);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));

    }


}


