package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
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
        setApprovalflow(data.get("TestCaseName"));
        setApprovalflow(data.get("Test_Description"));

    }
    //check for Band method: all those fields which take MOngo ID as input
    //this method used to set (java object) values to web application
    //boolean values to be passed thru excel sheet
    /**
     * this method used to set (java object) values to web application
     *
     * @return
     */
    public Map<String,String> toMap() {
        List<String> applicableToList = new ArrayList<>();

        Map<String, String> body = new HashMap<>();
        body.put("reimb_id", getId());
        body.put("TenantReimbursement[name]", getName());
        body.put("TenantReimbursement[description]", getDescription());
        body.put("TenantReimbursement[currency_allowed]", getCurrency());
        body.put("TenantReimbursement[parent_company_id]", getGrpCompany());
        body.put("TenantReimbursement[approval_flow]", getApprovalflow());
        body.put("TenantReimbursement[units]", getUnits());
        body.put("TenantReimbursement[ledger]", getLedger());

            //try with Hashmapinstaed of list types here, for single values.
            //check for Dept, location, emp type, band, grade

        return body;
    }
}

