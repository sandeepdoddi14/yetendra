package com.darwinbox.leaves.LeaveTransfer;

import java.time.LocalDate;
import java.util.Map;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Verify_Effective_Date_Leave_Balance_of_an_employee extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
	CommonSettingsPage commonSettings;
	LeavesSettingsPage leaveSettings;
	CreateAndManageLeavePoliciesPage createManageLeaves;
	RightMenuOptionsPage rightMenuOption;
	LeavesAction leavesAction;
	UtilityHelper objUtil;
	CommonAction commonAction;
	private static int i = 1;
	
	private static final Logger log = Logger.getLogger(Verify_Effective_Date_Leave_Balance_of_an_employee.class);

	@BeforeClass
	public void setup() throws Exception {
		ms.getDataFromMasterSheet(this.getClass().getName());
	}

	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
		leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
		createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
		leavesAction = PageFactory.initElements(driver, LeavesAction.class);
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		commonAction = PageFactory.initElements(driver, CommonAction.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
	public void Verify_Leave_Balance_is_calculated_correctly(Map<String,String> data) throws Exception {
	try {	
		Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
		Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
		Assert.assertTrue(leavesAction.setEmployeeID(UtilityHelper.getProperty("config","Employee.id")), "Employee ID is set successfully to test");
		leavesAction.removeEmployeeLeaveLogs();
		leavesAction.getAllEmployeeTypesInInstance();
		leavesAction.changeEmployeeType("Full Time");
		leavesAction.getEmployeeData();
		leavesAction.changeEmployeeType("Part Time");
		int tem = currentData;
			i = 1;
		while (i < dataCounter) {
			currentData = i;
			try {
			this.data = dataItem.get(currentData);
			wrapperMethod();
			}catch(Exception e) {		
			}
			i++;
		}
		currentData = tem;
		Assert.assertTrue(leavesAction.setLeaveType1(), "Leave scenario is set successfully");		
		Assert.assertTrue(leavesAction.changeEmployeeType("Full Time"), "Click on Settings link");	
		Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");		
		leavesAction.verifyEffectiveDateLeaveBalanceForParticularDOJ(DateTimeHelper.getCurrentLocalDate());

	}catch(Exception e) {
		Reporter("Exception in Verify_Effective_Date_Leave_Balance_of_an_employee", "Fatal");
	}
}
	
	public void wrapperMethod() {
		try {
		Assert.assertTrue(leavesAction.setLeaveType(), "Leave scenario is set successfully");		
		Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");		
		leavesAction.verifyEffectiveDateLeaveBalanceForParticularDOJ(DateTimeHelper.getCurrentLocalDate());
		}catch(Exception e) {			
			}
		}
}
