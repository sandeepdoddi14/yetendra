package com.darwinbox.attendance.pages.settings;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.services.Services;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceImportPage {

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    private String inTime;
    private String outTime;
    private String breakDuration;
    private String employeeId;
    private Date shitDate;
    private String inTimeDate;
    private String outTimeDate;
    private String shiftName;
    private String policyName;
    private boolean isWeekoff;

    public boolean isWeekoff() {
        return isWeekoff;
    }

    public void setWeekoff(boolean weekoff) {
        isWeekoff = weekoff;
    }

    public String getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(String breakDuration) {
        this.breakDuration = breakDuration;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getShitDate() {
        return shitDate;
    }

    public void setShitDate(Date shitDate) {
        this.shitDate = shitDate;
    }

    public String getInTimeDate() {
        return inTimeDate;
    }

    public void setInTimeDate(String inTimeDate) {
        this.inTimeDate = inTimeDate;
    }

    public String getOutTimeDate() {
        return outTimeDate;
    }

    public void setOutTimeDate(String outTimeDate) {
        this.outTimeDate = outTimeDate;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Map<String, String> getWorkDuration() {

        DateTimeHelper helper = new DateTimeHelper();
        String shiftDate = helper.formatDateTo(shitDate, "dd-MM-yyyy");

        Map<String, String> body = new HashMap<>();
        body.put("UserAttendanceImportBack[2][0]", getEmployeeId());
        body.put("UserAttendanceImportBack[2][1]", shiftDate);
        body.put("UserAttendanceImportBack[2][2]", getInTimeDate());
        body.put("UserAttendanceImportBack[2][3]", getInTime());
        body.put("UserAttendanceImportBack[2][4]", getOutTimeDate());
        body.put("UserAttendanceImportBack[2][5]", getOutTime());
        body.put("UserAttendanceImportBack[2][6]", getShiftName());
        body.put("UserAttendanceImportBack[2][7]", getPolicyName());
        body.put("UserAttendanceImportBack[2][8]", isWeekoff() ? "All" : "None");
        body.put("UserAttendanceImportBack[2][9]", getBreakDuration());

        return body;
            }

}
