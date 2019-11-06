package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.objects.jobsPages.JobPosting;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;

public class RequisitionService extends Services {


    public void createRequisition(Requisition requisition){

        String url = getData("@@url") + "/recruitment/recruitment/requestedrequisition";

        List<NameValuePair> list = new ArrayList<>();
        list.addAll(requisition.toMap());

        doPost(url, null,list);
    }

    /*The below method returns requisition ID*/

    public String searchRequisition(Requisition requisition){

        String url = getData("@@url") + "/recruitment/recruitment/RequisitionData";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(requisition.toMapSearch());
        String response = doPost(url, headers,mapToFormData(body));

        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("data");
        String id = arr.getJSONObject(0).get("requisition_id").toString();

        /*String a="";
        StringUtils.substringsBetween(a,"\">","<\\");
*/
        return id.replaceAll("\\<.*?>", "");

        }


    /*The below method returns job ID*/


    public String searchJobsPage(Requisition requisition){

        String url = getData("@@url") + "/recruitment/recruitment/GetIndexPaginatedData";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(requisition.toMapJobSearch());
        String response = doPost(url, headers,mapToFormData(body));

        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("data");
        String id = arr.getJSONObject(0).get("internal_job_code").toString();

        Document doc = Jsoup.parse(id);
        String reqID = doc.body().getElementsByTag("div").attr("id");

        return reqID;

    }

    /*The below method returns id's of active jobs*/

    public String searchActiveJobs(){

        Requisition requisition = new Requisition();
        String url = getData("@@url") + "/recruitment/recruitment/GetIndexPaginatedData";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(requisition.toMapActiveJobSearch());
        String response = doPost(url, headers,mapToFormData(body));

        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("data");

      String ids= obj.getJSONArray("data").getJSONObject(new Random().nextInt(arr.length())).get("internal_job_code").toString();

        Document doc = Jsoup.parse(ids);
        String jobID = doc.body().getElementsByTag("div").attr("id");

        return jobID;
    }

     /*Below method Posts job page-4*/

    public void postJob(Requisition requisition){

        String url = getData("@@url") + "/recruitment/recruitment/requisitionstagefour";

        Map<String, String> body = new HashMap<>();
        body.putAll(requisition.toMapPostJob());

        doPost(url, null,mapToFormData(body));

    }

    /*Below method is to post job, page-1*/

    public void jobPosting(String id){

        String url = getData("@@url") + "/recruitment/recruitment/requisitionstageone";

        Map<String, String> body = new HashMap<>();
       // body.putAll(requisition.toMapfirstPage());

        body.put("id",new JobPosting().getId());
       // body.put("jd["+getId()+"]",id);

        doPost(url, null,mapToFormData(body));

    }

}
