package com.darwinbox.customflows.objects;

import com.darwinbox.customflows.objects.forms.CFFormBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFSkipSettings {


    private String name;
    private String description;
    private boolean isAssgineeInitiator;
    private boolean isAssgineeSubject;
    private boolean isAssgineeEarlierRespondent;
    private List<String> assigneeList = new ArrayList<>();
    private List<String> initiatorList = new ArrayList<>();

    public List<String> getAssigneeList() {
        return assigneeList;
    }

    public void setAssigneeList(List<String> assigneeList) {
        this.assigneeList = assigneeList;
    }

    public List<String> getInitiatorList() {
        return initiatorList;
    }

    public void setInitiatorList(List<String> initiatorList) {
        this.initiatorList = initiatorList;
    }

    private boolean isNoAssignee;
    private String skipOutput;
    private String id;

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

    public boolean isAssgineeInitiator() {
        return isAssgineeInitiator;
    }

    public void setAssgineeInitiator(boolean assgineeInitiator) {
        isAssgineeInitiator = assgineeInitiator;
    }

    public boolean isAssgineeSubject() {
        return isAssgineeSubject;
    }

    public void setAssgineeSubject(boolean assgineeSubject) {
        isAssgineeSubject = assgineeSubject;
    }

    public boolean isAssgineeEarlierRespondent() {
        return isAssgineeEarlierRespondent;
    }

    public void setAssgineeEarlierRespondent(boolean assgineeEarlierRespondent) {
        isAssgineeEarlierRespondent = assgineeEarlierRespondent;
    }

    public boolean isNoAssignee() {
        return isNoAssignee;
    }

    public void setNoAssignee(boolean noAssignee) {
        isNoAssignee = noAssignee;
    }

    public String getSkipOutput() {
        return skipOutput;
    }

    public void setSkipOutput(String skipOutput) {
        this.skipOutput = skipOutput;
    }

    /**
     * from excel data values to CFSkipSettings java object
     */
    public void toObject(Map<String, String> data) {

        boolean isAssigeeInit = Boolean.valueOf(data.get("Assignee is initiator"));
        boolean isAssigeeSubject = Boolean.valueOf(data.get("Assignee is subject"));
        boolean isEarlierRespondent = Boolean.valueOf(data.get("Assignee is any earlier respondent"));
        boolean isNoAssigee = Boolean.valueOf(data.get("No Assignee"));

        setName(data.get("Name"));
        setDescription(data.get("Description"));

        setAssgineeInitiator(isAssigeeInit);
        setAssgineeEarlierRespondent(isEarlierRespondent);

        setNoAssignee(isNoAssigee);
        setAssgineeSubject(isAssigeeSubject);

        String temp = data.get("Assignee is seleted role");
        if (temp.length() != 0) {

            String assigneeSelectedRole[] = temp.split(",");

            List<String> assigneeList = new ArrayList<>();
            for (String value : assigneeSelectedRole) {
                assigneeList.add(value);
            }

            setAssigneeList(assigneeList);
        }


        temp = data.get("Initiator is Selected role");

        if (temp.length() != 0) {
            String initiatorsasRole[] = data.get("Initiator is Selected role").split(",");

            List<String> initiatorList = new ArrayList<>();
            for (String value : initiatorsasRole) {
                initiatorList.add(value);
            }

            setInitiatorList(initiatorList);
        }
        setSkipOutput(data.get("Skip Output"));

    }

    /**
     * this method used to set (java object) values to web application
     *
     * @return
     */
    public List<NameValuePair> toMap() {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id", getId()));

        formData.add(new BasicNameValuePair("SkipSetting[name]", getName()));
        formData.add(new BasicNameValuePair("SkipSetting[descriptions]", getDescription()));

        if ( isAssgineeInitiator) formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", "1"));
        if (isAssgineeSubject)   formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", "2"));
        if (isAssgineeEarlierRespondent)    formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", "3"));
        if (getAssigneeList().size() >= 1) {
            formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", "4"));
            for (String assignee : assigneeList)
                formData.add(new BasicNameValuePair("SkipSetting[skip_roles][]", assignee));
        }
        if (getInitiatorList().size() >= 1) {
            formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", "5"));
            for (String assignee : initiatorList)
                formData.add(new BasicNameValuePair("SkipSetting[initiator_skip_roles][]", assignee));
        }

        formData.add(new BasicNameValuePair("SkipSetting[skip_output][]",( getSkipOutput().equalsIgnoreCase("Approve") ? "1": "2")));
        return formData;
    }



}

