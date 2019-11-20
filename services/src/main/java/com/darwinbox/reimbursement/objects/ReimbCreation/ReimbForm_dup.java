package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import com.darwinbox.reimbursement.services.ReimbFormService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;
/*
public class ReimbForm_dup extends Services {
    private String name;
    private String description;
    private String units;//use extra fields as precalculated values
    private String approvalFlow;//use gpcompanies only from .ini files
    private String ledger;
    private String id;
    private String isParent = "No"; //can i use isParent to check if the parent entered in excel is a valid parent or not?use isGroupCompany logic to check: main/null/""
    private String GrpCompany;
    private String groupCompanyMongoId = "";
    private List<String> applicableToList = new ArrayList<>();
    private List<CURRENCY> currencyList = new ArrayList<>();

    private List<Map<String, String>> reimbLimitsBodyList = new ArrayList<>();

    public enum CURRENCY {
        RUPEE, DOLLAR, POUNDS, AED, EURO, PHP, SGD, IDR, HKD, AUD, BDT, VND, CNY, TWD, JPY, MYR, KRW, KHR
    }

    public List<Map<String, String>> getReimbLimitsBodyList() {
        return reimbLimitsBodyList;
    }

    public void setReimbLimitsBodyList(Map<String, String> reimbLimitsBodyList) {
        this.reimbLimitsBodyList.add(reimbLimitsBodyList);
    }

    public List<String> getApplicableToList() {
        return applicableToList;
    }

    public void setApplicableToList(List<String> applicableToList) {
        this.applicableToList = applicableToList;
    }

    public String getName() {  return name;  }

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

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
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

    public List<CURRENCY> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<CURRENCY> currencyList) {
        this.currencyList = currencyList;
    }

    public void addCurrency(String currency) {
        CURRENCY.valueOf(currency);
    }

    public void addApplicableTo(String applicableTo) {
        applicableToList.add(applicableTo);
    }

    public List<Map<String, String>> addReimbTypeData() {
     *//*
        List<Map<String,String>> reimbFormDataList = ReimbFormService.readDataFromSheet("ReimbLimitsBody");

        for (Map<String,String> testdata : reimbFormDataList) {
           reimbFormList.add(getReimbLimitsBodyList(testdata));
        }
     *//*
        return null;
    }

    //to convert excel values to ReimbForm java object
    public void toObject(Map<String, String> data) {
        setName(data.get("Name"));
        setDescription(data.get("Description"));
        setGrpCompany(data.get("GroupCompany"));
        String bodyObjects = data.get("ReimbursementLimitsBody");

        String objectTypes[] = bodyObjects.split(",");
        List<Map<String, String>> reimbLimitsBodyList = ReimbFormService.readDataFromSheet("ReimbLimitsBody");

        for (String value : objectTypes) {
            int index = Integer.parseInt(value) - 1;
            //addReimbLimits(reimbLimitsBodyList.get(index));
            setReimbLimitsBodyList(reimbLimitsBodyList.get(index));
        }
        *//**
         * this method used to set (java object) values to web application
         *
         * @return
         *//*
        public List<NameValuePair> toMap()
    {
            List<NameValuePair> body = new ArrayList<>();

            body.add(new BasicNameValuePair("TenantReimbursement[name]", getName()));
            body.add(new BasicNameValuePair("TenantReimbursement[description]", getDescription()));
            body.add(new BasicNameValuePair("TenantReimbursement[parent_company_id]", getGrpCompany()));
            body.add(new BasicNameValuePair("TenantReimbursement[units]", ""));
            body.add(new BasicNameValuePair("TenantReimbursement[approval_flow]", ""));
            body.add(new BasicNameValuePair("TenantReimbursement[ledger]", "0"));
            body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "ALL_0"));
            //body.add(new BasicNameValuePair("unit_type_value_modal" ,""));

            for (CURRENCY cur : getCurrencyList()) {
                body.add(new BasicNameValuePair("TenantReimbursement[currency_allowed][]", cur.toString()));
            }

            for (String applicableTo : getApplicableToList()) {
                body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", applicableTo));
           *//*int count =0;
            ReimbLimitsBody reimbLimitsBody = new ReimbLimitsBody();
            body.add((NameValuePair) reimbLimitsBody.toMap(count));
            count++;*//*
            }

            int count = 0;
            for (Map<String, String> reimbLimitsBody : reimbLimitsBodyList) {

                body.add(count, (NameValuePair) reimbLimitsBody);
                count++;
            }
*/




