package com.darwinbox.leaves.LeaveSettings.Over_Utilization;

import Service.Service;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Verify_Accordion_ApplyMoreThanLeaveBal  extends TestBase {
    private static final Logger log = Logger.getLogger(Verify_Accordion_ApplyMoreThanLeaveBal.class);

    LoginPage loginpage;

 @BeforeMethod
    public void initializeObjects() {
     loginpage = PageFactory.initElements(driver, LoginPage.class);
       /* objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
        leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
        createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
        rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        objUtil = PageFactory.initElements(driver, UtilityHelper.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
    }*/
 }
    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass( ).getName( ));
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Verify_Accordion_CanEmpApplyMoreThanAvailableLeaveBal(Map<String, String> data) throws Exception{
        loginpage.empLoginToApplication();
    String url="https://automation1.qa.darwinbox.io/employee/GetLeave#sBalancesOnBehalf";
    HashMap<String,String> h=null;
   /* NameValuePair nv= new BasicNameValuePair("","");
    nv.id="id";
    nv.value=String.valueOf("5c51a767cc854");*/
/*    List<NameValuePair> form= new ArrayList<>();
    form.add()*/
   // NameValuePair nv= new BasicNameValuePair("if","dsa");

        List<org.apache.http.NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("id", "5c51a767cc854"));
    /*    List<NameValuePair>  nvp= new ArrayList<>();
        nvp.add(new BasicNameValuePair("id","5c51a767cc854"));*/
       // loginpage.empLoginToApplication();
        Service s= new Service();
        s.doPost(url,h,formData);
        System.out.println(s.doPost(url,h,formData));
        System.out.println("testing....");
    }
}
