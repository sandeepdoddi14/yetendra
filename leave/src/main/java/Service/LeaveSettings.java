package Service;

import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;

import java.util.HashMap;
import java.util.Map;

public class LeaveSettings  extends  Service{


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
}
