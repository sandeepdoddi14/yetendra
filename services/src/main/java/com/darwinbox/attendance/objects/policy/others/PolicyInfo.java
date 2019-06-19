package com.darwinbox.attendance.objects.policy.others;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PolicyInfo implements Serializable {

    public boolean compareTo(PolicyInfo policyInfo) {

        return  policyInfo.getMarkin_policy() == this.getMarkin_policy() &&
                policyInfo.getPolicyName().equals(this.getPolicyName())  &&
                policyInfo.getGraceTimeIn() == this.getGraceTimeIn() &&
                policyInfo.getGraceTimeOut() == this.getGraceTimeOut() &&
                policyInfo.getCompanyID().equals(this.getCompanyID());
    }

    enum MARKIN { DUMMY, IN, BOTH, NONE }

    private String policyName;
    private String policyID = "";
    private String policyDescription = "Created by Automation";
    private String companyID = "";

    private int markin_policy;
    private int graceTimeIn;
    private int graceTimeOut;

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getPolicyDescription() {
        return policyDescription;
    }

    public void setPolicyDescription(String policyDescription) {
        this.policyDescription = policyDescription;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public int getMarkin_policy() {
        return markin_policy;
    }

    public void setMarkin_policy(String markin_policy) {
        this.markin_policy = Integer.parseInt(markin_policy);
    }

    public int getGraceTimeIn() {
        return graceTimeIn;
    }

    public void setGraceTimeIn(String graceTimeIn) {
        this.graceTimeIn = Integer.parseInt(graceTimeIn);
    }

    public int getGraceTimeOut() {
        return graceTimeOut;
    }

    public void setGraceTimeOut(String graceTimeOut) {
        this.graceTimeOut = Integer.parseInt(graceTimeOut);
    }


    public static PolicyInfo jsonToObject(Map<String, Object> data) {

        PolicyInfo plInfo = new PolicyInfo();

        String id = data.get("id").toString();
        String pol_name = data.get("policy_name").toString();
        String pol_desc = data.get("policy_description").toString();
        String cmp_id = "";

        Object cmpId = data.get("parent_company_id");
        if ( cmpId != null )
            cmp_id = cmpId.toString();

        String gracetime_in = data.get("grace_time").toString();
        String gracetime_out = data.get("grace_time_early").toString();
        String markin_pol = data.get("markin_policy").toString();

        plInfo.setPolicyName(pol_name);
        plInfo.setPolicyID(id);
        plInfo.setCompanyID(cmp_id);
        plInfo.setPolicyDescription(pol_desc);
        plInfo.setGraceTimeIn(gracetime_in);
        plInfo.setGraceTimeOut(gracetime_out);
        plInfo.setMarkin_policy(markin_pol);

        return plInfo;

    }


    public Map<String,String> getMap() {

        Map<String,String> data = new HashMap<>();

        data.put("AttendancePolicyForm[id]",getPolicyID());
        data.put("AttendancePolicyForm[policy_name]",getPolicyName());
        data.put("AttendancePolicyForm[policy_description]",getPolicyDescription());
        data.put("AttendancePolicyForm[parent_company_id]",getCompanyID());
        data.put("AttendancePolicyForm[grace_time]",getGraceTimeIn()+"");
        data.put("AttendancePolicyForm[grace_time_early]",getGraceTimeOut()+"");
        data.put("AttendancePolicyForm[markin_policy]",getMarkin_policy()+"");

        return data;
    }

    public void toObject(Map<String, String> data) {

        setPolicyName(data.get("Policy Name"));
        setCompanyID(data.get("Group Company"));

        String graceTime[] = data.get("GraceTime").split(",");

        setGraceTimeIn(graceTime[0]);
        setGraceTimeOut(graceTime[1]);
        setMarkin_policy(data.get("MarkinPolicy"));

    }
}
