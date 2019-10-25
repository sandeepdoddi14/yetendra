package com.darwinbox.recruitment.objects.jobsPages;

import com.darwinbox.recruitment.objects.Requisition;

import java.util.HashMap;
import java.util.Map;

public class JobPosting extends Requisition {


    //extends requisition, as values are same for different keys

    /*jd_c: Please enter job description
id: 5da595867631b
edit: 1
RaiseRequisition[parent_company_id]: 5a92fef2852b2
RaiseRequisition[designation]: 5a92ff94a4734
RaiseRequisition[officelocation_arr][]: 5af86a749bfe2
RaiseRequisition[experience_from]: 1
RaiseRequisition[experience_to]: 4
RaiseRequisition[experience_yrs_month]: Years
RaiseRequisition[target_tat]:
RaiseRequisition[employee_type]: 5af6be4f7e311
RaiseRequisition[designation_display_name]:
RaiseRequisition[key_skills]: PHP
RaiseRequisition[salary_currency]: INR
RaiseRequisition[salary_min]: 50000
RaiseRequisition[salary_max]: 75000
RaiseRequisition[job_details_updated]: 1
RaiseRequisition[post_on_carrers_page]: 0
RaiseRequisition[post_on_refer_page]: 0
RaiseRequisition[post_on_ijp_page]: 0
RaiseRequisition[internal_job_code]:
jd[5da595867631b]: Please enter job description*/


//capture how to activate jobs on page-1 using api

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Map<String, String> toMapfirstPage() {


        Map<String, String> body = new HashMap<>();

        body.put("id",getId());
        body.put("","");
        body.put("",getCompanyName());
        body.put("",getDesignationAndDepartment());
        body.put("",getLocations());
        body.put("",getExpFrom());
        body.put("",getExpTo());
        body.put("",getYearsOrMonths().s);
        body.put("","");
        body.put("",getEmployeeType());
        body.put("","");
        body.put("",getSkillsRequired());
        body.put("",getCurrency());
        body.put("",getMinSalary());
        body.put("",getMaxSalary());
        body.put("","");
        body.put("jd["+getId()+"]","Please enter job description");


        return body;

    }


    }
