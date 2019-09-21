package com.darwinbox.customflows.objects.customflow;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlowBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CustomFlow extends Services{

    private String name;
    private String description;
    private String company;
    private String id;

    private List<CustomFlowBody> formBody = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }



    enum CustomFlowFor{

        Employee,
        OfferLetter,
        RaiseRequisition
    }

    enum TriggerEvent{
        BusinesFlow,
        LocationChnage,
        ManagerChange,
        DesignationChange,
        EmployeeMovement,
        EmployeeTransfer
    }

    enum EventType{
        NoEvent,
        DOC,
        DOJ,
        WorkAnniversary,
        AbscondingTrigger
    }

    //can we make it boolean
    private String RestricCondition;

    List<String> Applicability;

    List<String> EmailTo;


    public List<NameValuePair> toMapObject(){

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id", getId()));
        formData.add(new BasicNameValuePair("CustomFlow[name]", getName()));

        int count = 0;
        for (CustomFlowBody formBody : formBody) {
            count ++;
            //formData.addAll(formData.size(), Services.mapToFormData(form.toMap(count)));
        }

        return formData;
    }




}
