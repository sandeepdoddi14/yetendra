package com.darwinbox.customflows.objects.approvalflows;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.forms.CFFormBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CFApprovalFlow {


    private String name;
    private String id = "";
    private List<CFApprovalFlowBody> formBody = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        formData.add(new BasicNameValuePair("CustomApprovalFlow[name]", getName()));

        int count = 0;
        for (CFApprovalFlowBody formBody : formBody) {
            count ++;
            //formData.addAll(formData.size(), Services.mapToFormData(form.toMap(count)));
        }

        return formData;
    }




}
