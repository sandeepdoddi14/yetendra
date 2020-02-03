package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.ReferAndIJP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class ReferAndIJPService extends Services {

/*Below method returns jobID that can be referred by passing job title as parameter*/

    public String searchJobsInReferPage(String title) {

        ReferAndIJP referAndIJP = new ReferAndIJP();
        String url = getData("@@url") + "/recruitment/recruitment/JobsActiveEmployeePagination";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(referAndIJP.toMap());
        String response = doPost(url, headers, mapToFormData(body));

        String jobIDReferPage = "";
        JSONObject obj = new JSONObject(response);

        int totalJobs= (int) obj.get("recordsTotal");

        for(int i=0;i<totalJobs;i++) {
            String ids = obj.getJSONArray("data").getJSONObject(i).get("role").toString();
            Document doc = Jsoup.parse(ids);
            String titleOnReferPage = String.valueOf(doc.body().getElementsByTag("a").html());

            if(titleOnReferPage.equalsIgnoreCase(title)){

                String aa = String.valueOf(doc.body().getElementsByTag("a"));
                String[] b = aa.split("/");
                String[] c =b[5].split("\"");
                jobIDReferPage= c[0];
            }
        }
        return jobIDReferPage;
    }

    /*Below method returns jobID that present in IJP by passing job title as parameter*/

    public String searchJobsInIJPPage(String title) {

        ReferAndIJP referAndIJP = new ReferAndIJP();
        String url = getData("@@url") + "/recruitment/recruitment/JobsIjpEmployeePagination";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(referAndIJP.toMap());
        body.putAll(referAndIJP.toMapIJP());

        String response = doPost(url, headers, mapToFormData(body));

        String jobIDIJPPage = "";
        JSONObject obj = new JSONObject(response);

        int totalJobs= (int) obj.get("recordsTotal");

        for(int i=0;i<totalJobs;i++) {
            String ids = obj.getJSONArray("data").getJSONObject(i).get("role").toString();
            Document doc = Jsoup.parse(ids);
            String titleOnIJPPage = String.valueOf(doc.body().getElementsByTag("a").html());

            if(titleOnIJPPage.equalsIgnoreCase(title)){

                String aa = String.valueOf(doc.body().getElementsByTag("a"));
                String[] b = aa.split("/");
                String[] c =b[5].split("\"");
                jobIDIJPPage= c[0];
            }
        }
        return jobIDIJPPage;

    }

    }
