package com.darwinbox.customflows.objects.approvalflows;

import com.darwinbox.customflows.objects.CFSkipSettings;
import com.darwinbox.customflows.services.CFFormService;
import com.darwinbox.customflows.services.CFSLASettingsService;
import com.darwinbox.customflows.services.CFSkipSettingsService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFApprovalFlowBody {


    String action;
    String actionValues;
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

            // get form id
            String formName = action.split(":")[0];
            String formID = fmService.getFormbyName(formName);

        } else if (action.length() == 0) {

            // no action
        } else {

            //action = fieldname
            // values = data.get("values")
            /* setFieldType(CFFormBody.FieldType.valueOf(data.get("FieldType").toUpperCase().replace(" ", "_")));

        String values[] = data.getOrDefault("Values","").split(",");
        for ( String value : values)
            fieldValues.add(value);

        setMandatory(data.getOrDefault("Mandatory","no").equalsIgnoreCase("no"));
        setLineBreak(data.getOrDefault("LineBreak","no").equalsIgnoreCase("no"));
        */

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
            slaID = cfSkipSrv.getSkipSettingByName(skipSettingName);
            setSkipSettingName(skipID);
        } else {
            setSkipSettingName("No Skip");
        }


    }


    public Map<String, String> toMap(int order) {

        Map<String, String> body = new HashMap<>();
        //List<NameValuePair> formData = new ArrayList<>();

        if (getApproverRoles().size() >= 1) {
            for (String aprrover : approverRoles)
                body.put("ApprovalFlowset[role][" + order + "][]", aprrover);
        }
        if (getVisibilityRoles().size() >= 1) {
            for (String visiblityUser : visibilityRoles)
                body.put("ApprovalFlowset[visibility][" + order + "][]", visiblityUser);
        }


        //write condition if action values are there then
        List<String> values = getFieldValues();
        if (values.size() >= 1) {
            String valueBody = "";

            for (String value : values) {
                valueBody = "," + value + valueBody;
            }

            if (valueBody.length() != 1)
                valueBody = valueBody.substring(1);

            body.put("ApprovalFlowset[options][" + order + "]", valueBody);
        }

        if (order == 0) {
            body.put("ApprovalFlowset[title][]", "");
            body.put("ApprovalFlowset[action][]", "");
            body.put("ApprovalFlowset[options][]", "");
            body.put("ApprovalFlowset[ini_sub_src_tar][]", "");
            body.put("ApprovalFlowset[skip_id][]", "");
            body.put("ApprovalFlowset[sla_id][]", "");
        } else {
            body.put("ApprovalFlowset[title][" + order + "]", "");
            body.put("ApprovalFlowset[action][" + order + "]", "");
            body.put("ApprovalFlowset[options][" + order + "]", "");
            body.put("ApprovalFlowset[ini_sub_src_tar][" + order + "]", "");
            body.put("ApprovalFlowset[skip_id][" + order + "]", "");
            body.put("ApprovalFlowset[sla_id][" + order + "]", "");

        }


        return body;
    }

}