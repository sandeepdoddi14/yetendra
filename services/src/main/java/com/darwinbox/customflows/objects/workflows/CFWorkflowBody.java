package com.darwinbox.customflows.objects.workflows;

import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFWorkflowBody {

    private String stageName;
    private String action;
    private int assignee;
    private int noOfDays;

    public static void main(String[] args) {

        // int input[] = {2,2,3,4,4,4,5,5,5};


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


    enum SelectWhen {
        Before,
        After
    }


    enum SelectDay {
        EffectiveDate,
        ApprovalDate,
        TriggerDate
    }


    /**
     * Making Custom Workflow Bdy to add it to WF
     * @param order
     * @return
     */
    public Map<String, String> toMap(int order) {

        Map<String, String> body = new HashMap<>();

        body.put("", "");
        body.put("CustomWorkFlow_set[stage_name][]", "");
        body.put("CustomWorkFlow_set[title][]", "");
        body.put("CustomWorkFlow_set[action][]", "");
        body.put("CustomWorkFlow_set[options][]", "");
        body.put("CustomWorkFlow_set[role][]", "");
        body.put("CustomWorkFlow_set[	][]", "");
        body.put("CustomWorkFlow_set[trigger_point_before_after][]", "");
        body.put("CustomWorkFlow_set[trigger_point_particular][]", "");

        //change below logic as per requirement
        List<String> values = new ArrayList<>();
        String valueBody = "";

        for (String value : values) {
            valueBody = "," + valueBody + value;
        }

        if (valueBody.length() != 1)
            valueBody = valueBody.substring(1);

        body.put("CustomWorkFlow_set[options][]", valueBody);

        return body;
    }

}
