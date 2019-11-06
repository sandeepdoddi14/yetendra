package com.darwinbox.core.services;

import com.darwinbox.Services;
import com.darwinbox.core.company.objects.GroupCompany;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GroupComapanyServices extends Services {

    public void createGroupCompany(GroupCompany groupCompany) {

        Map<String, String> body = groupCompany.toMap();

        //body.putAll(grade.toMap());


        String url = getData("@@url") + "/settings/company/grpcompany";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateGroupCompnay(GroupCompany groupCompany) {
        Map<String,String> body= groupCompany.toMap();
        /* Map<String, String> body = getDefaultforBand();
         */
        //body.putAll(grade.toMap());
/*

        HashMap<String,String> grades=getGrades();
        String  id=grades.get(grade.getGradeName());*/

        if(groupCompany.getId()!=null){
            body.put("UserGrade[id]",groupCompany.getId());
        }
        else
            throw new RuntimeException("There is no Group Company  ID to update Group Company Name="+groupCompany.getGroupCompanyName());

        String url = getData("@@url") + "/settings/editGroupCompanies";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }


    public HashMap<String, String> getGroupCompanies() {
        String url = data.get("@@url") + "/settings/getGroupCompanies";

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


    public void deleteGroupCompany(GroupCompany groupCompany){
        String id=getGroupCompanies().get(groupCompany.getGroupCompanyName());

        String url = getData("@@url") + "/settings/editGroupCompanies";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",id);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));

    }

}
