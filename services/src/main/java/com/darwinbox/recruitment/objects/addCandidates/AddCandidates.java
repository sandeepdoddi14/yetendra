package com.darwinbox.recruitment.objects.addCandidates;

import com.darwinbox.Services;
import com.darwinbox.recruitment.services.RequisitionService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCandidates extends Services {

    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();

        body.put("draw","1");
        body.put("columns[0][data]","checkbox");
        body.put("columns[0][name]","");
        body.put("columns[0][searchable]","true");
        body.put("columns[0][orderable]","false");
        body.put("columns[0][search][value]","");
        body.put("columns[0][search][regex]","false");

        body.put("columns[1][data]","name");
        body.put("columns[1][name]","");
        body.put("columns[1][searchable]","true");
        body.put("columns[1][orderable]","true");
        body.put("columns[1][search][value]","");
        body.put("columns[1][search][regex]","false");

        body.put("columns[2][data]","candidate_id");
        body.put("columns[2][name]","");
        body.put("columns[2][searchable]","true");
        body.put("columns[2][orderable]","true");
        body.put("columns[2][search][value]","");
        body.put("columns[2][search][regex]","false");

        body.put("columns[3][data]","email_phone");
        body.put("columns[3][name]","");
        body.put("columns[3][searchable]","true");
        body.put("columns[3][orderable]","true");
        body.put("columns[3][search][value]","");
        body.put("columns[3][search][regex]","false");

        body.put("columns[4][data]","status");
        body.put("columns[4][name]","");
        body.put("columns[4][searchable]","true");
        body.put("columns[4][orderable]","false");
        body.put("columns[4][search][value]","");
        body.put("columns[4][search][regex]","false");

        body.put("columns[5][data]","stack_rank");
        body.put("columns[5][name]","");
        body.put("columns[5][searchable]","true");
        body.put("columns[5][orderable]","true");
        body.put("columns[5][search][value]","");
        body.put("columns[5][search][regex]","false");

        body.put("columns[6][data]","application_date");
        body.put("columns[6][name]","");
        body.put("columns[6][searchable]","true");
        body.put("columns[6][orderable]","true");
        body.put("columns[6][search][value]","");
        body.put("columns[6][search][regex]","false");

        body.put("columns[7][data]","application");
        body.put("columns[7][name]","");
        body.put("columns[7][searchable]","true");
        body.put("columns[7][orderable]","false");
        body.put("columns[7][search][value]","");
        body.put("columns[7][search][regex]","false");

        body.put("columns[8][data]","source");
        body.put("columns[8][name]","");
        body.put("columns[8][searchable]","true");
        body.put("columns[8][orderable]","false");
        body.put("columns[8][search][value]","");
        body.put("columns[8][search][regex]","false");

        body.put("columns[9][data]","actions");
        body.put("columns[9][name]","");
        body.put("columns[9][searchable]","true");
        body.put("columns[9][orderable]","false");
        body.put("columns[9][search][value]","");
        body.put("columns[9][search][regex]","false");

        body.put("order[0][column]","candidate_id");
        body.put("order[0][dir]","desc");
        body.put("start","0");
        body.put("length","10");
        body.put("search[value]","");
        body.put("search[regex]","false");
        body.put("filterid","total");

        body.put("page_load","true");

        return body;
    }

    /*Below method returns JobID, for which candidates been added*/

    String jobID ;

    public String filter(){

        String url = getData("@@url") + "/recruitment/recruitment/filtercandidates";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        jobID= new RequisitionService().searchActiveJobs();
        body.put("job_id",jobID);
        body.putAll(toMap());

        String response = doPost(url, headers,mapToFormData(body));

        if(!response.contains("\n"))
           Reporter("Candidate is NOT added","ERROR");
        else
        Reporter("Candidate is added","INFO");

        return jobID;
    }

    /*Below method returns added Candidates ID*/
    public String filterByGetAddedCandidates(){

        String url = getData("@@url") + "/recruitment/recruitment/filtercandidates";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("job_id",jobID);
        body.putAll(toMap());

        String response = doPost(url, headers,mapToFormData(body));
        driver.navigate().refresh();
        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("data");

        String id = arr.getJSONObject(0).get("id").toString();

        return id;
    }

     /*Below method updates added candidate details*/

    public void addCandidateDetails(String jobID, String candidateID){

    Map<String,String> body= new HashMap<>();
    body.put("RecruitmentApplicants[id]","");

    /*RecruitmentApplicants[id]: 5dcbc8a3df00e
RecruitmentApplicants[job_id]: 5dbab1245c9da
JobApplyForm[firstname]: check
JobApplyForm[lastname]: now
JobApplyForm[source]: 3
JobApplyForm[phone]: 7418529630
JobApplyForm[email]: snj@yopmail.com
JobApplyForm[recruiter_name]: 5d7f2df845d2b
JobApplyForm[job_portal_name]: 5d76025f3d238
JobApplyForm[refered_by]:
*/

    }

    }
