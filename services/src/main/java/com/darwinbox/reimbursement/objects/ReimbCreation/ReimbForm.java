package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.attendance.services.Services;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReimbForm extends Services
{
    private  String name;
    private String description;
    private String GrpCompany;
    private String units;
    private String approvalflow;
    private String currency;
    private String id ="";
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

    public String getApprovalflow() {  return approvalflow;  }

    public void setApprovalflow(String approvalflow) {   this.approvalflow = approvalflow;  }

    public String getCurrency() {  return currency;  }

    public void setCurrency(String currency) {   this.currency = currency;   }

    public void add(ReimbFormBody formBody){    reimbFormBodyList.add(formBody);  }

    public void toObject(Map<String,String> data)
    {
        setName(data.get("Name"));
        setDescription(data.get("Description"));
        setGrpCompany(data.get("GrpCompany"));
        setUnits(data.get(("Units")));
        setApprovalflow(data.get("ApprovalFlow"));
        setCurrency(data.get("Currency"));

    }

    public List<BasicNameValuePair> toMap()
    {
        List<BasicNameValuePair> formdata = new ArrayList<>();

        formdata.add(new BasicNameValuePair("id" , getId()));
        formdata.add(new BasicNameValuePair("TenantReimbursement[name]" , getName()));
        formdata.add(new BasicNameValuePair("TenantReimbursement[description]" , getDescription()));
        formdata.add(new BasicNameValuePair("TenantReimbursement[parent_company_id]" , getGrpCompany()));

        return formdata;
    }



}
