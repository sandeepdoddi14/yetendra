package com.darwinbox.customflows.objects.approvalflows;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CFApprovalFlow extends Services {


    private String name;
    private String id = "";
    private List<CFApprovalFlowBody> cfApprovalFlowBodyList = new ArrayList<>();


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




    public void add(CFApprovalFlowBody cfApprovalFlowBody){
        cfApprovalFlowBodyList.add(cfApprovalFlowBody);
    }

    public void toObject(Map<String, String> data) {

        setName(data.get("Name"));

    }

    /**
     * this method used to set (java object) values to web application
     *
     * @return
     */
    public List<NameValuePair> toMap() {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id", getId()));
        formData.add(new BasicNameValuePair("CustomApprovalFlow[name]", getName()));


        int count = 0;
        for (CFApprovalFlowBody fmbody : cfApprovalFlowBodyList) {
            formData.addAll(formData.size(), (fmbody.toMap(count)));
            count++;
        }

        return formData;
    }




}
