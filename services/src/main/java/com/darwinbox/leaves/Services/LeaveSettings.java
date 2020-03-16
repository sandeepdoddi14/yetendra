package com.darwinbox.leaves.Services;

import com.darwinbox.Services;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;

import java.util.HashMap;
import java.util.Map;

public class LeaveSettings  extends Services {




    public void setDenominatorForWorkingDaysAccural(String value)
    {
        String url = data.get("@@url")+ "/settings/employees/tenprofc";


        HashMap<String,String> body=defaultTemprofcBody();
        body.put("TenantProfile[leave_workingdays_denominator]",value);


        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        doPost(url,headers,mapToFormData(body));
    }
    public HashMap<String,String> defaultTemprofcBody(){
        HashMap<String,String> body= new HashMap<>();


        body.put("TenantProfile[show_employee_structure]","0");
        body.put("TenantProfile[show_employee_structure]","1");
        body.put("TenantProfile[show_functional_structure]","0");
        body.put("TenantProfile[show_functional_structure]","1");
        body.put("TenantProfile[show_only_self_company_structure]","0");
        body.put("TenantProfile[is_manager_mandatory]","0");
        body.put("TenantProfile[last_name_not_mandatory]","0");
        body.put("TenantProfile[face_recognition_required]","0");
        body.put("TenantProfile[block_resignation_manager_onbehalf]","0");
        body.put("TenantProfile[block_leave_manager_onbehalf]","0");
        body.put("TenantProfile[leave_allow_auto_reject_and_make_unpaid]","0");
        body.put("TenantProfile[auto_approve_sepration_days]","0");
        body.put("TenantProfile[exit_date_cannot_be_weeklyoff]","0");
        body.put("TenantProfile[goal_plan_enabled]","0");
        body.put("TenantProfile[goal_plan_enabled]","1");
        body.put("TenantProfile[block_manager_shift_edits]","0");
        body.put("TenantProfile[block_employee_profile_dep_structure]","0");
        body.put("TenantProfile[block_employee_profile_org_structure]","0");
        body.put("TenantProfile[allow_behaveas]","0");
        body.put("TenantProfile[stop_intercompany_transfer]","0");
        body.put("TenantProfile[duplicate_check_only_active_employees]","0");
        body.put("TenantProfile[duplicate_check_only_active_pending_employees]","0");
        body.put("TenantProfile[separation_others_hide]","0");
        body.put("TenantProfile[language]","0");
        body.put("TenantProfile[block_employee_profile_functional_structure]","0");
        body.put("TenantProfile[event_subevent_mandatory]","0");
        body.put("TenantProfile[dont_show_logo_in_payslip]","0");
        body.put("TenantProfile[allow_separation_mobile]","0");
        body.put("TenantProfile[replace_leave_allowed]","0");
        body.put("TenantProfile[neev_level_allowed]","0");
        body.put("TenantProfile[analytics]","0");
        body.put("TenantProfile[checkin_map_status]","0");
        body.put("TenantProfile[location_view_add_loctype]","0");
        body.put("TenantProfile[hrbp_mandatory]","0");
        body.put("TenantProfile[only_pms_manager]","0");

        body.put("TenantProfile[compulsory_policy_signoff]","0");
        body.put("TenantProfile[hide_reject_separation","0");
        body.put("TenantProfile[region_location_basis]","0");
        body.put("TenantProfile[disable_clockin_on_mobile]","0");
        body.put("TenantProfile[show_time_beside_clockin_button]","0");
        body.put("TenantProfile[dont_allow_unpaid_leave]","0");
        body.put("TenantProfile[pre_offer_mandatory]","0");
        body.put("TenantProfile[use_only_male_female_in_gender]","0");
        body.put("TenantProfile[position_mandatory_raise_requisition]","0");
        body.put("TenantProfile[disable_checkout_on_mobile]","0");
        body.put("TenantProfile[talent_profile_show_only_manager]","0");
        body.put("TenantProfile[talent_profile_ratings_show_only_manager]","0");
        body.put("TenantProfile[dont_allow_refral_option_adding_candidate]","0");
        body.put("TenantProfile[probation_mandatory","0");
        body.put("TenantProfile[add_employee_sift_policy_mandatory]","0");
        body.put("TenantProfile[block_helpdesk_issue_edit]","0");
        body.put("TenantProfile[block_concurrent_logins]","0");
        body.put("TenantProfile[xoxo_integration]","0");
        body.put("TenantProfile[send_all_managers_yest_attendance_status]","0");
        body.put("TenantProfile[travel_if_all_settled_only_then_allow_new]","0");
        body.put("TenantProfile[travel_advance_dont_allow_new]","0");
        body.put("TenantProfile[offerletter_generation_block_recruiter]","0");
        body.put("TenantProfile[cleartax_integration]","0");
        body.put("TenantProfile[block_different_device_login]","0");
        body.put("TenantProfile[l2_hod_allow_edit_form]","0");
        body.put("TenantProfile[recognition_received]","0");
        body.put("TenantProfile[recognition_colleague]","0");
        body.put("TenantProfile[recognition_reportee]","0");
        body.put("TenantProfile[leave_request]","0");
        body.put("TenantProfile[leave_approved]","0");
        body.put("TenantProfile[attendance_request]","0");
        body.put("TenantProfile[attendance_approved]","0");
        body.put("TenantProfile[reimbursement_request]","0");
        body.put("TenantProfile[reimbursement_approved]","0");
        body.put("TenantProfile[birthday_wishes]","0");
        body.put("TenantProfile[anniversary_wishes]","0");

        body.put("TenantProfile[alert_email]","");
        body.put("TenantProfile[request_email]","");
        body.put("TenantProfile[noreply_email]","");
        body.put("TenantProfile[minimum_age]","18");
        body.put("TenantProfile[push_api_error_to_email]","");
        body.put("TenantProfile[phone_restriction]","10");
        body.put("TenantProfile[compensation_positioning]","");
        body.put("TenantProfile[auto_logout_mins]","");
        body.put("TenantProfile[auto_logout_redirect_url]","");
        body.put("TenantProfile[base_currency]","");
        body.put("TenantProfile[letter_ctc_font_size]","");
        body.put("TenantProfile[letter_ctc_font]","");
        body.put("TenantProfile[letter_ctc_padding]","");
        body.put("TenantProfile[letter_break_address_multiple_lines]","");
        body.put("TenantProfile[after_login_landing_page]","");
        body.put("TenantProfile[retirement_year]","58");
        body.put("TenantProfile[login_retry_count]","");
        body.put("TenantProfile[login_retry_time]","");
        body.put("TenantProfile[leave_workingdays_denominator]","");


        return  body;
    }
    public String showLeaveAdjustments(String cycle)
    {
        Map<String,String> defaultBody=defaultBodyForLeaveSettings();
        defaultBody.put("TenantLeavesSettings[show_adjustment]","1");


        if(cycle.contains("calender"))
            defaultBody.put("TenantLeavesSettings[unpaid_refresh]","1");

        else if(cycle.contains("financial"))
            defaultBody.put("TenantLeavesSettings[unpaid_refresh","2");

        String url= data.get("@@url")+UtilityHelper.getProperty("ServiceUrls","updateSettings");


        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");


        return doPost(url,headers,mapToFormData(defaultBody));

    }

