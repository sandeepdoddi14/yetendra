package com.darwinbox.core.services;

import com.darwinbox.Services;
import com.darwinbox.core.company.objects.GroupCompany;
import com.darwinbox.core.company.objects.JobDescription;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobDescriptionServices extends Services {

    public void createJobDescription(JobDescription jobDescription) {

        Map<String, String> body = jobDescription.toMap();


        String url = getData("@@url") + "/JobDescription/editjobdescription";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateJobDescription(JobDescription jobDescription) {
        Map<String,String> body= jobDescription.toMap();
        /* Map<String, String> body = getDefaultforBand();
         */
        //body.putAll(grade.toMap());
/*

        HashMap<String,String> grades=getGrades();
        String  id=grades.get(grade.getGradeName());*/

        if(jobDescription.getId()!=null){
            body.put("JobDescription[id]",jobDescription.getId());
        }
        else
            throw new RuntimeException("There is no Job Description ID to update Job Description title="+jobDescription.getJobDescriptionTitle());

        String url = getData("@@url") + "/JobDescription/editjobdescription";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }


    public HashMap<String, String> getJobDescriptions() {
        String url = data.get("@@url") + "/JobDescription/GetJobDescriptions";

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

    public void deleteJobDescription(JobDescription jobDescription){
        String id=getJobDescriptions().get(jobDescription.getJobDescriptionTitle());

        String url = getData("@@url") + "/JobDescription/editjobdescription";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",id);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));

    }

}
