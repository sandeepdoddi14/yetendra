package Service;

import Objects.Employee;
import Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveAdmin extends Service {
    private String applyLeaveUrl = getData("@@url") + "/leaves/leaves/apply";
    private String getLeavesByOnBehalf = getData("@@url") + "/employee/GetLeavesByOnBehalf";

    /*
    Admin Apply Leave on Behalf of employee
     */
    public String ApplyLeave(String userId, String fromDate, String toDate, String leaveId) {
        Map<String, String> request = new HashMap<>();
        Map<String, String> body = applyLeaveWithDefaultValues();

        request.put("user_id", userId);
        request.put("from_date", fromDate);
        request.put("to_date", toDate);
        request.put("leave_change", leaveId);

        body.putAll(request);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        return new Service().doPost(applyLeaveUrl, headers, mapToFormData(body));
    }

    public String applyLeaveWithEmpSession(Employee e, String fromDate, String toDate, String leaveId) {
        Map<String, String> request = new HashMap<>();
        Map<String, String> body = applyLeaveWithDefaultValues();

        request.put("user_id", e.getUserID());
        request.put("from_date", fromDate);
        request.put("to_date", toDate);
        request.put("leave_change", leaveId);
        request.put("UserLeavesOther", "0");

        body.putAll(request);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Cookie", e.getPhpSessid());

        return new Service().doPost(applyLeaveUrl, headers, mapToFormData(body));
    }


    /*
    @Override halfDAY
     */
    public String ApplyLeave(String userId, String fromDate, String toDate, String leaveId, String whichHalfOfTheDay) {
        Map<String, String> request = new HashMap<>();
        Map<String, String> body = applyLeaveWithDefaultValues();

        request.put("user_id", userId);
        request.put("from_date", fromDate);
        request.put("to_date", toDate);
        request.put("leave_change", leaveId);
        request.put("is_half_day", "1");

        if (whichHalfOfTheDay.equalsIgnoreCase("firsthalf")) {
            request.put("is_firsthalf_secondhalf", "1");
        } else if (whichHalfOfTheDay.equalsIgnoreCase("secondhalf")) {
            request.put("is_firsthalf_secondhalf", "2");
        }

        body.putAll(request);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        return new Service().doPost(applyLeaveUrl, headers, mapToFormData(body));
    }


    /*
    admin gets leaves on behalf of employee
     */
    public Map<String, String> GetLeavesByOnBehalf(String userMongoId) {
        Map<String, String> request = new HashMap<>();
        Map<String, String> body = GetLeavesByOnBehalfDefaultParameters();

        request.put("id[]", userMongoId);

        body.putAll(request);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String leaves = new Service().doPost(getLeavesByOnBehalf, headers, mapToFormData(body));
        String leaves1 = leaves.replace("{\"status\":\"success\",\"update\":{", "").replace("}}", "");
        String[] leaves2 = leaves1.split(",");

        Map<String, String> temp = new HashMap<String, String>();

        for (int i = 0; i < leaves2.length; i++) {
            temp.put(leaves2[i].split(":")[1].replace("\"", ""), leaves2[i].split(":")[0].replace("\"", ""));
        }
        return temp;
    }

    /*
    default request body
    get leaves on behalf
     */
    public Map<String, String> GetLeavesByOnBehalfDefaultParameters() {
        Map<String, String> body = new HashMap<String, String>();
        body.put("id[]", null);
        return body;
    }


    /*
    default body
    apply leave on behalf
     */
    public Map<String, String> applyLeaveWithDefaultValues() {
        Map<String, String> body = new HashMap<String, String>();
        body.put("user_id", null);
        body.put("from_date", null);
        body.put("to_date", null);
        body.put("leave_change", null);
        body.put("is_half_day", "0");
        body.put("is_firsthalf_secondhalf", "0");
        body.put("message", "automation leave");
        body.put("name", null);
        body.put("Messages_type", "leave");
        body.put("source", "dashboard");
        body.put("selected_user_search", null);
        body.put("UserLeavesOther", "1");
        body.put("iOS", "1");
        body.put("form_id", null);
        return body;
    }


    //pass any date in between from date and to date
    public void revokeLeave(String leaveType, String leaveDate) {
        String empId = UtilityHelper.getProperty("config", "Employee.id");
        String url = data.get("@@url") + "/emailtemplate/getmessageid?id=" + empId + "&leave=" + leaveType + "&date=" + leaveDate;
        driver.get(url);
        String messageId = driver.findElement(By.xpath("//body")).getText();
        String leaveId = getLeaveIdForRevoke(messageId);

        if (messageId.isEmpty()) {
            Reporter("Error in getting messagae id for the leaveType and LeaveDate are" + leaveType + "and" + leaveDate, "Error");
        }

        String leaveActionUrl = data.get("@@url") + "/messages/leaveAction";
        Map<String, String> body = new HashMap<>();

        Map<String, String> request = revokeRequestDefaultBody();

        request.put("Messages[id]", messageId);
        request.put("Leave[id]", leaveId);

        body.putAll(request);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = new Service().doPost(leaveActionUrl, headers, mapToFormData(body));

        if (response.contains("success")) {
            Reporter("Leave Request Revoked Successfully for" + "date" + leaveDate, "Pass");
        } else {
            Reporter("Error in Revoking Leave for" + leaveType + "date" + leaveDate, "Fail");
        }
    }

    /*
    @Over Ride -- Employee Object
     */
    public void revokeLeave(Employee employee, String leaveType, String leaveDate) {
        String empId = employee.getEmployeeID();
        String url = data.get("@@url") + "/emailtemplate/getmessageid?id=" + empId + "&leave=" + leaveType + "&date=" + leaveDate;
        driver.get(url);
        String messageId = driver.findElement(By.xpath("//body")).getText();
        try {
            String leaveId = getLeaveIdForRevoke(messageId);

            if (messageId.isEmpty()) {
                Reporter("Error in getting messagae id for the leaveType and LeaveDate are" + leaveType + "and" + leaveDate, "Error");
            } else {
                String leaveActionUrl = data.get("@@url") + "/messages/leaveAction";
                Map<String, String> body = new HashMap<>();

                Map<String, String> request = revokeRequestDefaultBody();

                request.put("Messages[id]", messageId);
                request.put("Leave[id]", leaveId);

                body.putAll(request);

                Map<String, String> headers = new HashMap<>();
                headers.put("X-Requested-With", "XMLHttpRequest");

                String response = new Service().doPost(leaveActionUrl, headers, mapToFormData(body));

                if (response.contains("success")) {
                    Reporter("Leave Request Revoked Successfully for" + "date" + leaveDate, "Pass");
                } else {
                    Reporter("Error in Revoking Leave for" + leaveType + "date" + leaveDate, "Fail");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in Retrving Message ID to revoke Leave");
        }
    }

    /*
   @Over Ride -- Employee id
    */
    public void revokeLeave(String empID, String leaveType, String leaveDate) {
        String empId = empID;
        String url = data.get("@@url") + "/emailtemplate/getmessageid?id=" + empId + "&leave=" + leaveType + "&date=" + leaveDate;
        driver.get(url);
        String messageId = driver.findElement(By.xpath("//body")).getText();
        String leaveId = getLeaveIdForRevoke(messageId);

        if (messageId.isEmpty()) {
            Reporter("Error in getting messagae id for the leaveType and LeaveDate are" + leaveType + "and" + leaveDate, "Error");
        } else {
            String leaveActionUrl = data.get("@@url") + "/messages/leaveAction";
            Map<String, String> body = new HashMap<>();

            Map<String, String> request = revokeRequestDefaultBody();

            request.put("Messages[id]", messageId);
            request.put("Leave[id]", leaveId);

            body.putAll(request);

            Map<String, String> headers = new HashMap<>();
            headers.put("X-Requested-With", "XMLHttpRequest");

            String response = new Service().doPost(leaveActionUrl, headers, mapToFormData(body));

            if (response.contains("success")) {
                Reporter("Leave Request Revoked Successfully for" + "date" + leaveDate, "Pass");
            } else {
                Reporter("Error in Revoking Leave for" + leaveType + "date" + leaveDate, "Fail");
            }
        }
    }

    /*
    gets message ID for the leave
    Dependency -> Leaaves Automation Branch for MessageId
     */
    public String getMessageId(Employee employee,LeavePolicyObject leavePolicyObject,String leaveDate){
        String empId = employee.getEmployeeID();
        String url = data.get("@@url") + "/emailtemplate/getmessageid?id=" + empId + "&leave=" + leavePolicyObject.getLeave_Type() + "&date=" + leaveDate;
        driver.get(url);
        String messageId = driver.findElement(By.xpath("//body")).getText();


        if (messageId.isEmpty()) {
            throw  new RuntimeException("Error in getting messagae id for the leaveType and LeaveDate are" + leavePolicyObject.getLeave_Type() + "and" + leaveDate);
            //return  "Error in getting Message Id";
        } else {
            return messageId;
        }
    }

    /*
    Performs leave actions such as accept,decline,employee
    takes the phpSessionID of the employee and performs the leave action
    Example - Useful for roleHolders in approvalFlows
    */
    public String leaveAction(Employee employee, String messageId, String leaveAction) {

        String leaveId=getLeaveIdForRevoke(messageId);
            String leaveActionUrl = data.get("@@url") + UtilityHelper.getProperty("ServiceUrls","leaveActionAcceptOrReject");
            Map<String, String> body = new HashMap<>();

            Map<String, String> request = revokeRequestDefaultBody();

            request.put("Messages[id]", messageId);
            request.put("Leave[id]", leaveId);

            if(leaveAction.equalsIgnoreCase("accept")){
                request.put("action","approve");
            }
            if(leaveAction.equalsIgnoreCase("decline")){
                request.put("action","decline");
            }
            if(leaveAction.equalsIgnoreCase("revoke")){
                request.put("action","revoke");
            }

            body.putAll(request);

            Map<String, String> headers = new HashMap<>();
            headers.put("X-Requested-With", "XMLHttpRequest");

            if(employee!=null)
            headers.put("Cookie",employee.getPhpSessid());

            return doPost(leaveActionUrl, headers, mapToFormData(body));
    }

    public Map<String, String> revokeRequestDefaultBody() {
        Map<String, String> body = new HashMap<String, String>();
        body.put("Messages[id]", null);
        body.put("Leave[id]", null);
        body.put("action", "revoke");

        return body;
    }


    public String getLeaveIdForRevoke(String MessageId) {
        Map<String, String> request = threadrequestDefault();
        String url = data.get("@@url") + "/messages/threadrequest";
        request.put("id", MessageId);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = new Service().doPost(url, headers, mapToFormData(request));
        return response.split("value")[2].substring(3, 16);
    }


    public Map<String, String> threadrequestDefault() {
        Map<String, String> body = new HashMap<String, String>();
        body.put("id", null);
        body.put("mode", null);
        return body;
    }


    //checks if leave is displayed for user from admin end
    public boolean checkifLeaveIsDisplayedForUser(String userId, String leave) {
        Map<String, String> leaves = new LeaveAdmin().GetLeavesByOnBehalf(userId);
        try {
            boolean checkifLeaveisDisplayed = !leaves.get(leave).isEmpty();

            return true;
        } catch (NullPointerException e) {
            return false;
        }

    }


    //seperation done by admin
    //@@ reasons for resignation,sepearation are hard coded with ids in defaultBody
    //
    public boolean resignOnBehalf(HashMap<String, String> requestData) {
        String resourcePath = UtilityHelper.getProperty("ServiceUrls", "resignOnBehalf");
        String url = data.get("@@url") + resourcePath;


        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> defaultBody = seperationDefaultBody();
        defaultBody.putAll(requestData);

        String response = doPost(url, headers, mapToFormData(defaultBody));

        if (response.contains("success")) {
            Reporter("Info : Successfully Applied Resignation", "Info");
            return true;
        } else {
            Reporter("Error in Applying Resignation", "Error");
            return false;
        }
    }

    public Map<String, String> seperationDefaultBody() {
        HashMap<String, String> defaultBody = new HashMap<String, String>();
        defaultBody.put("user_id", "");
        defaultBody.put("cal_minus", "1");
        defaultBody.put("mongo_id", "");
        defaultBody.put("notice_period_days", "");
        defaultBody.put("notice_period_days_get", "");
        defaultBody.put("Separation[date_of_resignation]", "");
        defaultBody.put("Separation[requested_last_day]", "");
        defaultBody.put("Separation[recovery_days]", "");
        defaultBody.put("Separation[reason_for_resignation]", "5af9772ddb163");
        defaultBody.put("Separation[other_reason]", "");

        defaultBody.put("Separation[resignation_comments]", "");
        defaultBody.put("Separation[manager_proposed_last_day]", "");
        defaultBody.put("Separation[manager_proposed_recovery_days]", "");
        defaultBody.put("Separation[reason_for_proposed_recovery_days]", "test autoamtion");
        defaultBody.put("AddAttachmentWorkFlow[name]", "");
        defaultBody.put("AddAttachmentWorkFlow[name]", "(binary)");
        defaultBody.put("Separation[final_recovery_days]", "");
        defaultBody.put("Separation[final_recovery_days_reason]:", "test autoamtion");
        defaultBody.put("Separation[admin_separation_type]", "2");
        defaultBody.put("Separation[admin_separation_reason_v]", "5af9772ddb163");
        defaultBody.put("Separation[admin_separation_reason_iv]", "5af9772dd8f0c");
        defaultBody.put("Separation[admin_other_reason]", "");
        defaultBody.put("AddAttachmentWorkFlow[name]", "");
        defaultBody.put("AddAttachmentWorkFlow[name]", "(binary)");


        return defaultBody;
    }


    public String changeManagerBulk(String userId, String managerUserId, LocalDate effectiveDate)
    {
        String url= getData("@@url")+UtilityHelper.getProperty("ServiceUrls","changeManagerBulk");

        List<NameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("UserManager[user_id][]",userId));
        request.add(new BasicNameValuePair("User[manager_id]",managerUserId));

        String date= (effectiveDate.getDayOfMonth()<10 ? "0"+effectiveDate.getDayOfMonth() : effectiveDate.getDayOfMonth() )
                +"-" + (effectiveDate.getMonthValue()<10 ? "0"+effectiveDate.getMonthValue() : effectiveDate.getMonthValue())
                +"-" +effectiveDate.getYear();

        request.add(new BasicNameValuePair("User[effective_date]",date));

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        return doPost(url,headers,request);

    }


    public String updateDepartment(String departmentId,String departmentName,Employee hod){
        String url= data.get("@@url")+UtilityHelper.getProperty("ServiceUrls","updateDepartment");

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        List<NameValuePair> request = new ArrayList<>();

        request.add(new BasicNameValuePair("UserDepartments[id]",departmentId));
        request.add(new BasicNameValuePair("UserDepartments[department_name]",departmentName));
        request.add(new BasicNameValuePair("UserDepartments[department_code]",""));
        request.add(new BasicNameValuePair("UserDepartments[department_email]",""));
        request.add(new BasicNameValuePair("UserDepartments[parent_department]","DEPT_"+departmentId));
        request.add(new BasicNameValuePair("UserDepartments[departments_hod]",hod.getEmployeeID()+","+hod.getUserID()));

        return  doPost(url,headers,request);
    }


}
