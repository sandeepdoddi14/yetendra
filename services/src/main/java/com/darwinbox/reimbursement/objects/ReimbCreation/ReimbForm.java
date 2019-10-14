package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReimbForm extends Services
{
    private String name;
    private String description;
    private String GrpCompany;
    private String units;
    private String approvalFlow;
    private String id ="";

    /*public List<String> getApplicableToList() {
        return applicableToList; }
    public void setApplicableToList(List<String> applicableToList) {
        this.applicableToList = applicableToList; }*/
    private List<String> currency = new ArrayList<>();

    private List<String> applicableToList = new ArrayList<>();
    private List<ReimbFormBody> reimbFormBodyList = new ArrayList<>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {   return id;  }
    public void setId(String id) {
        this.id = id;
    }

    public String getGrpCompany() {  return GrpCompany;   }
    public void setGrpCompany(String grpCompany) { GrpCompany = grpCompany;  }

    public String getUnits() {    return units;   }
    public void setUnits(String units) {  this.units = units;  }
    //unit IDs have to be mapped, check in services, ID is required from units page

    public String getApprovalflow() {  return approvalFlow;  }
    public void setApprovalflow(String approvalflow) {   this.approvalFlow = approvalflow;  }
//make currency as Enum
    public List<String> getCurrency() {  return currency;  }
    public void setCurrency(List<String> currency) {   this.currency = currency;   }

    public void add(String applicableTo){ applicableToList.add(applicableTo);   }
    public void add(ReimbFormBody formBody){  reimbFormBodyList.add(formBody);  }

/*    //to convert excel values to ReimbForm java object
    public void toObject(Map<String,String> data)
    {
        setName(data.get("Name"));
        setDescription(data.get("Description"));

    }
//check for Band method: all those fields which take MOngo ID as input
    //this method used to set (java object) values to web application
    //boolean values to be passed thru excel sheet

    public List<NameValuePair> toMap()
    {
        //Map ReimbFormBody values directly
        List<String> applicableToList = new ArrayList<>();
        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id" , getId()));
        formData.add(new BasicNameValuePair("TenantReimbursement[name]" , getName()));
        formData.add(new BasicNameValuePair("TenantReimbursement[description]" , getDescription()));
        *//*formData.add(new BasicNameValuePair("TenantReimbursement[currency_allowed]" , getCurrency()));
        formdata.add(new BasicNameValuePair("TenantReimbursement[parent_company_id]" , getGrpCompany()));
        formdata.add(new BasicNameValuePair("TenantReimbursement[applicable]", getApprovalflow()));
        formdata.add(new BasicNameValuePair("TenantReimbursement[units]", getUnits()));

        int count=0;
        for(String applicableTo : applicableToList){
            count++;
            formData.addAll(formData.size(),applicableTo))*//*
        //try with Hashmapinstaed of list types here, for single values.
        }
        return formData*/;
    }