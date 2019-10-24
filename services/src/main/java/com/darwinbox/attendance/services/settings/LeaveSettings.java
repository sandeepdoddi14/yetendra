package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LeaveSettings extends Services {



    protected Map<String,String> defaultBodyForLeaveSettings(){
        Map<String,String> defaultBody= new HashMap();

        defaultBody.put("yt0","SAVE");
        defaultBody.put("TenantLeavesSettings[unpaid_refresh]","1");//calender leave
        defaultBody.put("TenantLeavesSettings[leaves_leave]","1");//approval
        defaultBody.put("TenantLeavesSettings[cc_users]","");

        defaultBody.put("employee_search","");

        defaultBody.put("TenantLeavesSettings[no_of_cascading_leaves]","3");
        defaultBody.put("TenantLeavesSettings[dont_allow_past_date_leave_revoke]","1");
        defaultBody.put("TenantLeavesSettings[allow_employee_revoke_option]","1");
        defaultBody.put("TenantLeavesSettings[allow_employee_past_date_leave_revoke]","1");

        defaultBody.put("TenantLeavesSettings[hide_policy]","1");
        defaultBody.put("TenantLeavesSettings[allow_previous_year_application]","1");
        defaultBody.put("TenantLeavesSettings[type]", "0");
        defaultBody.put("TenantLeavesSettings[from_date]","");
        defaultBody.put("TenantLeavesSettings[to_date]","");
        defaultBody.put("TenantLeavesSettings[no_of_days]","");
        //pass on the leave ids for the leave types which are not to be counted as present days
        // defaultBody.put("TenantLeavesSettings[cnt_present_leaves][]", "5af9cbf4bb7da");
        //defaultBody.put("TenantLeavesSettings[cnt_present_leaves][]", "5af9cc3f02e53");
        return defaultBody;
    }



    public String saveLeaveSettings(Map<String,String> request)
    {
        Map<String,String> defaultBody=defaultBodyForLeaveSettings();
        defaultBody.putAll(request);

        String url= data.get("@@url")+ UtilityHelper.getProperty("ServiceUrls","updateSettings");

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");


        return doPost(url,headers,mapToFormData(defaultBody));
    }
    public JSONObject getAllLeaveIDs(String companyId) {

        String url = getData("@@url") + "/DependentDrop/LeavesAttendance?id="+companyId;
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        if (objResponse != null && objResponse.getString("status").equals("success")) {
            return objResponse.getJSONObject("update");
        } else {
            log.error("Status in Response: " + objResponse);
            throw new RuntimeException("ERROR: Unable to get shifts");
        }
    }

    public String getLeaveID(String leavename, String companyId) {

        JSONObject arr = getAllLeaveIDs(companyId);
        Iterator it = arr.keys();
        String leaveId = null;
        while(it.hasNext()) {
            String key = it.next().toString();
            if (arr.get(key).equals(leavename)) {
                leaveId = key;
                break;
            }
        }
        return leaveId;
    }

    public String createLeave(String leavename,String company) {

        Map<String,String> body = new HashMap<>();
        body.put("Leaves[name]",leavename);
        body.put("Leaves[parent_company_id]",(company.equalsIgnoreCase("main") ? "" : company));
        body.put("yt0", "Save" );

        String leaveId = getLeaveID(leavename,company );

        if ( leaveId == null ) {
            createLeave(body);
            leaveId = getLeaveID(leavename,company );
            log.info(leavename + " Leave is created");
        }
        else {
            body.put("Leaves[id]",leaveId );
            updateLeave(body);
            log.info(leavename + " Leave is updated");
        }

        return leaveId;

    }

    public void updateLeave(Map<String, String> leaveBody) {

        Map<String,String> body = getDefaultforLeaveDeduction();
        body.putAll(leaveBody);
        String url = getData("@@url") + "/settings/editLeave";
        Map headers = new HashMap();
        headers.put("x-requested-with","XMLHttpRequest" );
        doPost(url, headers, mapToFormData(body));

    }

    public void createLeave(Map<String, String> leaveBody) {

        Map<String,String> body = getDefaultforLeaveDeduction();
        body.putAll(leaveBody);

        String url = getData("@@url") + "/settings/leaves/create";
        Map headers = new HashMap();
        headers.put("x-requested-with","XMLHttpRequest" );

        doPost(url, headers, mapToFormData(body));

    }

    public Map<String,String> getDefaultforLeaveDeduction(){

        Map<String,String> body = new HashMap<>();

        body.put("Leaves[assignment_type]","0");
        body.put("Leaves[description]","Created By Automation");
        body.put("Leaves[yearly_endowment]","120");
        body.put("Leaves[or_and]","0");
        body.put("Leaves[p3_max_consecutive_days_limit]","0");
        body.put("Leaves[p4_carry_over_time]","1");
        body.put("Leaves[auto_replenish_times]","");
        body.put("Leaves[push_leaves_to_admin]","1");
        body.put("Leaves[auto_approve_days]","0");
        body.put("Leaves[max_leave_application_month]","");
        body.put("Leaves[max_leave_application_year]","");
        body.put("Leaves[max_leave_application_tenure]","");
        body.put("Leaves[restrictGender]","0");
        body.put("Leaves[pre_approved_no_of_days]","0");
        body.put("Leaves[p1_waiting_after_doj_status]","1");
        body.put("Leaves[p1_waiting_after_doj]","0");
        body.put("Leaves[p2_max_per_month]","0");
        body.put("Leaves[max_leaves_notice_period]","0");
        body.put("Leaves[max_number_of_leaves_accrued]","0");
        body.put("Leaves[min_consecutive_days_limit]","0");
        body.put("Leaves[max_consecutive_days_limit]","0");
        body.put("Leaves[form]","");
        body.put("Leaves[approval_flow]","");
        body.put("Leaves[days_to_trigger_exceptional_flow]","0");
        body.put("Leaves[exceptional_flow]","");
        body.put("Leaves[multiple_allotment_restriction][0][maximumLeaves_radio]","0");
        body.put("Leaves[multiple_allotment_restriction][0][maximumLeaves]","");
        body.put("Leaves[sub_category][0][sub_category]","");
        body.put("Leaves[sub_category][0][min]","");
        body.put("Leaves[sub_category][0][max]","");
        body.put("Leaves[sub_category][0][form]","");
        body.put("LeavePolicy_Prorated[status]","0");
        body.put("LeavePolicy_Prorated[probation_status]","0");
        body.put("LeavePolicy_Accural[status]","0");
        body.put("LeavePolicy_Accural[is_monthly_quaterly]","0");
        body.put("LeavePolicy_Accural[number_of_months]","1");
        body.put("LeavePolicy_Accural[starting_from]","0");
        body.put("LeavePolicy_Accural[starting_from_monthly]","0");
        body.put("LeavePolicyTenure[status]","0");
        body.put("LeavePolicyTenure[leaves_per_year][0][from_year]","1");
        body.put("LeavePolicyTenure[leaves_per_year][0][to_year]","1");
        body.put("LeavePolicyTenure[leaves_per_year][0][leaves]","");
        body.put("LeavePolicy_HalfDays[status]","1");
        body.put("LeavePolicy_UnusedCarryover[status]","0");
        body.put("LeavePolicy_UnusedCarryover[carry_forward]","0");
        body.put("LeavePolicy_UnusedCarryover[carry_forward_amount_type]","0");
        body.put("LeavePolicy_UnusedCarryover[carry_forward_amount]","0");
        body.put("LeavePolicy_UnusedCarryover[remaining]","0");
        body.put("LeavePolicy_UnusedFandFEncash[status]","0");
        body.put("LeavePolicy_UnusedFandFEncash[fandf_encash]","0");
        body.put("LeavePolicy_UnusedFandFEncash[fandf_encash_amount_type]","0");
        body.put("LeavePolicy_UnusedFandFEncash[fandf_encash_amount]","0");
        body.put("LeavePolicy_InterveningHolidays[status]","1");
        body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]","1");
        body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]","1");
        body.put("LeavePolicyPrefixSuffix[status]","0");
        body.put("LeavePolicyPrefixSuffix[prefix_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[suffix_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[prefix_holiday]","0");
        body.put("LeavePolicyPrefixSuffix[suffix_holiday]","0");
        body.put("LeavePolicy_PreviousDates[status]","1");
        body.put("LeavePolicy_PreviousDates[no_of_days]","300");
        body.put("LeavePolicy_ClubbingOthers[status]","1");
        body.put("LeavePolicy_ClubbingOthers[clubbing_type]","0");
        body.put("LeavePolicy_OverUtilization[status]","1");
        body.put("LeavePolicy_OverUtilization[utilize_from]","1");
        body.put("LeavePolicy_OverUtilization[not_more_than_yearly]","0");
        body.put("LeavePolicy_OverUtilization[not_more_than_yearly_accrual]","0");
        body.put("LeavePolicy_OverUtilization[fixed_overutilization]","0");
        body.put("LeavePolicy_OverUtilization[other_leave]","");

        body.put("LeavePolicy_UnusedUserEncash[status]","0");
        body.put("LeavePolicy_UnusedUserEncash[user_encash_type]","0");
        body.put("LeavePolicy_UnusedUserEncash[minimum_encash_leaves]","0");
        body.put("LeavePolicy_UnusedUserEncash[remaining_balance]","0");
        body.put("LeavePolicy_UnusedUserEncash[user_encash_amount]","0");

        return body;
    }

}
