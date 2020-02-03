package com.darwinbox.recruitment.objects.staffingModel;

import com.darwinbox.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionSettings extends Services {


    private List<String> roles;

    public Map<String,String> updatePositionSettings(){

         Map<String,String> body= new HashMap<>();

         String temp =   "";   //"HRBP,Probation Period";//body.get("Roles");
         if (temp.length() != 0) {

             String VisibilityUsers[] = temp.split(",");

             roles = new ArrayList<>();
             for (String value : VisibilityUsers) {
                 roles.add(value);
             }

             if(roles.contains("HRBP"))
                 body.put("TenantPositionSetting[hrbp]","1");

             if(roles.contains("Job Level"))
                 body.put("TenantPositionSetting[job_level]","1");

             if(roles.contains("Probation Period"))
                 body.put("TenantPositionSetting[probation_period]","1");

             if(roles.contains("Dotted Line Manager"))
                 body.put("TenantPositionSetting[dotted_line_manager]","1");

             if(roles.contains("Functional Area"))
                 body.put("TenantPositionSetting[functional_area]","1");

             if(roles.contains("Over write allowed"))
                 body.put("TenantPositionSetting[over_writing_allowed]","1");

             if(roles.contains("Reverse sync allowed"))
                 body.put("TenantPositionSetting[reverse_sync_allowed]","1");

             if(roles.contains("Manager Over write allowed"))
                 body.put("TenantPositionSetting[manager_over_writing_allowed]","1");

             if(roles.contains("Manager Reverse sync allowed"))
                 body.put("TenantPositionSetting[manager_reverse_sync_allowed]","1");

             if(roles.contains("Functional Area Over write allowed"))
                 body.put("TenantPositionSetting[functional_area_over_writing_allowed]","1");

             if(roles.contains("Functional Area Reverse sync allowed"))
                 body.put("TenantPositionSetting[functional_area_reverse_sync_allowed]","1");

         }
         Reporter("Selected Position Settings are : "+roles,"INFO");
         return body;
     }


    public void UpdatePositions() {

        String url = getData("@@url") + "/settings/company/positionsetting";

        Map<String, String> body = new HashMap<>();

        body.put("yt0","SAVE");
        body.put("TenantPositionSetting[status]","0");
        body.put("TenantPositionSetting[status]","1");
        body.putAll(updatePositionSettings());

        doPost(url,null,mapToFormData(body));
    }
     }
