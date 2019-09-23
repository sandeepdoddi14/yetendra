package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.forms.CFForm;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CFFormService extends Services {



    // get url data
    public HashMap<String, String> getAllCFFoms(){

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
            cfCFFormsdata.put(cfFormName+"_"+cfFormVersion, cfFormID);

        }

        return cfCFFormsdata;
    }

    /**
     * method is used to create a Form can be used in Custom Flows
     * @param cfFrom
     */
    public void createCFForm(CFForm cfFrom){

        String url = getData("@@url") + "/settings/customworkflow/create";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, cfFrom.toMap());
        waitForUpdate(3);
        if (!response.contains("Form created successfully.")) {
            throw new RuntimeException(" Error in creating Form for Custom Workflow. ");
        }

    }

    /**
     * method is used to update a Form can be used in Custom Flows
     * @param cfFrom
     */
    public void updateCFForm(CFForm cfFrom){



    }

    /**
     * method is used to delete a Form in Custom Flows
     * @param cfFrom
     */
    public void deleteCFForm(CFForm cfFrom){


    }

    public String getFormbyName(String expFormName){

        HashMap<String, String> cfFormsDataMap = getAllCFFoms();
        String cfFormID = "";

        for (Map.Entry<String, String> entry1 : cfFormsDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("_")[0];
            if (expFormName.equalsIgnoreCase(actualKey)) {
                cfFormID = entry1.getValue();
                break;
            }
        }
        return cfFormID;
    }

}
