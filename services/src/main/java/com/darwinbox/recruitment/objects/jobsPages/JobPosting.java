package com.darwinbox.recruitment.objects.jobsPages;

import com.darwinbox.recruitment.objects.Requisition;

import java.util.HashMap;
import java.util.Map;

public class JobPosting  {


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

    public Map<String, String> toMapfirstPage(Requisition requisition) {

        Map<String, String> body = new HashMap<>();

        body.put("edit","1");
        body.put("RaiseRequisition[parent_company_id]",requisition.getCompanyName());
        body.put("RaiseRequisition[designation]",requisition.getDesignationAndDepartment());
        body.put("RaiseRequisition[officelocation_arr][]",requisition.getLocations());
        body.put("RaiseRequisition[experience_from]",requisition.getExpFrom());
        body.put("RaiseRequisition[experience_to]",requisition.getExpTo());
        body.put("RaiseRequisition[experience_yrs_month]",requisition.getYearsOrMonths().s);
        body.put("RaiseRequisition[target_tat]","");
        body.put("RaiseRequisition[employee_type]",requisition.getEmployeeType());
        body.put("RaiseRequisition[designation_display_name]","");
        body.put("RaiseRequisition[key_skills]",requisition.getSkillsRequired());
        body.put("RaiseRequisition[salary_currency]",requisition.getCurrency());
        body.put("RaiseRequisition[salary_min]",requisition.getMinSalary());
        body.put("RaiseRequisition[salary_max]",requisition.getMaxSalary());
        body.put("RaiseRequisition[job_details_updated]","");
        body.put("RaiseRequisition[post_on_carrers_page]","");
        body.put("RaiseRequisition[post_on_refer_page]","");
        body.put("RaiseRequisition[post_on_ijp_page]","");
        body.put("RaiseRequisition[internal_job_code]","");

        return body;

    }

    public void toObjectFirstPage(Map<String,String> body) {

           //to post on careers page objects?

    }
    }
