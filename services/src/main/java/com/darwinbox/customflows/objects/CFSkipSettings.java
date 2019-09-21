package com.darwinbox.customflows.objects;

import com.darwinbox.customflows.objects.forms.CFFormBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CFSkipSettings {


    private String name;
    private String description;
    private boolean isAssgineeInitiator;
    private boolean isAssgineeSubject;
    private boolean isAssgineeEarlierRespondent;
    private boolean isAssgineeSelectedRole;
    private boolean isInitiatorSelectedRole;
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

    public boolean isAssgineeSelectedRole() {
        return isAssgineeSelectedRole;
    }

    public void setAssgineeSelectedRole(boolean assgineeSelectedRole) {
        isAssgineeSelectedRole = assgineeSelectedRole;
    }

    public boolean isInitiatorSelectedRole() {
        return isInitiatorSelectedRole;
    }

    public void setInitiatorSelectedRole(boolean initiatorSelectedRole) {
        isInitiatorSelectedRole = initiatorSelectedRole;
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
        boolean isAssigeeSelectedRole = Boolean.valueOf(data.get("Assignee is seleted role"));
        boolean isInitiatorSelectedRole = Boolean.valueOf(data.get("Initiator is Selected role"));
        boolean isNoAssigee = Boolean.valueOf(data.get("No Assignee"));

        setName(data.get("Name"));
        setDescription(data.get("Description"));

        setAssgineeInitiator(isAssigeeInit);
        setAssgineeEarlierRespondent(isEarlierRespondent);
        setAssgineeSelectedRole(isAssigeeSelectedRole);
        setNoAssignee(isNoAssigee);
        setAssgineeSubject(isAssigeeSubject);
        setInitiatorSelectedRole(isInitiatorSelectedRole);

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

//        formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", getDescription()));
//        formData.add(new BasicNameValuePair("SkipSetting[skip_conditions][]", getDescription()));
//        formData.add(new BasicNameValuePair(" SkipSetting[skip_roles][]", getDescription()));
//        formData.add(new BasicNameValuePair(" SkipSetting[skip_roles][]", getDescription()));
//        formData.add(new BasicNameValuePair("SkipSetting[initiator_skip_roles][]", getDescription()));
        formData.add(new BasicNameValuePair("SkipSetting[skip_output][]", getSkipOutput()));


        return formData;
    }
}

