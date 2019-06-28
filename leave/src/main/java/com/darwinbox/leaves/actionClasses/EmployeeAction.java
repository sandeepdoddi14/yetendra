package com.darwinbox.leaves.actionClasses;

import Service.LeaveBalanceAPI;
import com.codoid.products.fillo.Recordset;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeAction extends TestBase {
    private static String empId;

    public EmployeeAction(String empId) {
        EmployeeAction.empId = empId;
    }

    //gets leave Balance Of Emp
    //Input    -->    empId
    public double getCurrentLeaveBalanceOfEmp(String leavePolicy) {
        return new LeaveBalanceAPI(empId, leavePolicy).getBalance();
    }


    /**
     * This method changes Employee Date of Joining
     *
     * @param iterationDate
     * @return DOJ as String
     */
    public String changeEmployeeDOJ(LocalDate iterationDate) {
        try {
            String DOJ = null;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DOJ = iterationDate.format(formatter);
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/emailtemplate/employeedoj?id=" + empId + "&date=" + DOJ;
                driver.navigate().to(URL);
                String frontEndDOJ = driver.findElement(By.xpath("//body")).getText();

                if (frontEndDOJ.trim().equals("DOJ Not changed")) {
                    for (int i = 0; i < 3; i++) {
                        driver.navigate().to(URL);
                        frontEndDOJ = driver.findElement(By.xpath("//body")).getText();
                        if (frontEndDOJ.trim().equals(DOJ)) {
                            break;
                        }
                    }
                }

                if (!frontEndDOJ.trim().equals(DOJ)) {
                    Reporter("DOJ not changed to '" + DOJ + "'", "Warning");
                }
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                DOJ = changeEmployeeDOJ(iterationDate);
            }
            return DOJ;
        } catch (Exception e) {
            Reporter("Exception while changing employees DOJ", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //updates the DOJ
    //to match Leave Balance
    //given as input param
    public void setEmpLeaveBal(int requiredLeaves, Recordset leavePolicy) {
        try {
            changeEmployeeDOJ(DateTimeHelper.getCurrentDateLocalDateFormat().minusMonths(requiredLeaves - 1));
            new CronAction().runCarryFrowardCronByEndPointURL(empId, leavePolicy.getField("Leave Cycle"), leavePolicy.getField("Leave_Type"));
            if (new EmployeeAction(empId).getCurrentLeaveBalanceOfEmp(leavePolicy.getField("Leave_Type")) < requiredLeaves)
                changeEmployeeDOJ(DateTimeHelper.getCurrentDateLocalDateFormat().minusMonths(requiredLeaves));
            new CronAction().runCarryFrowardCronByEndPointURL(empId, leavePolicy.getField("Leave Cycle"), leavePolicy.getField("Leave_Type"));
        } catch (com.codoid.products.exception.FilloException e) {
            Reporter("Exeption while retreving policy details from Excel", "Error");
            e.printStackTrace();
        }
    }


}
