package com.darwinbox.leaves.Objects;




import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ApprovalLevels {

    private List<Employee> employees;
    private List<String > roleHolders;
    private boolean allowRevoke;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<String> getRoleHolders() {
        return roleHolders;
    }

    public void setRoleHolders(List<String> roleHolders) {
        this.roleHolders = roleHolders;
    }

    public boolean isAllowRevoke() {
        return allowRevoke;
    }

    public void setAllowRevoke(boolean allowRevoke) {
        this.allowRevoke = allowRevoke;
    }

        public List<NameValuePair> getMap(String level) {

        List<NameValuePair> body = new ArrayList<>();

        if (employees == null || employees.size() == 0) {
            body.add(new BasicNameValuePair("user[level_"+level+"_checkbox]", "0"));
            body.add(new BasicNameValuePair("user[level_"+level+"_elastic]", ""));
        } else {
            body.add(new BasicNameValuePair("user[level_"+level+"_checkbox]", "1"));
            String list = "";
            for (Employee emp : employees) {
                list += "," + emp.getUserID();
            }
            body.add(new BasicNameValuePair("user[level_"+level+"_elastic]", list.substring(1)));
        }


        body.add(new BasicNameValuePair("user[level_"+level+"_revoke]", LeaveDeductionsBase.parseToPHP(isAllowRevoke())));


        if (roleHolders == null || roleHolders.size() == 0) {
            body.add(new BasicNameValuePair("user[level"+level+"][]", ""));
        } else {
            for (String roleHolder : roleHolders) {
                body.add(new BasicNameValuePair("user[level"+level+"][]", roleHolder));
            }

        }


        return body;
    }

}
