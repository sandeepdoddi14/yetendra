package com.darwinbox.customflows.objects.approvalflows;

import com.darwinbox.customflows.services.CFFormService;
import com.darwinbox.customflows.services.CFSLASettingsService;
import com.darwinbox.customflows.services.CFSkipSettingsService;
import  com.darwinbox.customflows.objects.forms.CFFormBody.FieldType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFApprovalFlowBody {


    String action;

    FieldType fieldType;
    String approvalContext;
    List<String> visibilityRoles;
    String skipSettingName;
    String slaSettingName;
    List<String> approverRoles;
    private List<String> fieldValues = new ArrayList<>();
    private List<CFApprovalFlowBody> cfApprovalFlowBodyList = new ArrayList<>();

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public List<String> getApproverRoles() {
        return approverRoles;
    }

    public void setApproverRoles(List<String> approverRoles) {
        this.approverRoles = approverRoles;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getApprovalContext() {
        return approvalContext;
    }

    public void setApprovalContext(String approvalContext) {
        this.approvalContext = approvalContext;
    }

    public List<String> getVisibilityRoles() {
        return visibilityRoles;
    }

    public void setVisibilityRoles(List<String> visibilityRoles) {
        this.visibilityRoles = visibilityRoles;
    }

    public String getSkipSettingName() {
        return skipSettingName;
    }

    public void setSkipSettingName(String skipSettingName) {
        this.skipSettingName = skipSettingName;
    }

    public String getSlaSettingName() {
        return slaSettingName;
    }

    public void setSlaSettingName(String slaSettingName) {
        this.slaSettingName = slaSettingName;
    }

    public void toObject(Map<String, String> data) {

        CFFormService fmService = new CFFormService();

        String action = data.get("Action");

        if (action.startsWith("form:")) {
            String formName = action.split(":")[1];
            String formID = fmService.getFormbyName(formName);
            setAction("form_"+formID);
        } else if (action.length() == 0) {
            setAction("");
        } else {
            setAction(data.get("Title"));
            fieldType = (FieldType.valueOf(data.get("Action").toUpperCase().replace(" ", "_")));
            String values[] = data.getOrDefault("Values", "").split(",");
            for (String value : values)
                fieldValues.add(value);

            setFieldValues(fieldValues);


        }


        setApprovalContext(data.get("Approver Context"));

        String temp = data.get("Approvers");
        if (temp.length() != 0) {

            String Approvers[] = temp.split(",");

            approverRoles = new ArrayList<>();
            for (String value : Approvers) {
                approverRoles.add(value);
            }

            setApproverRoles(approverRoles);
        }


        temp = data.get("VisibilityRoles");
        if (temp.length() != 0) {

            String VisibilityUsers[] = temp.split(",");

            visibilityRoles = new ArrayList<>();
            for (String value : VisibilityUsers) {
                visibilityRoles.add(value);
            }

            setVisibilityRoles(visibilityRoles);
        }

        String slaSettingName = data.getOrDefault("SLA Settings", "");
        String slaID = "";
        if (slaSettingName != "") {
            CFSLASettingsService cfSlasrv = new CFSLASettingsService();
            slaID = cfSlasrv.getSlaSettingByName(slaSettingName);
            setSlaSettingName(slaID);
        } else {
            setSlaSettingName("No Sla");
        }

        String skipSettingName = data.getOrDefault("Skip Settings", "");
        String skipID = "";
        if (skipSettingName != "") {
            CFSkipSettingsService cfSkipSrv = new CFSkipSettingsService();
            skipID = cfSkipSrv.getSkipSettingByName(skipSettingName);
            setSkipSettingName(skipID);
        } else {
            setSkipSettingName("No Skip");
        }


    }


    public List<NameValuePair> toMap(int lineItem) {

        List<NameValuePair> formData = new ArrayList<>();

        String order = "";
        if (lineItem < 1 ){
            order = "";
        }else{
            order = order+lineItem;
        }

        if (getApproverRoles().size() >= 1) {
            for (String aprrover : approverRoles)
                formData.add(new BasicNameValuePair("ApprovalFlowset[role][" + lineItem + "][]", aprrover));
        }
        if (getVisibilityRoles().size() >= 1) {
            for (String visiblityUser : visibilityRoles)
                formData.add(new BasicNameValuePair("ApprovalFlowset[visibility][" + lineItem+ "][]", visiblityUser));
        }


        if (action.startsWith("form_") || action.length() == 0) {
            formData.add(new BasicNameValuePair("ApprovalFlowset[action][" + order + "]", action));
            formData.add(new BasicNameValuePair("ApprovalFlowset[options][" + order + "]", ""));
            formData.add(new BasicNameValuePair("ApprovalFlowset[title][" + order + "]", ""));
        } else {
            List<String> values = getFieldValues();
            if (values.size() >= 1) {
                String valueBody = "";

                for (String value : values) {
                    valueBody = "," + value + valueBody;
                }

                if (valueBody.length() != 1)
                    valueBody = valueBody.substring(1);
                formData.add(new BasicNameValuePair("ApprovalFlowset[title][" + order + "]", getAction()));
                formData.add(new BasicNameValuePair("ApprovalFlowset[action][" + order + "]", fieldType.getType()));
                formData.add(new BasicNameValuePair("ApprovalFlowset[options][" + order + "]", valueBody));
            }
        }
        formData.add(new BasicNameValuePair("ApprovalFlowset[ini_sub_src_tar][" + order + "]", getApprovalContext().equalsIgnoreCase("Subject") ? "1": "3"));
        formData.add(new BasicNameValuePair("ApprovalFlowset[skip_id][" + order + "]", getSkipSettingName()));
        formData.add(new BasicNameValuePair("ApprovalFlowset[sla_id][" + order + "]", getSlaSettingName()));

        return formData;
    }

}