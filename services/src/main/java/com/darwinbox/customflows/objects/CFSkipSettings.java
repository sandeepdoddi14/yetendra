package com.darwinbox.customflows.objects;

import com.darwinbox.customflows.objects.forms.CFFormBody;

import java.util.List;

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







}
