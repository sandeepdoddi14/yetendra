package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class ReimbForm extends Services {
    private String name;
    private String description;
    private String GrpCompany;
    private String units;
    private String approvalFlow;
    private String ledger;
    private String id;

    public enum CURRENCY {
        RUPEE, DOLLAR, POUNDS, AED, EURO, PHP, SGD, IDR, HKD, AUD, BDT, VND, CNY, TWD, JPY, MYR, KRW, KHR
    }

    private List<String> applicableToList = new ArrayList<>();

    private List<CURRENCY> currencyList = new ArrayList<>();
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

    public List<CURRENCY> getCurrencyList() {  return currencyList; }

    public void setCurrencyList(List<CURRENCY> currencyList) { this.currencyList = currencyList; }

    public void addCurrency(String currency) {
    CURRENCY.valueOf(currency);
    }

    public void addApplicableTo(String applicableTo) {
        applicableToList.add(applicableTo);
    }
    public void addReimbLimits(ReimbLimitsBody formBody) {
        reimbLimitsBodyList.add(formBody);
    }

    //to convert excel values to ReimbForm java object
    public void toObject(Map<String, String> data) {
        setName(data.get("Name"));
        setDescription(data.get("Description"));
        setGrpCompany(data.get("GroupCompany"));
        setUnits(data.get("Units"));
        setApprovalflow(data.get("Approval_Flow"));
        setLedger(data.get("Ledger"));
    }

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
        body.add(new BasicNameValuePair("TenantReimbursement[parent_company_id]", getGrpCompany()));
        body.add(new BasicNameValuePair("TenantReimbursement[approval_flow]", getApprovalflow()));
        body.add(new BasicNameValuePair("TenantReimbursement[units]", getUnits()));
        body.add(new BasicNameValuePair("TenantReimbursement[ledger]", getLedger()));

        for (CURRENCY cur : currencyList) {
            body.add(new BasicNameValuePair("TenantReimbursement[currency_allowed][]", cur.toString()));
        }

        for (String applicableTo : getApplicableToList()) {
            body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", applicableTo));
        }

        int count = 0;
        for (ReimbLimitsBody reimbLimitsBody : reimbLimitsBodyList) {

            body.addAll(body.size(), reimbLimitsBody.toMap(count));
            count++;
        }

        return body;
    }
}

