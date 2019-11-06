/*
package com.darwinbox.leaves.Services;

import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;

public class LeaveBalanceFromDOM  extends Services {


    public HashMap<Object,Object> getleaveBalances(Employee employee){

        HashMap<Object,Object> map=new HashMap<>();
        String leavesURL=data.get("@@url") + "/leaves/user/index/id/"+employee.getUserID();

        driver.navigate().to(leavesURL);

        List<WebElement> leaves=driver.findElements(By.xpath("/html/body/div[2]/div[1]/div/div[3]/section[1]/div[1]/div[1]/div[4]/div"));
        for(WebElement leave : leaves){
            String leaveName=leave.findElement(By.className("smaller-header-bold mb-16 primary-hover")).getText();
            String leaveBalance=leave.findElement(By.className("primary-hover display-1 mb-8")).getText();
            map.put(leaveName,leaveBalance);
        }

        return  map;

    }

    public double getLeaveBalance(Employee employee,String leaveName){

    }
}
*/
