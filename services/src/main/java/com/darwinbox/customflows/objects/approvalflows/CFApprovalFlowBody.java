package com.darwinbox.customflows.objects.approvalflows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CFApprovalFlowBody {


    String action;
    String approvalContext;
    List<String>  visibilityRoles;

    String skipSettingName;
    String slaSettingName;

    List<String>  approverRoles;

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



    private List<CFApprovalFlowBody> cfApprovalFlowBodyList = new ArrayList<>();

    public void toObject(Map<String, String> data) {


        setApprovalContext(data.get("Approver Context"));
       /* setFieldType(CFFormBody.FieldType.valueOf(data.get("FieldType").toUpperCase().replace(" ", "_")));

        String values[] = data.getOrDefault("Values","").split(",");
        for ( String value : values)
            fieldValues.add(value);

        setMandatory(data.getOrDefault("Mandatory","no").equalsIgnoreCase("no"));
        setLineBreak(data.getOrDefault("LineBreak","no").equalsIgnoreCase("no"));
        */

    }


}
