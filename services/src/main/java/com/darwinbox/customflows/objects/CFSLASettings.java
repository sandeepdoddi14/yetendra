package com.darwinbox.customflows.objects;

public class CFSLASettings {


    private String name;
    private String desription;

    public String getSlaDuration() {
        return slaDuration;
    }

    private String slaDuration;
    private String slaBreachOutput;


    public void setSlaDuration(String slaDuration) {
        this.slaDuration = slaDuration;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public String getSlaBreachOutput() {
        return slaBreachOutput;
    }

    public void setSlaBreachOutput(String slaBreachOutput) {
        this.slaBreachOutput = slaBreachOutput;
    }





}
