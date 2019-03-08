package com.darwinbox.leaves.actionClasses;

import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.HTTPSClientHelper;
import com.darwinbox.framework.uiautomation.Utility.ExcelWriter;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportAction extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;
	DateTimeHelper objDateTimeHelper;
	UtilityHelper objUtil;
	CreateAndManageLeavePoliciesPage createManageLeaves;
	LeavesSettingsPage leaveSettings;
	ActionHelper objActionHelper;
	BrowserHelper objBrowserHelper;
	ExcelWriter objExcelWriter;
	HTTPSClientHelper objHTTPSClient;

	public static final Logger log = Logger.getLogger(ImportAction.class);

	public ImportAction(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
		objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
		objDateTimeHelper = PageFactory.initElements(driver, DateTimeHelper.class);
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
		objJavaScrHelper = PageFactory.initElements(driver, JavaScriptHelper.class);
		leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
		objActionHelper = PageFactory.initElements(driver, ActionHelper.class);
		objBrowserHelper = PageFactory.initElements(driver, BrowserHelper.class);
		objExcelWriter = PageFactory.initElements(driver, ExcelWriter.class);
		objHTTPSClient = PageFactory.initElements(driver, HTTPSClientHelper.class);
	}

	@FindBy(xpath = "#//span[text()='Choose a file']")
	public WebElement chooseFile;

	@FindBy(css = "#upload_import_file input']")
	public WebElement next;

	@FindBy(xpath = "//input[@value = 'Done mapping, Next']")
	public WebElement done;

	@FindBy(css = ".table-hover tbody tr select")
	public List<WebElement> horizontalHeaders;

	@FindBy(css = ".table-hover thead tr select")
	public List<WebElement> verticalHeaders;

	/**
	 * Imports the given file by mapping the data
	 *
	 * @param headers
	 *            is an array of column names to map while importing csv file
	 * @param fileName
	 *            file name to import, fetches the file from 'src/main/resources/'
	 */
	public void importData(String[] headers, String fileName) {
		chooseFile.click();
		objGenHelper.attachFile(fileName);
		next.click();
		List<WebElement> dropdowns = horizontalHeaders;
		if (dropdowns.size() == 0) {
			dropdowns = verticalHeaders;
		}
		for (int i = 0; i < dropdowns.size(); i++) {
			objDropDownHelper.selectUsingVisibleValue(dropdowns.get(i), headers[i], headers[i]);
		}
		done.submit();
		// TODO validation pending..........
		// List<WebElement> ele = driver.findElements(By.className("errorMessage"));

	}

	@SuppressWarnings("unlikely-arg-type")
	public void createBackDatedAttendanceImportForProvidedYear(String empID, String fromDate, String toDate,
			String shiftName, String PolicyName, String WeeklyOff) {
		try {

			LocalDate shiftDate = LocalDate.parse(fromDate);
			LocalDate inTimeDate = shiftDate;
			String inTime = "09:00:00";
			LocalDate outTimeDate = shiftDate;
			String outTime = "18:00:00";
			List<String[]> listStringObj = new ArrayList<>();
			String[] headers = new String[] { "Email/Employee ID", "Shift Date", "Intime Date", "In Time",
					"Outtime Date", "Out Time", "Shift Name", "Policy Name", "Weekly Off Name" };
			listStringObj.add(headers);

			while (shiftDate.isAfter(LocalDate.parse(toDate))) {
				shiftDate = shiftDate.minusDays(1);
				inTimeDate = shiftDate;
				outTimeDate = shiftDate;
				String[] dataToWrite = new String[9];
				dataToWrite[0] = empID;
				dataToWrite[1] = shiftDate.toString();
				if (shiftDate.getDayOfWeek().toString().equals("SUNDAY")
						|| shiftDate.getDayOfWeek().toString().equals("SATURDAY")) {
					dataToWrite[2] = "";
					dataToWrite[3] = "";
					dataToWrite[4] = "";
					dataToWrite[5] = "";
				} else {
					dataToWrite[2] = inTimeDate.toString();
					dataToWrite[3] = inTime;
					dataToWrite[4] = outTimeDate.toString();
					dataToWrite[5] = outTime;
				}
				dataToWrite[6] = shiftName;
				dataToWrite[7] = PolicyName;
				dataToWrite[8] = WeeklyOff;
				listStringObj.add(dataToWrite);
			}

			ExcelWriter.csvFileWriter(listStringObj, TestBase.resultsDir, "BackDatedAttendance");

		} catch (Exception e) {
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	public void createLeavesImportForProvidedYear(String empID, String leaveType, String leaveMessage, String fromDate,
			String toDate) {
		try {
			String halfDay;
			LocalDate ToIterationDate = LocalDate.parse(toDate);
			LocalDate leavesFromDate = LocalDate.parse(fromDate);

			List<String[]> listStringObj = new ArrayList<>();
			String[] headers = new String[] { "Email/Employee ID", "Leave Name (leave type)", "Message (leave message)",
					"From Date (format like dd-mm-yyyy)", "To Date (format like dd-mm-yyyy)",
					"Is Halfday? (yes or no)" };
			listStringObj.add(headers);

			while (leavesFromDate.isAfter(ToIterationDate)) {
				leavesFromDate = leavesFromDate.minusDays(Long.valueOf(objGenHelper.getRandomNumber(45)));
				LocalDate leavesToDate = leavesFromDate.minusDays(-(Long.valueOf(objGenHelper.getRandomNumber(7))));
				String[] dataToWrite = new String[6];
				dataToWrite[0] = empID;
				dataToWrite[1] = leaveType;
				dataToWrite[2] = leaveMessage;
				dataToWrite[3] = leavesFromDate.toString();
				dataToWrite[4] = leavesToDate.toString();

				if (Integer.valueOf(objGenHelper.getRandomNumber(5)) == 1) {
					halfDay = "Yes";
				} else {
					halfDay = "No";
				}
				dataToWrite[5] = halfDay;

				listStringObj.add(dataToWrite);
			}
			ExcelWriter.csvFileWriter(listStringObj, TestBase.resultsDir, "LeavesImport");
		} catch (Exception e) {
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	public void createPresentDaysData(String empID, String fromDate, String toDate, String shiftName, String PolicyName,
			String WeeklyOff) {
		try {

			LocalDate shiftDate = LocalDate.parse(fromDate);
			LocalDate inTimeDate = shiftDate;
			String inTime = "09:00:00";
			LocalDate outTimeDate = shiftDate;
			String outTime = "18:00:00";
			List<String[]> listStringObj = new ArrayList<>();
			String[] headers = new String[] { "Email/Employee ID", "Shift Date", "Intime Date", "In Time",
					"Outtime Date", "Out Time", "Shift Name", "Policy Name", "Weekly Off Name" };
			listStringObj.add(headers);

			inTimeDate = shiftDate;
			outTimeDate = shiftDate;
			String[] dataToWrite = new String[9];
			dataToWrite[0] = empID;
			dataToWrite[1] = shiftDate.toString();
			if (shiftDate.getDayOfWeek().toString().equals("SUNDAY")
					|| shiftDate.getDayOfWeek().toString().equals("SATURDAY")) {
				dataToWrite[2] = "";
				dataToWrite[3] = "";
				dataToWrite[4] = "";
				dataToWrite[5] = "";
			} else {
				dataToWrite[2] = inTimeDate.toString();
				dataToWrite[3] = inTime;
				dataToWrite[4] = outTimeDate.toString();
				dataToWrite[5] = outTime;
			}
			dataToWrite[6] = shiftName;
			dataToWrite[7] = PolicyName;
			dataToWrite[8] = WeeklyOff;
			listStringObj.add(dataToWrite);

			ExcelWriter.csvFileWriter(listStringObj, TestBase.resultsDir, "BackDatedAttendance");

		} catch (Exception e) {
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	public void createOptionalHolidayDataImport(String empID, String fromDate, String toDate) {
		try {

			LocalDate startDate = LocalDate.parse(fromDate);
			LocalDate endDate = LocalDate.parse(toDate);
			LocalDate iterationDate = endDate;

			List<String[]> listStringObj = new ArrayList<>();
			String[] headers = new String[] { "Email/Employee ID", "Optional Holiday Date", "Approve Yes/No" };
			listStringObj.add(headers);

			while (iterationDate.isAfter(startDate)) {
				if (iterationDate.getDayOfWeek().toString().equals("THURSDAY")) {
					objHTTPSClient.createOptionalHolidays(iterationDate.toString());
					String[] dataToWrite = new String[3];
					dataToWrite[0] = empID;
					dataToWrite[1] = iterationDate.toString();
					dataToWrite[2] = "Yes";
					listStringObj.add(dataToWrite);
				}
				iterationDate = iterationDate.minusDays(1);
			}
			ExcelWriter.csvFileWriter(listStringObj, TestBase.resultsDir, "OptionalHolidayImport");

		} catch (Exception e) {
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

}
