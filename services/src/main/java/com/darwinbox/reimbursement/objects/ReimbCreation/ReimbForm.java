package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReimbForm extends Services {
    private String name;
    private String description;
    private String GrpCompany;
    private String units;
    private String approvalFlow;
    private String ledger;
    private String id = "";
    private String currency;
    private List<String> applicableToList = new ArrayList<>();
    private List<ReimbLimitsBody> reimbLimitsBodyList = new ArrayList<>();

    public List<String> getApplicableToList() {
        return applicableToList;
    }

    public void setApplicableToList(List<String> applicableToList) {
        this.applicableToList = applicableToList;
    }

    public List<ReimbLimitsBody> getReimbLimitsBodyList() {
        return reimbLimitsBodyList;
    }

    public void setReimbLimitsBodyList(List<ReimbLimitsBody> reimbLimitsBodyList) {
        this.reimbLimitsBodyList = reimbLimitsBodyList;
    }

    public enum currency {
        RUPEE, DOLLAR, POUNDS, AED, EURO, PHP, SGD, IDR, HKD, AUD, BDT, VND, CNY, TWD, JPY, MYR, KRW, KHR
    }

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

    public String getGrpCompany() {
        return GrpCompany;
    }

    public void setGrpCompany(String grpCompany) {
        GrpCompany = grpCompany;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
    //unit IDs have to be mapped, check in services, ID is required from units page

    public String getApprovalflow() {
        return approvalFlow;
    }

    public void setApprovalflow(String approvalflow) {
        this.approvalFlow = approvalflow;
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void add(String applicableTo) {
        applicableToList.add(applicableTo);
    }

    public void add(ReimbLimitsBody formBody) {
        reimbLimitsBodyList.add(formBody);
    }

    //to convert excel values to ReimbForm java object
    public void toObject(Map<String, String> data) {
        setName(data.get("Name"));
        setDescription(data.get("Description"));
        setApprovalflow(data.get("Applicable"));
        setApprovalflow(data.get("Units"));
        setApprovalflow(data.get("Approval_Flow"));
        setApprovalflow(data.get("Ledger"));
    }
    //check for Band method: all those fields which take MOngo ID as input
    //this method used to set (java object) values to web application
    //boolean values to be passed thru excel sheet
    /**
     * this method used to set (java object) values to web application
     *
     * @return
     */
    public List<NameValuePair> toMap() {
        List<String> applicableToList = new ArrayList<>();
        List<NameValuePair> body = new ArrayList<>();

        body.add(new BasicNameValuePair("reimb_id", getId()));
        body.add(new BasicNameValuePair("TenantReimbursement[name]", getName()));
        body.add(new BasicNameValuePair("TenantReimbursement[description]", getDescription()));
        body.add(new BasicNameValuePair("TenantReimbursement[currency_allowed]", getCurrency()));
        body.add(new BasicNameValuePair("TenantReimbursement[parent_company_id]", getGrpCompany()));
        body.add(new BasicNameValuePair("TenantReimbursement[approval_flow]", getApprovalflow()));
        body.add(new BasicNameValuePair("TenantReimbursement[units]", getUnits()));
        body.add(new BasicNameValuePair("TenantReimbursement[ledger]", getLedger()));

        for(String applicableTo : getApplicableToList())
        {
            body.add(new BasicNameValuePair("TenantReimbursement[applicable][]",applicableTo));
        }

        int count=0;
        for(ReimbLimitsBody reimbLimitsBody : reimbLimitsBodyList)
        {

            body.addAll(body.size(), reimbLimitsBody.toMap(count));
            count++;
        }
            //try with Hashmapinstaed of list types here, for single values.
            //check for Dept, location, emp type, band, grade
        return body;
    }
}

