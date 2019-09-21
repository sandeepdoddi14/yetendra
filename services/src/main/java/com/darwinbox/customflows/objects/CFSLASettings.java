package com.darwinbox.customflows.objects;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CFSLASettings {

    private String id;
    private String name;
    private String description;

    private String slaDuration;
    private String slaBreachOutput;


    public void setSlaDuration(String slaDuration) {
        this.slaDuration = slaDuration;
    }
    public String getSlaDuration() {
        return slaDuration;
    }

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

    public void setDescription(String desription) {
        this.description = desription;
    }

    public String getSlaBreachOutput() {
        return slaBreachOutput;
    }

    public void setSlaBreachOutput(String slaBreachOutput) {
        this.slaBreachOutput = slaBreachOutput;
    }


    /**
     * to make excel values to CFSLASettigns java object
     */

    public void toObject(Map<String, String> data) {

        setName(data.get("Name"));
        setDescription(data.get("Description"));
        setSlaBreachOutput(data.get("BreachOutput"));
        setSlaDuration(data.get("Duration"));
    }

    /**
     * this method used to set (java object) values to web application
     *
     * @return
     */
    public List<NameValuePair> toMap() {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id", getId()));

        formData.add(new BasicNameValuePair("SlaSetting[name]", getName()));
        formData.add(new BasicNameValuePair("SlaSetting[descriptions]", getDescription()));
        formData.add(new BasicNameValuePair("SlaSetting[sla_duration]", getSlaDuration()));
        formData.add(new BasicNameValuePair("SlaSetting[sla_breach_output]", getSlaBreachOutput()));

        return formData;
    }

}
