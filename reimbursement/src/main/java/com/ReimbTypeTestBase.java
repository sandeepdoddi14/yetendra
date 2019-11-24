
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
  /*for (String locn : location) {
                if (locn.equals("all"))
                    body.add(new BasicNameValuePair("Reimb_set[location][" + count + "][]", "ALL_0"));
                else
                    body.add(new BasicNameValuePair("Reimb_set[location][" + count + "][]", location));
            }
            for (String bgd : band_grade_desig) {
                if (bgd.equals("all"))
                    body.add(new BasicNameValuePair("Reimb_set[designation][" + count + "][]", "ALL_0"));

                else
                    body.add(new BasicNameValuePair("Reimb_set[designation][" + count + "][]", bgd));
            }
        }*/





