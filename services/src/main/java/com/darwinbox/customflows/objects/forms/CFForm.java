package com.darwinbox.customflows.objects.forms;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class CFForm extends Services{

    private String  name;
    private String  description;
    private String  id = "";
    private List<CFFormBody> formBody = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


     public List<NameValuePair> toMapObject(){

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id", getId()));
        formData.add(new BasicNameValuePair("CutomWorkflowFormEvaluation[name]", getName()));
        formData.add(new BasicNameValuePair("CutomWorkflowFormEvaluation[description]", getDescription()));

        int count = 0;
        for (CFFormBody form : formBody) {
            count ++;
           formData.addAll(formData.size(), mapToFormData(form.toMap(count)));
        }

        return formData;
    }


}
