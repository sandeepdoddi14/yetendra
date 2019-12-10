package com.darwinbox.core.employee.objects;

import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.util.Date;
import java.util.HashMap;

public class DesignationNames {
    private String id;
    private String designationName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }




    public HashMap<String,String>  toMap(){
        HashMap<String,String> body= new HashMap<>();

        body.put("yt0","SAVE");
        body.put("UserDesignationNames[name]",getDesignationName());

        return body;
    }

    public String toObject(){

        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        Date date = new Date();
        String designationName = "Designation_"+dateTimeHelper.formatDateTo(date,"yyyy-MMM-dd::hh:mm:ss");

        setDesignationName(designationName);

        return designationName;
    }
}
