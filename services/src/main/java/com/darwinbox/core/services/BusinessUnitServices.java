package com.darwinbox.core.services;
import com.darwinbox.core.Services;
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.employee.objects.BusinessUnit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class   BusinessUnitServices extends Services {

    public Map<String,String> getDefaultforBusinessUnit(){
        Map<String,String> body = new HashMap<>();

        body.put("TenantBusinessUnitForm[unit_name]","");
        body.put("TenantBusinessUnitForm[unit_address]","");
        body.put("TenantBusinessUnitForm[parent_company_id][]","");

        return  body;
    }


    public void createBusinessUnit(BusinessUnit businessUnit) {

        Map<String, String> body = getDefaultforBusinessUnit();
        body.putAll(businessUnit.toMap());

        String url = getData("@@url") + "/settings/company/businessunit";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateBusinessUnit(BusinessUnit businessUnit) {
        Map<String, String> body = getBusinessUnits();
        body.putAll(businessUnit.toMap());


        HashMap<String,String> businessUnits=getBusinessUnits();
        String  id=businessUnits.get(businessUnit.getBusinessUnitName());

        if(id!=null){
            body.put("TenantBusinessUnitForm[id]",id);
        }
        else
            throw new RuntimeException("There is no band to update Business Unit="+businessUnit.getBusinessUnitName());

        String url = getData("@@url") + "/settings/editBusinessunit";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }

    /*
   gets Business Units
    */
    public HashMap<String, String> getBusinessUnits() {
        String url = data.get("@@url") + "/settings/getBusinessUnits";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String value = arr.getJSONArray(i).getString(1).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }


    public void deleteBusinessUnit(BusinessUnit businessUnit){

        String businessUnitId=getBusinessUnits().get(businessUnit.getBusinessUnitName());

        String url = getData("@@url") + "/settings/editbusinessunit";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",businessUnitId);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));
    }

}