    public String saveLeaveSettings(Map<String,String> request)
    {
       Map<String,String> defaultBody=defaultBodyForLeaveSettings();
       defaultBody.putAll(request);

       String url= data.get("@@url")+UtilityHelper.getProperty("ServiceUrls","updateSettings");

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");


        return doPost(url,headers,mapToFormData(defaultBody));
    }

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

        defaultBody.put("TenantLeavesSettings[show_adjustment]","0");

        defaultBody.put("TenantLeavesSettings[hide_policy]","1");
        defaultBody.put("TenantLeavesSettings[allow_previous_year_application]","1");

        defaultBody.put("TenantLeavesSettings[type]", "0");
        defaultBody.put("TenantLeavesSettings[from_date]","");
        defaultBody.put("TenantLeavesSettings[to_date]","");
        defaultBody.put("TenantLeavesSettings[no_of_days]","");

        defaultBody.put("TenantLeavesSettings[encash_from_date]","");
        defaultBody.put("TenantLeavesSettings[encash_to_date]","");

        defaultBody.put("TenantLeavesSettings[approval_flow]","");

        //pass on the leave ids for the leave types which are not to be counted as present days
       // defaultBody.put("TenantLeavesSettings[cnt_present_leaves][]", "5af9cbf4bb7da");
        //defaultBody.put("TenantLeavesSettings[cnt_present_leaves][]", "5af9cc3f02e53");
        return defaultBody;
    }
}
