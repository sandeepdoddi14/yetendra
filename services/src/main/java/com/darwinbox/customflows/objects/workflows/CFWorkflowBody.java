package com.darwinbox.customflows.objects.workflows;

import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.Map;

public class CFWorkflowBody {

    private String stageName;
    private String action;
    private int assignee;
    private int noOfDays;

    enum SelectWhen{
        Before,
        After
    }

    enum SelectDay{
        EffectiveDate,
        ApprovalDate,
        TriggerDate
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getAssignee() {
        return assignee;
    }

    public void setAssignee(int assignee) {
        this.assignee = assignee;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

   public static  void main(String[] args){

        int input[] = {2,2,3,4,4,4,5,5,5};



   }


}
