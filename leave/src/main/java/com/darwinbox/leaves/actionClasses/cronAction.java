package com.darwinbox.leaves.actionClasses;

import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

public class cronAction extends TestBase {
    UtilityHelper objUtil;


    public cronAction(){
        objUtil = PageFactory.initElements(driver, UtilityHelper.class);
    }


    public double runCarryFrowardCronByEndPointURL(String empId,String leaveCycle,String leaveType) {
        try {
            double actualLeaveBalance = 0;
            String applicationURL = data.get("@@url");
            int leaveCycleCode = 0;
            if (leaveCycle.contains("Calendar")){
                leaveCycleCode = 1;
            }else if (leaveCycle.contains("Financial")){
                leaveCycleCode = 2;
            } else {
                throw new RuntimeException("Leave Cycle is not Calendar or Financial, please check");
            }

            String URL = UtilityHelper.getProperty("allAPIRepository", "Run.Cron.API") + "\"CarryforwardLeavesnew\"&type=" + leaveCycleCode + "&eno=" + empId + "&leave=" + leaveType;
            objUtil.getHTMLTextFromAPI(driver, URL);
            String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText( );
            if (frontEndLeaveBalance.isEmpty( )) {
                Reporter("Carry Forward cron has not ran successfully", "Error");
                throw new RuntimeException("Carry Forward cron has not ran successfully");
            }
            actualLeaveBalance = Double.valueOf(frontEndLeaveBalance.replace("Job finished for tenant Id ==","").trim());
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end carry forward leave balance for the employee", "Error");
            e.printStackTrace( );
            throw new RuntimeException(e);
        }
    }

}
