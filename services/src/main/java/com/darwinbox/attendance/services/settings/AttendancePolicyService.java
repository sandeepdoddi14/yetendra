package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.services.Services;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttendancePolicyService extends Services {


    private JSONArray getAttendancePolicyList() {

        String url = getData("@@url") + "/settings/getAttendancePolicyList";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject objResponse = new JSONObject(doGet(url, headers));

        if (objResponse != null && objResponse.get("aaData") != null) {
            return objResponse.getJSONArray("aaData");
        } else {
            log.error(objResponse);
            throw new RuntimeException("ERROR: Unable to get Attendance Policy List");
        }
    }

    public String getAttendancePolicyId(String policyname, String groupCompanyName) {

        JSONArray arr = getAttendancePolicyList();

        String policy_id = null;

        for (Object policy : arr) {

            JSONArray policyData = (JSONArray) policy;

            String name = policyData.getString(0);
            String company = policyData.getString(1);

            name = name.substring(name.indexOf("</a>") + 4);

            if (company.equalsIgnoreCase(groupCompanyName) && name.equalsIgnoreCase(policyname)) {
                policy_id = policyData.getString(2);
                break;
            }

        }

        if (policy_id == null)
            return policy_id;

        Pattern p = Pattern.compile("id=\"\\w+\"");
        Matcher m = p.matcher(policy_id);
        if (m.find()) {
            policy_id = StringUtils.substringsBetween(m.group(0), "\"", "\"")[0];
        }

        return (policy_id);

    }

    public AttendancePolicy getAttendancePolicy(String policy_id) {

        String url = getData("@@url") + "/attendance/attendancePolicy/index?id=" + policy_id + "&type=json";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers)).getJSONObject("update");

        AttendancePolicy policy = new AttendancePolicy();
        policy.jsonToObject(response.toMap());

        return policy;

    }

    public Map<String, String> getDefaultforAttendance() {

        Map<String, String> body = new HashMap<>();

        body.put("AttendancePolicyForm[grace_time]", "0");
        body.put("AttendancePolicyForm[grace_time_early]", "0");

        body.put("AttendancePolicyForm[max_optional_holiday_policy]", "0");
        body.put("AttendancePolicyForm[auto_approve_optional_holidays]", "0");

        body.put("AttendancePolicyForm[markin_policy]", "2");
        body.put("AttendancePolicyForm[work_from_home]", "0");
        body.put("AttendancePolicyForm[out_duty]", "0");

        body.put("AttendancePolicyForm[edit_back_days]", "90");
        body.put("AttendancePolicyForm[apply_back_days_employee]", "90");
        body.put("AttendancePolicyForm[roster_back_days]", "90");
        body.put("AttendancePolicyForm[auto_approve_days]", "0");
        body.put("AttendancePolicyForm[absconding_days]", "");

        body.put("AttendancePolicyForm[disable_work_duration]", "0");
        body.put("AttendancePolicyForm[disable_break_duration]", "0");
        body.put("AttendancePolicyForm[disable_final_work_duration]", "0");
        body.put("AttendancePolicyForm[disable_late_mark]", "0");
        body.put("AttendancePolicyForm[disable_early_out]", "0");
        body.put("AttendancePolicyForm[disable_overtime]", "0");

        body.put("AttendancePolicyForm[allow_attrequest_weekoff]", "0");
        body.put("AttendancePolicyForm[allow_attrequest_holiday]", "0");

        body.put("AttendancePolicyForm[clockin_allowed_attendance_regularise]", "0");
        body.put("AttendancePolicyForm[od_allowed_attendance_regularise]", "0");

        body.put("AttendancePolicyForm[short_leave_allowed_attendance_regularise]", "0");
        body.put("AttendancePolicyForm[short_leave_allowed_days]", "");
        body.put("AttendancePolicyForm[short_leave_min_mins]", "");
        body.put("AttendancePolicyForm[short_leave_max_mins]", "");

        body.put("AttendancePolicyForm[allow_attendance_request_future]", "0");
        body.put("AttendancePolicyForm[allow_shift_change_request]", "0");
        body.put("AttendancePolicyForm[allow_auto_shift_assignment]", "0");
        body.put("AttendancePolicyForm[nearest]", "2");

        body.put("AttendancePolicyForm[pre_post]", "0");
        body.put("AttendancePolicyForm[pre_time_hour]", "0");
        body.put("AttendancePolicyForm[pre_time_min]", "0");
        body.put("AttendancePolicyForm[post_time_hour]", "0");
        body.put("AttendancePolicyForm[post_time_min]", "0");

        body.put("AttendancePolicyForm[leave_deduction_policy]", "0");
        body.put("AttendancePolicyForm[leave_deduction]", "");
        body.put("AttendancePolicyForm[leave_deduction_deduct_after_approval]", "0");
        body.put("AttendancePolicyForm[leave_deduction_on_holiday]", "0");
        body.put("AttendancePolicyForm[leave_deduction_on_weeklyoff]", "0");

        body.put("AttendancePolicyForm[work_duration_policy]", "0");
        body.put("AttendancePolicyForm[clock_in_hrs_half_day]", "0");
        body.put("AttendancePolicyForm[clock_in_min_half_day]", "0");
        body.put("AttendancePolicyForm[work_duration_leave_type]", "");
        body.put("AttendancePolicyForm[clock_in_hrs_full_day]", "0");
        body.put("AttendancePolicyForm[clock_in_min_full_day]", "0");
        body.put("AttendancePolicyForm[work_duration_deduct_after_approval]", "0");
        body.put("AttendancePolicyForm[work_duration_deduction_on_holiday]", "1");
        body.put("AttendancePolicyForm[work_duration_deduction_on_weeklyoff]", "0");
        body.put("AttendancePolicyForm[calculate_on_net_work_duration]", "0");

        body.put("AttendancePolicyForm[maxin_policy]", "0");
        body.put("AttendancePolicyForm[maxin_clock_in_hrs_half_day]", "0");
        body.put("AttendancePolicyForm[maxin_clock_in_min_half_day]", "0");
        body.put("AttendancePolicyForm[maxin_leave_type]", "");
        body.put("AttendancePolicyForm[maxin_clock_in_hrs_full_day]", "0");
        body.put("AttendancePolicyForm[maxin_clock_in_min_full_day]", "0");
        body.put("AttendancePolicyForm[maxin_deduct_after_approval]", "0");
        body.put("AttendancePolicyForm[maxin_deduction_on_holiday]", "0");
        body.put("AttendancePolicyForm[maxin_deduction_on_weeklyoff]", "0");

        body.put("AttendancePolicyForm[lateplusearlymark_policy]", "0");
        body.put("AttendancePolicyForm[lateplusearlymark_leave_deduction]", "");
        body.put("AttendancePolicyForm[lateplusearlymark_leave_type]", "");
        body.put("AttendancePolicyForm[number_of_lateplusearlymarks]", "1");
        body.put("AttendancePolicyForm[every_next_lateplusearlymark]", "0");
        body.put("AttendancePolicyForm[lateplusearlymark_deduct_after_approval]", "0");
        body.put("AttendancePolicyForm[lateplusearlymark_deduction_on_holiday]", "0");
        body.put("AttendancePolicyForm[lateplusearlymark_deduction_on_weeklyoff]", "0");
        body.put("AttendancePolicyForm[lateplusearlymark_both_on_sameday_count]", "2");

        body.put("AttendancePolicyForm[latemark_policy]", "0");
        body.put("AttendancePolicyForm[latemark_leave_deduction]", "");
        body.put("AttendancePolicyForm[latemark_leave_type]", "");
        body.put("AttendancePolicyForm[number_of_latemarks]", "1");
        body.put("AttendancePolicyForm[every_next_latemark]", "0");
        body.put("AttendancePolicyForm[latemark_deduct_after_approval]", "0");
        body.put("AttendancePolicyForm[latemark_deduction_on_holiday]", "0");
        body.put("AttendancePolicyForm[latemark_deduction_on_weeklyoff]", "0");

        body.put("AttendancePolicyForm[earlymark_policy]", "0");
        body.put("AttendancePolicyForm[earlymark_leave_deduction]", "");
        body.put("AttendancePolicyForm[earlymark_leave_type]", "");
        body.put("AttendancePolicyForm[number_of_earlymarks]", "1");
        body.put("AttendancePolicyForm[earlymark_deduct_after_approval]", "0");
        body.put("AttendancePolicyForm[earlymark_deduction_on_holiday]", "0");
        body.put("AttendancePolicyForm[earlymark_deduction_on_weeklyoff]", "0");

        body.put("AttendancePolicyForm[change_date]", "0");
        body.put("AttendancePolicyForm[policy_description]", " Created via Automation");

        return body;
    }

    public String createPolicy(AttendancePolicy policy) {

        Map<String, String> body = getDefaultforAttendance();

        body.putAll(policy.getMap());
        body.put("AttendancePolicyForm[id]", "");

        String url = getData("@@url") + "/attendance/attendancePolicy/create";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, mapToFormData(body));
        waitForUpdate(3);
        return response;
    }

    public void createAttendancePolicy(AttendancePolicy policy) {
        String response = createPolicy(policy);
        if (!response.contains("Attendance policy created successfully.")) {
            throw new RuntimeException(" Error in creating attendance policy ");
        }
    }

    public String updatePolicy(AttendancePolicy policy) {
        Map<String, String> body = getDefaultforAttendance();
        body.putAll(policy.getMap());
        String url = getData("@@url") + "/attendance/attendancePolicy/update";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doPost(url, headers, mapToFormData(body));
        return response;
    }

    public void updateAttendancePolicy(AttendancePolicy policy) {
        String response = updatePolicy(policy);
        if (!response.contains("Attendance policy updated successfully.")) {
            throw new RuntimeException(" Error in updating attendance policy ");
        }
    }

    public String deletePolicy(AttendancePolicy policy) {

        String url = getData("@@url") + "/attendance/attendancePolicy/delete?id=" + policy.getPolicyInfo().getPolicyID();
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);

        return response;
    }


    public void deleteAttendancePolicy(AttendancePolicy policy) {

        String response = deletePolicy(policy);

        if (!response.contains("Attendance Policy deleted successfully.")) {
            throw new RuntimeException(" Error in deleting attendance policy ");
        }
    }


}

