package com.darwinbox.core.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.core.company.objects.CostCentre;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CostCenterServices extends Services {

    public void createCostCenter (CostCentre cc ) {



    }
        /*
        get cost centers
         */

        public HashMap<String, String> getCostCenters() {
            String url = data.get("@@url") + "/settings/getCostCenters";

            HashMap<String, String> headers = new HashMap<>();
            headers.put("X-Requested-With", "XMLHttpRequest");

            JSONObject response = new JSONObject(doGet(url, headers));
            JSONArray arr = response.getJSONArray("aaData");
            int i = 0;
            HashMap<String, String> ids = new HashMap();
            while (i < arr.length()) {
                //Pattern p = Pattern.compile("id=\"\\w+\"");
                String name = arr.getJSONArray(i).getString(0);
                String value = arr.getJSONArray(i).getString(2).substring(7, 20);

                //  if (m.find()) {
                ids.put(name, value);
                //  } else {
                //    ids.put(arr.getJSONArray(i).getString(0), "");
                // }
                i++;
            }
            return ids;
        }

    }

