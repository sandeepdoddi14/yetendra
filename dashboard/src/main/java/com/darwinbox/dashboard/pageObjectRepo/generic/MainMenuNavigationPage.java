/**
 * 
 */
package com.darwinbox.dashboard.pageObjectRepo.generic;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author balaji
 * @Creation_Date:  2 Dec 2017 
 * @ClassName: MainMenuNavigationPage.java
 * @LastModified_Date:  2 Dec 2017 
 */
public class MainMenuNavigationPage extends TestBase{

	private static final Logger log = Logger.getLogger(MainMenuNavigationPage.class);
	WebDriver driver;
	GenericHelper objGenHelper;
	WaitHelper objWaitHelper;
	
	public MainMenuNavigationPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		
	}
	
	@FindBy(xpath = "//a[contains(@class,'dashboard nav-dashboard')]")
	protected WebElement navMenuDashboard;

	@FindBy(xpath = "//a[contains(@class,'employees nav-employee')]")
	protected WebElement navMenuEmployee;

	@FindBy(xpath = "//a[contains(@class,'payroll nav-payroll')]")
	protected WebElement navMenuPayroll;

	@FindBy(xpath = "//a[contains(@class,'reports nav-reports')]")
	protected WebElement navMenuReports;

	@FindBy(xpath = "//a[contains(@class,'pms nav-pms')]")
	protected WebElement navMenuPerformance;

	@FindBy(xpath = "//a[contains(@class,'hr-documents nav-hrdocs')]")
	protected WebElement navMenuHRDocs;

	/**
	 * This method used to objGenHelper.elementClick Dashboard Icon
	 */
	public boolean clickNavMenuDashboard() {
		return objGenHelper.elementClick(navMenuDashboard, "Dashboard icon");
	}

	/**
	 * This method used to objGenHelper.elementClick Employee Icon
	 */
	public boolean clickNavMenuEmployee() {
		return objGenHelper.elementClick(navMenuEmployee, "Employee icon");
	}

	/**
	 * This method used to objGenHelper.elementClick Payroll Icon
	 */
	public boolean clickNavMenuPayroll() {
		return objGenHelper.elementClick(navMenuPayroll, "Payroll icon");
	}

	/**
	 * This method used to objGenHelper.elementClick Reports Icon
	 */
	public boolean clickNavMenuReports() {
		return objGenHelper.elementClick(navMenuReports, "Reports icon");
	}

	/**
	 * This method used to objGenHelper.elementClick HR Documents Icon
	 */
	public boolean clickNavMenuHRDocs() {
		return objGenHelper.elementClick(navMenuHRDocs, "HR Documents icon");
	}

	/**
	 * This method used to objGenHelper.elementClick Performance Icon
	 */
	public boolean clickNavMenuPerformance() {
		return objGenHelper.elementClick(navMenuPerformance, "Performance icon");
	}

}
