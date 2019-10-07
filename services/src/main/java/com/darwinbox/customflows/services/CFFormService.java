package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.forms.CFForm;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.SkipException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFFormService extends Services {


    // get url data
    public HashMap<String, String> getAllCFFoms() {

        HashMap<String, String> cfCFFormsdata = new HashMap<>();
        String url = getData("@@url") + "/settings/getcustomworkflowformevaluationdata";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);
        JSONArray jsonArray = objResponse.getJSONArray("aaData");

        String cfFormName = "";
        String cfFormVersion = "";
        String cfFormID = "";

        for (Object obj : jsonArray) {

            JSONArray data = (JSONArray) obj;
            cfFormName = data.getString(0);
            cfFormVersion = data.getString(1);
            cfFormID = data.getString(2).split("\" class")[0].substring(7);
            cfCFFormsdata.put(cfFormName + "#" + cfFormVersion, cfFormID);

        }

        return cfCFFormsdata;
    }

    /**
     * method is used to create a Form can be used in Custom Flows
     *
     * @param cfFrom
     */
    public void createCFForm(CFForm cfFrom) {

        String url = getData("@@url") + "/settings/customworkflow/create";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, cfFrom.toMap());

        waitForUpdate(3);
        if (!response.contains("Form created successfully.")) {
            throw new RuntimeException(" Error in creating Form for Custom Workflow. ");
        }

       /* boolean isFormExist = false;
        String formName = data.get("Name");
        HashMap<String, String> cfFormsDataMap = getAllCFFoms();

        for (Map.Entry<String, String> entry1 : cfFormsDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            if (formName.equalsIgnoreCase(actualKey)) {
                isFormExist = true;
                break;
            }
        }

        if (!isFormExist) {
            String response = doPost(url, headers, cfFrom.toMap());

            waitForUpdate(3);
            if (!response.contains("Form created successfully.")) {
                throw new RuntimeException(" Error in creating Form for Custom Workflow. ");
            }
        } else {

            Reporter("Form Already exist", "Fail" );
            //throw SkipException("Form Already exist with Same Name.");
        }*/


    }

    /**
     * method is used to update a Form can be used in Custom Flows
     *
     * @param cfFrom
     */
    public void updateCFForm(CFForm cfFrom) {

        String url = getData("@@url") + "/settings/EditCutomWorkflowFormEvaluation";
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = cfFrom.toMap();
        obj.add(new BasicNameValuePair("reimb_id",cfFrom.getId()));
        obj.add(new BasicNameValuePair("mode","edit"));
        String response = doPost(url, headers, obj);

    }

    /**
     * method is used to delete a Form in Custom Flows
     *
     * @param cfFrom
     */
    public void deleteCFForm(CFForm cfFrom) {
        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/editcutomworkflowformevaluation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        body.put("resource",cfFrom.getId());
        body.put("mode","delete");
        doPost(url, headers, mapToFormData(body));


    }

    public String getFormbyName(String expFormName) {

        HashMap<String, String> cfFormsDataMap = getAllCFFoms();
        String cfFormID = null;

        for (Map.Entry<String, String> entry1 : cfFormsDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            if (expFormName.equalsIgnoreCase(actualKey)) {
                cfFormID = entry1.getValue();
                break;
            }
        }
        return cfFormID;
    }

    public String getcfFormName(String expcfFormName, String version) {

        HashMap<String, String> cfFormDataMap = getAllCFFoms();
        String cfFormID = null;

        for (Map.Entry<String, String> entry1 : cfFormDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            String actVerstion = key.split("#")[1];
            if (expcfFormName.equalsIgnoreCase(actualKey) && version.equalsIgnoreCase(actVerstion)) {
                cfFormID = entry1.getValue();
                break;
            }
        }
        return cfFormID;
    }




}
