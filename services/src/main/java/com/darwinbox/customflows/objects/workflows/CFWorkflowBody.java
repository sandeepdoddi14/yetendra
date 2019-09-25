package com.darwinbox.customflows.objects.workflows;
import  com.darwinbox.customflows.objects.forms.CFFormBody.FieldType;
import com.darwinbox.customflows.objects.forms.CFFormBody;
import com.darwinbox.customflows.services.CFFormService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFWorkflowBody {

    private String stageName;
    private String action="0";
    private String assignee;
    private String noOfDays;
    private String SelectWhen;

    private SelectDay selectDay;



    FieldType fieldType;
    private List<String> fieldValues = new ArrayList<>();
    private List<CFWorkflowBody> cfWorkflowBodyList = new ArrayList<>();

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }
    public String getSelectWhen() {
        return SelectWhen;
    }

    public void setSelectWhen(String selectWhen) {
        SelectWhen = selectWhen;
    }


    enum SelectDay {
        DUMMY,
        EFFECTIVE_DATE ,
        APPROVAL_DATE,
        TRIGGER_DATE
    }

    public SelectDay getSelectDay() {
        return selectDay;
    }

    public void setSelectDay(SelectDay selectDay) {
        this.selectDay = selectDay;
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

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }


    public void toObject(Map<String, String> data){

        setStageName(data.get("Stage Name"));

        CFFormService fmService = new CFFormService();

        String action = data.get("Action");

        if (action.startsWith("form:")) {
            String formName = action.split(":")[1];
            String formID = fmService.getFormbyName(formName);
            setAction("form_"+formID);
        } else if (action.length() == 0) {
            setAction("0");
        } else {
            setAction(data.get("Title"));
            fieldType = (CFFormBody.FieldType.valueOf(data.get("Action").toUpperCase().replace(" ", "_")));
            String values[] = data.getOrDefault("Values", "").split(",");
            for (String value : values)
                fieldValues.add(value);

            setFieldValues(fieldValues);

        }

        setAssignee(data.get("Assignee"));
        setNoOfDays(data.getOrDefault("No Of Days",""));
        setSelectWhen(data.get("Select When"));
        setSelectDay(SelectDay.valueOf(data.get("Select Day").replace(" ","_").toUpperCase()));



    }


     /**
     * Making Custom Workflow Bdy to add it to WF
     * @param stageOrder
     * @return
     */
    public List<NameValuePair> toMap(int stageOrder) {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("CustomWorkFlow_set[stage_name][]", getStageName()));
        if (action.startsWith("form_") ) {
            formData.add(new BasicNameValuePair("CustomWorkFlow_set[action][]", getAction()));
            formData.add(new BasicNameValuePair("CustomWorkFlow_set[options][]", ""));
            formData.add(new BasicNameValuePair("CustomWorkFlow_set[title][]", ""));
        } else if (action.equalsIgnoreCase("0") ) {
            formData.add(new BasicNameValuePair("CustomWorkFlow_set[action][]", "0"));
            formData.add(new BasicNameValuePair("CustomWorkFlow_set[options][]", ""));
            formData.add(new BasicNameValuePair("CustomWorkFlow_set[title][]", ""));
        } else {
            List<String> values = getFieldValues();
            if (values.size() >= 1) {
                String valueBody = "";

                for (String value : values) {
                    valueBody = "," + value + valueBody;
                }

                if (valueBody.length() != 1)
                    valueBody = valueBody.substring(1);
                formData.add(new BasicNameValuePair("CustomWorkFlow_set[title][]", getAction()));
                formData.add(new BasicNameValuePair("CustomWorkFlow_set[action][]", fieldType.getType()));
                formData.add(new BasicNameValuePair("CustomWorkFlow_set[options][]", valueBody));
            }
        }
        formData.add(new BasicNameValuePair("CustomWorkFlow_set[role][]", getAssignee()));
        formData.add(new BasicNameValuePair("CustomWorkFlow_set[trigger_point_before_after][]", getSelectWhen().equalsIgnoreCase("Before") ? "1": "2"));
        formData.add(new BasicNameValuePair("CustomWorkFlow_set[trigger_point_days][]", getNoOfDays()));
        formData.add(new BasicNameValuePair("CustomWorkFlow_set[trigger_point_particular][]", selectDay.ordinal()+""));

        return formData;
    }

}
