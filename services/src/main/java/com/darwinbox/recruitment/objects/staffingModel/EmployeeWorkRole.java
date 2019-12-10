package com.darwinbox.recruitment.objects.staffingModel;

import com.darwinbox.Services;
import com.darwinbox.framework.uiautomation.base.TestBase;

import java.util.HashMap;
import java.util.Map;

public class EmployeeWorkRole extends Services {

    /*Below method updates employee work role with a designation and position*/

    public void updateEmpWorkRole(String empMongoID, String designationID, String positionID) {

        String url = getData("@@url") + "/employee/editWorkDetails";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();

        body.put("UserMongo[id]",empMongoID);
        body.put("UserDesignationForm[parent_company_id]","");
        body.put("UserDesignationForm[designation_id]",designationID);
        body.put("UserDesignationForm[position_id]",positionID);
        body.put("UserDesignationForm[work_from_date]","");
        body.put("UserDesignationForm[isPromotion]","1");
        body.put("Events[event]","");

        doPost(url, null,mapToFormData(body));

    }

    }
