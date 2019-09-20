package com.darwinbox.recruitment.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.recruitment.objects.ArchivePosition;
import com.darwinbox.recruitment.objects.EvaluationForms;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvaluationFormsService extends Services {


    public void createEvaluationForm(EvaluationForms evaluationForms){
        String url = getData("@@url") + "/settings/recruitment/evaluation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> list = new ArrayList<>();
        list.addAll(evaluationForms.toMap());

        doPost(url, headers,list);

    }

    public EvaluationForms getEvaluationFormByName(String name, String version) {

        EvaluationForms evaluationForms = new EvaluationForms();
        String url = getData("@@url") + "/settings/getRecevaluationData";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray arr = objResponse.getJSONArray("aaData");

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;
            evaluationForms.setEvaluationName(objarr.getString(0));
            evaluationForms.setVersion(objarr.getString(1));
            evaluationForms.setId(objarr.getString(2));

            if(name.equalsIgnoreCase(evaluationForms.getEvaluationName()) && version.equalsIgnoreCase(evaluationForms.getVersion())){

                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(evaluationForms.getId());

                if (m.find()){
                    evaluationForms.setId(StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
                }
                break;
            }
        }
     return evaluationForms;
    }

    public void editEvaluationForm(EvaluationForms evaluationForms) {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> list = new ArrayList<>();

        String url = getData("@@url") + "/settings/editrecevaluation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("mode","edit");
        body.put("reimb_id",evaluationForms.getId());
        list.addAll(evaluationForms.toEdit("0"));

        list.addAll(mapToFormData(body));
        doPost(url, headers,list);
    }

    public void deleteEvaluationForm(EvaluationForms evaluationForms){

        Map<String, String> body = new HashMap<>();

        String url = getData("@@url") + "/settings/EditRecevaluation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",evaluationForms.getId());
        body.put("mode","delete");
        doPost(url, headers, mapToFormData(body));

    }
}
