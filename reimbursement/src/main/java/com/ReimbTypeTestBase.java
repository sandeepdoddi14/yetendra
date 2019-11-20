
package com;

import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbLimitsBody;
import com.darwinbox.reimbursement.services.ReimbFormService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReimbTypeTestBase {
    private static HashMap<String, ReimbTypeTestBase> obj;
    private String fileName;
    List<ReimbForm> reimbFormList = new ArrayList<>();
    List<ReimbLimitsBody> reimbLimitsBodyList = new ArrayList<>();
    ReimbForm reimbForm = new ReimbForm();
    ReimbFormService reimbFormService = new ReimbFormService();
}


/*
    private ReimbTypeTestBase(String fileName) {
        this.fileName = fileName;
    }

    public static ReimbTypeTestBase getObject(String reimbFile) {
        if (obj == null)
            obj = new HashMap<>();

        ReimbTypeTestBase rttb = obj.get(reimbFile);
        if (rttb == null) {
            rttb = new ReimbTypeTestBase(reimbFile);
            rttb.loadData();
            obj.put(reimbFile, rttb);
        }
        return rttb;
    }

    public static ReimbTypeTestBase getObject() {
        return getObject("TestData.xlsx");
    }

    private void loadData() {
        readReimbTypeData();
    }

    public void readReimbTypeData() {
        List<Map<String, String>> ReimbFormDataList = readDatafromSheet("ReimbForm");
        List<Map<String, String>> ReimbLimitsBodyDataList = readDatafromSheet("ReimbLimitsBody");

        //with all values in ReimbLimitsBody excel sheet create a java object
        for (Map<String, String> data : ReimbLimitsBodyDataList) {
            ReimbLimitsBody reimbLimitsBody = new ReimbLimitsBody();
            reimbLimitsBody.toObject(data);
            reimbLimitsBodyList.add(reimbLimitsBody);
        }

        //with all values in ReimbForm excel sheet create a java object
        for (Map<String, String> data : ReimbFormDataList) {
            ReimbForm reimbForm = new ReimbForm();
            //reimbForm.toObject(data);

            String id = (reimbFormService.getReimbFormIdByName(data.get("GroupCompany"), data.get("Name"))).getId();
            if (id == null) {
                String bodyObjects = data.get("ReimbursementLimitsBody");
                String objectTypes[] = bodyObjects.split(",");

                for (String value : objectTypes) {
                    int index = Integer.parseInt(value) - 1;
                    reimbForm.addReimbLimits(reimbLimitsBodyList.get(index));
                }
//            int rowcount = Integer.parseInt(data.get("ReimbursementLimitsBody"));
//            data.put("ReimbursementLimitsBody", reimbLimitsBodyList.get(rowcount).toString());
            }
            else {
                reimbForm.setId(id);
                reimbFormService.updateReimbForm(reimbForm);
            }
            reimbFormList.add(reimbForm);
        }
    }


    private List<Map<String, String>> readDatafromSheet(String sheetname) {

        Map<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "TestData.xlsx");
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }
    //String key = "TenantReimbursement[applicable][]";
        /*String groupCompanyMongoId = getGroupCompanyIds().get(TestBase.data.get("@@group"));
        String companyId = getIsParent().equalsIgnoreCase("yes") ? "" : groupCompanyMongoId;
        String id = "";
        List<String> valuesList;
        int randomIndex;

        //How to check for exception when parentCompId is Null??
        HashMap departments = getDepartments(groupCompanyMongoId);
        HashMap locations = getOfficeLocations(groupCompanyMongoId);
        HashMap empTypes = getEmployeeTypes();
        HashMap bands = getBands();
        HashMap grades = getGrades();
        JSONObject designations = getDesignations(groupCompanyMongoId);
//use centralized method in Custom Flow & use applicable to: another method to invoke
        for (String applicable : applicableToList) {
            applicableToList = getApplicableToList();
            switch (applicable) {
                case "ALL":

                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "ALL_0"));
                    break;
                case "DEPT":

                    valuesList = new ArrayList<String>(departments.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "DEPT_" + id));
                    break;
                case "LOC":

                    valuesList = new ArrayList<String>(locations.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "LOC_" + id));
                    break;
                case "TYP":

                    valuesList = new ArrayList<String>(empTypes.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "TYP_" + id));
                    break;
                case "BAND":

                    valuesList = new ArrayList<String>(bands.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "BAND_" + id));
                    break;
                case "GRAD":

                    valuesList = new ArrayList<String>(grades.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "GRADE_" + id));
                    break;

                case "DESG":
                    Object[] keys = designations.keySet().toArray();
                    JSONObject keyvalue = designations.getJSONObject((String) keys[new Random().nextInt(keys.length)]);
                    Object desig = keyvalue.keySet().toArray();
                    body.add(new BasicNameValuePair("TenantReimbursement[applicable][]", "DESG_" + id));
                    break;
            }
            //addReimbTypeData("ReimbursementLimitsBody");
        /*String temp = data.get("Applicable_To");
        if (temp.length() != 0) {
            for (String value : temp.split(",")) {
                applicableToList.add(value);
            }
            setApplicableToList(applicableToList);
        }*/

        /*//reimbForm = reimbFormService.getReimbFormIdByName(testdata.get("GroupCompany"), formname);
        //GrpCompany not to be taken from excel, modify later
        if (reimbForm.getId() != null) {
            formname = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
            reimbForm.setName(formname);
        }

         //addReimbTypeData("ReimbursementLimitsBody");
        /*String temp = data.get("Applicable_To");
        if (temp.length() != 0) {
            for (String value : temp.split(",")) {
                applicableToList.add(value);
            }
            setApplicableToList(applicableToList);
        }
    }

            */
