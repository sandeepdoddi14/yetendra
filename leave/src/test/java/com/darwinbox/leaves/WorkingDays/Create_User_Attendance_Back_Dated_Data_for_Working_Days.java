package com.darwinbox.leaves.WorkingDays;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.leaves.actionClasses.ImportAction;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Create_User_Attendance_Back_Dated_Data_for_Working_Days extends TestBase {

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
	ImportAction objImport;
	
	private static final Logger log = Logger.getLogger(Create_User_Attendance_Back_Dated_Data_for_Working_Days.class);

	@BeforeClass
	public void setup() throws Exception {
		ms.getDataFromMasterSheet(this.getClass().getName());
/*		if(WriteResultToExcel.equalsIgnoreCase("Yes")) {
			ExcelWriter.copyExportFileToResultsDir();					
		}*/
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
		objImport = PageFactory.initElements(driver, ImportAction.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
	public void Verify_Leave_Balance_is_calculated_correctly(Map<String,String> data) throws Exception {

		objImport.createBackDatedAttendanceImportForProvidedYear("WD001", DateTimeHelper.getCurrentLocalDate(), "2017-02-28", "General", "General",
				"All Saturday and Sunday");
//		objImport.createLeavesImportForProvidedYear("A001", "Working Days", "Automation Testing Import",DateTimeHelper.getCurrentLocalDate(), "2018-04-18");
	}
}
