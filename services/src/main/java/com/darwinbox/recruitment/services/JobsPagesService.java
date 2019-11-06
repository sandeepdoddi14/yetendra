package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.objects.jobsPages.HiringTeam;
import com.darwinbox.recruitment.objects.jobsPages.HiringWFThirdPage;
import com.darwinbox.recruitment.objects.jobsPages.JobApplication;
import com.darwinbox.recruitment.objects.jobsPages.JobPosting;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsPagesService extends Services {

    /*Below method is to post job, page-1*/

    public void jobPosting(Requisition requisition,String id){

        String url = getData("@@url") + "/recruitment/recruitment/requisitionstageone";

        Map<String, String> body = new HashMap<>();
        body.put("id",id);
        body.put("jd["+id+"]","Please enter job description");
        body.putAll(new JobPosting().toMapfirstPage(requisition));

        doPost(url, null,mapToFormData(body));

    }

    /*Below method is to post job, page-2*/

    public void jobApplication(JobApplication jobApplication,String id) {

        String url = getData("@@url") + "/recruitment/recruitment/requisitionstagetwo";

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("id",id));
        list.addAll(jobApplication.toMapSecondPage());

        doPost(url, null,list);
    }

    /*Below method is to post job, HiringWorkflow, page-3*/

    public void hiringWorkFlow(HiringWFThirdPage hiringWFThirdPage,String id){

        String url = getData("@@url") + "/recruitment/recruitment/requisitionstagethree/id/"+id+"/edit/1";

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("id",id));
        list.addAll(hiringWFThirdPage.toMapThirdPage());

        doPost(url, null,list);

    }

    /*Below method is to post job, HiringTeam Page, page-4*/

    public void hiringTeam(HiringTeam hiringTeam, String id){

        String url = getData("@@url") + "/recruitment/recruitment/requisitionstagefour";

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("id",id));
        list.addAll(hiringTeam.toMapFourthPage());

        doPost(url, null,list);

    }

    }
