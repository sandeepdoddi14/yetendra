package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbForm extends Services {
    public enum CURRENCY {
        RUPEE, DOLLAR, POUNDS, AED, EURO, PHP, SGD, IDR, HKD, AUD, BDT, VND, CNY, TWD, JPY, MYR, KRW, KHR
    }

    public String companyId;
    private String name;
    private String description;
    private String units;//use extra fields as precalculated values
    private String approvalFlow;//use gpcompanies only from .ini files
    private String ledger;
    private String id;
    private String isGpCompany;
    private String GrpCompany;
    private List<String> applicableToList = new ArrayList<>();
    private List<CURRENCY> currencyList = new ArrayList<>();
    private List<ReimbLimitsBody> reimbLimitsBodyList = new ArrayList<>();

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

    public String getIsGpCompany() {
        return isGpCompany;
    }

    public void setIsGpCompany(String isGpCompany) {
        this.isGpCompany = isGpCompany;
    }

    public void addCurrency(String currency) {
        currencyList.add(CURRENCY.valueOf(currency));
    }

    public void addApplicableTo(String applicableTo) {
        applicableToList.add(applicableTo);
    }

    public void add(ReimbLimitsBody reimbLimitsBody) {
        reimbLimitsBodyList.add(reimbLimitsBody);
    }

    public static List<Map<String, String>> readDataFromSheet(String sheetname) {
        Map<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "TestData.xlsx");
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }

    public ReimbForm readReimbData() {
        List<Map<String, String>> rldata = readDataFromSheet("ReimbLimitsBody");
        List<Map<String, String>> rdata = readDataFromSheet("ReimbForm");
        ReimbForm rform = new ReimbForm();
        for (Map<String, String> robj : rdata) {
            String bodyObjects = robj.get("ReimbursementLimitsBody");
            String objectTypes[] = bodyObjects.split(",");

            for (String value : objectTypes) {
                ReimbLimitsBody bdy = new ReimbLimitsBody();
                int index = 0;
                if (value != null)
                    index = Integer.valueOf(value); //this throws number format exception when only 1 entry is passed, why?
                bdy.toObject(rldata.get(index - 1)); //this calls setter multiple times, even if i want that value to be set only once?
                rform.add(bdy);
            }
            rform.toObject(robj);
        }
        return rform;
    }

    public void toObject(Map<String, String> entry) {
        setName(entry.get("Name"));
        setDescription(entry.get("Description"));
        setIsGpCompany(entry.get("IsGroupCompany"));
        //Append '_' at end for all choices given in excel sheet for ReimbForm.
        String tempAppTo = entry.get("ApplicableTo");
        if (tempAppTo.length() != 0) {
            for (String value : tempAppTo.split(",")) {
                addApplicableTo(value);
            }
        }
        String tempCurr = entry.get("CurrencyType");
        if (tempCurr.length() != 0) {
            for (String value : tempCurr.split(",")) {
                addCurrency(value);
            }
        }
    }

    public List<NameValuePair> toMap(String mode) {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("TenantReimbursement[name]", getName()));
        body.add(new BasicNameValuePair("TenantReimbursement[description]", getDescription()));
        body.add(new BasicNameValuePair("TenantReimbursement[units]", ""));
        body.add(new BasicNameValuePair("TenantReimbursement[approval_flow]", ""));
        body.add(new BasicNameValuePair("TenantReimbursement[ledger]", ""));

        for (CURRENCY cur : currencyList) {
            body.add(new BasicNameValuePair("TenantReimbursement[currency_allowed][]", cur.toString()));
        }

        String groupCompanyMongoId = getGroupCompanyIds().get(TestBase.data.get("@@group"));
        companyId = getIsGpCompany().equalsIgnoreCase("yes") ? "" : groupCompanyMongoId;

        String keyAppTo = "TenantReimbursement[applicable][]";
      /*  List<NameValuePair> nvp = chooseApplicableTo(applicableToList, companyId, keyAppTo);
        for (NameValuePair nv : nvp) {
            body.add(nv);
        }
      */  int count = 0;
        for (ReimbLimitsBody reLimitsBody : reimbLimitsBodyList) {
            for (NameValuePair e : reLimitsBody.toMap(count, mode)) {
                body.add(e);
            }
            count++;
        }
        count++;

        if (mode == "create") {
            body.add(new BasicNameValuePair("mode", "create"));
            body.add(new BasicNameValuePair("unit_type_value", ""));
            body.add(new BasicNameValuePair("TenantReimbursement[parent_company_id]", getGrpCompany()));
        }
        if (mode == "edit") {
            body.add(new BasicNameValuePair("mode", "edit"));
            body.add(new BasicNameValuePair("unit_type_value_modal", "No Unit"));
            body.add(new BasicNameValuePair("reimb_id", getId()));
        }
        return body;
    }
}



