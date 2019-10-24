package com.darwinbox.leaves.Services;


import com.darwinbox.core.Services;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.*;


public class LeaveService extends Services {

    /*
    @argument companyID
    @return allLeaveIds
     */
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

    /*
   @argument companyID,leavename
   @return leaveId
    */
    public String getLeaveID(String leavename,String companyId) {

        if ( companyId.trim().length() == 0)
            companyId = "main";
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


    public List<Map<String,Object>> calculateDuration(String userId, LocalDate fromDate, LocalDate toDate, String leaveID, Boolean halfDay){
        String url= data.get("@@url")+ UtilityHelper.getProperty("ServiceUrls","calculateDuration");

        Map<String,String> body= new HashMap<>();
        body.put("user_id",userId);
        body.put("from_date",fromDate.toString());
        body.put("to_date",toDate.toString());
        body.put("leave_change",leaveID);
        body.put("is_half_day",halfDay?1+"":0+"");//default
        body.put("is_today: 0","0");//default
        body.put("is_firsthalf_secondhalf","1");//default
        body.put("attachment","");//default


        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");


        String response=doPost(url,headers,mapToFormData(body));
        JSONObject objResponse = new JSONObject(response);

        List<Map<String,Object>> leaveDetails= new ArrayList<>();

        if (objResponse != null && objResponse.getString("status").equals("success")) {
         for(int i=0;i<(Integer)objResponse.get("count");i++){
                 JSONObject temp=(JSONObject)objResponse.getJSONArray("leave_details").get(i);
                 leaveDetails.add(temp.toMap());
         }
         return  leaveDetails;
        }
        else{
            throw new RuntimeException("Error in API Call - Calcualte Duration");
        }
    }


    public String createLeave(String leavename,String company) {

        String leaveId = getLeaveID(leavename,company );

        if ( leaveId == null ) {
            Map<String,String> body = new HashMap<>();

            body.put("Leaves[name]",leavename);
            body.put("Leaves[parent_company_id]",company);
            body.put("yt", "Save" );
            createLeave(body );
            leaveId = getLeaveID(leavename,company );
        }


        return leaveId;

    }

    public void updateLeave(Map<String, String> leaveBody) {

        Map<String,String> body = getDefaultforLeaveDeduction();
        body.putAll(leaveBody);
        String url = getData("@@url") + "/settings/editLeave";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));

    }

    /*
    updates leave
    @overloading (namevaluepair,leavename,companyid)
     */
    public void updateLeave(List<NameValuePair> leaveBody,String leavename,String companyId) {
        String leaveId=getLeaveID(leavename,companyId);

        leaveBody.add(new BasicNameValuePair("Leaves[id]",leaveId));

        String url = getData("@@url") + "/settings/editLeave";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, leaveBody);
    }

    /*
    updates Leave
    @@overloading (map,leavename,companyid)
     */
    public void updateLeave(Map<String, String> leaveBody,String leavename,String companyId) {

        String leaveId=getLeaveID(leavename,companyId);

        leaveBody.put("Leaves[id]",leaveId);

        Map<String,String> body = getDefaultforLeaveDeduction();
        body.putAll(leaveBody);
        String url = getData("@@url") + "/settings/editLeave";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));

    }


    //creates leave
    public void createLeave(Map<String,String> leaveBody){
        Map<String, String> body = getDefaultforLeaveDeduction();
        body.putAll(leaveBody);
        String url = getData("@@url") + "/settings/leaves/create";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }

    /*
  creates leaves
  @@OverLoading (map,leavename,company)
   */
    public boolean createLeave(Map<String, String> leaveBody, String leavename, String company) {
        try {
            String leaveId = getLeaveID(leavename, company);
            if (leaveId != null) {
                updateLeave(leaveBody, leavename, company);
            } else {
                Map<String, String> body = getDefaultforLeaveDeduction();
                body.putAll(leaveBody);
                String url = getData("@@url") + "/settings/leaves/create";
                Map headers = new HashMap();
                headers.put("X-Requested-With", "XMLHttpRequest");
                doPost(url, headers, mapToFormData(body));
            }
            return true;
        } catch (Exception e) {
            Reporter("Error While Creating/Updating Leave Policy", "ERROR");
            return false;
        }

    }

    /*
    creates leaves
    @@OverLoading (namevaluepair,leavename,company)
     */
    public boolean createLeave(List<NameValuePair> leaveBody, String leavename, String company) {
       try {
           String leaveId = getLeaveID(leavename, company);
           if (leaveId != null) {
               updateLeave(leaveBody, leavename, company);
           } else {
               String url = getData("@@url") + "/settings/leaves/create";
               Map headers = new HashMap();
               headers.put("X-Requested-With", "XMLHttpRequest");
               doPost(url, headers, leaveBody);
           }
           return true;
       } catch (Exception e) {
           Reporter("Error While Creating/Updating Leave Policy", "ERROR");
           return false;
       }

    }



    //sets mandatory fields
    public Map<String, String> mandatoryFieldsToCreateLeave(LeavePolicyObject leavePolicyObject) {
        Map<String, String> requestBody = new HashMap<>();

        //set assignment type
        if (leavePolicyObject.getAssignment_Type().equalsIgnoreCase("company wise")) {
            requestBody.put("Leaves[assignment_type]", "0");
            Reporter("Assignment Type is set to Company Wise", "Info");
        }

        if ((leavePolicyObject.getAssignment_Type().equalsIgnoreCase("assignment framework"))) {
            requestBody.put("Leaves[assignment_type]", "1");
            Reporter("Assignment Type is set to Assignment framework", "Info");
        }

        //set group company
        if (leavePolicyObject.getGroup_Company() != null) {
            requestBody.put("Leaves[parent_company_id]", leavePolicyObject.groupCompanyMongoId);
            Reporter("Group Company is set to  " +  leavePolicyObject.getGroup_Company(), "Info");
        }

        //set leave type
        if (leavePolicyObject.getLeave_Type() != null) {
            requestBody.put("Leaves[name]",  leavePolicyObject.getLeave_Type());
            Reporter("Leave Type is   " +  leavePolicyObject.getLeave_Type(), "Info");
        }

        //set description
        if (leavePolicyObject.getDescription() != null) {
            requestBody.put("Leaves[description]", leavePolicyObject.getDescription());
            Reporter("Description is   " +  leavePolicyObject.getDescription(), "Info");
        }

        //Maximum Leave Allowed Per Year
        if (leavePolicyObject.getMaximum_leave_allowed_per_year() != 0) {
            requestBody.put("Leaves[yearly_endowment]",  leavePolicyObject.getMaximum_leave_allowed_per_year()+"");
            Reporter("Maximum Leave Allowed Per Year  is    " +  leavePolicyObject.getMaximum_leave_allowed_per_year(), "Info");
        }

        return requestBody;
    }

    public void createLeaveForPolicy(Map<String, String> requestBody, LeavePolicyObject leavePolicyObject) {
        createLeave(requestBody, leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
    }

    public void createLeaveForPolicy(List<NameValuePair> requestBody, LeavePolicyObject leavePolicyObject) {
        createLeave(requestBody, leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
    }


    /*
    returns defualt reuqest
    Create Leave Policy
     */
    public Map<String,String> getDefaultforLeaveDeduction(){
        Map<String,String> body = new HashMap<String,String>();
        body.put("Leaves[assignment_type]","0");
        body.put("Leaves[description]","");
        body.put("Leaves[yearly_endowment]","0");
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
        body.put("LeavePolicyTenure[leaves_per_year][0][from_year]","0");
        body.put("LeavePolicyTenure[leaves_per_year][0][to_year]","0");
        body.put("LeavePolicyTenure[leaves_per_year][0][leaves]","");
        body.put("LeavePolicy_HalfDays[status]","0");
        body.put("LeavePolicy_UnusedCarryover[status]","0");
        body.put("LeavePolicy_UnusedCarryover[carry_forward]","0");
        body.put("LeavePolicy_UnusedCarryover[carry_forward_amount_type]","0");
        body.put("LeavePolicy_UnusedCarryover[carry_forward_amount]","0");
        body.put("LeavePolicy_UnusedCarryover[remaining]","0");
        body.put("LeavePolicy_UnusedFandFEncash[status]","0");
        body.put("LeavePolicy_UnusedFandFEncash[fandf_encash]","0");
        body.put("LeavePolicy_UnusedFandFEncash[fandf_encash_amount_type]","0");
        body.put("LeavePolicy_UnusedFandFEncash[fandf_encash_amount]","0");

        body.put("LeavePolicy_InterveningHolidays[status]","0");
        //body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]","1");
       // body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]","1");

        body.put("LeavePolicyPrefixSuffix[status]","0");
        body.put("LeavePolicyPrefixSuffix[prefix_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[suffix_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[prefix_holiday]","0");
        body.put("LeavePolicyPrefixSuffix[suffix_holiday]","0");
        body.put("LeavePolicy_PreviousDates[status]","0");

        body.put("LeavePolicy_PreviousDates[no_of_days]","300");

        body.put("LeavePolicy_ClubbingOthers[status]","1");//allowing default clubbing
        body.put("LeavePolicy_ClubbingOthers[clubbing_type]","0");
        body.put("LeavePolicy_OverUtilization[status]","0");
        body.put("LeavePolicy_OverUtilization[utilize_from]","0");
        body.put("LeavePolicy_OverUtilization[not_more_than_yearly]","0");
        body.put("LeavePolicy_OverUtilization[not_more_than_yearly_accrual]","0");
        body.put("LeavePolicy_OverUtilization[fixed_overutilization]","0");


        body.put("LeavePolicy_UnusedUserEncash[status]","0");
        body.put("LeavePolicy_UnusedUserEncash[user_encash_type]","0");
        body.put("LeavePolicy_UnusedUserEncash[minimum_encash_leaves]","0");
        body.put("LeavePolicy_UnusedUserEncash[remaining_balance]","0");
        body.put("LeavePolicy_UnusedUserEncash[user_encash_amount]","0");

        return body;
    }

}
