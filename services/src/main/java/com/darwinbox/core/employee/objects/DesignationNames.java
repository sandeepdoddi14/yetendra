package com.darwinbox.core.employee.objects;

import java.util.HashMap;

public class DesignationNames {
    private String id;

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

    private String designationName;



    public HashMap<String,String>  toMap(){
        HashMap<String,String> body= new HashMap<>();


        body.put("UserDesignationNames[name]",getDesignationName());

        return body;
    }
}
