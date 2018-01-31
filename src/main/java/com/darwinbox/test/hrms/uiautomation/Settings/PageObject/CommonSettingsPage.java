package com.darwinbox.test.hrms.uiautomation.Settings.PageObject;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class CommonSettingsPage {

	WaitHelper objWait ;
	GenericHelper objGenHelper;
	WebDriver driver;
	public static final Logger log = Logger.getLogger(CommonSettingsPage.class);	
	
	public CommonSettingsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
	}
	
	final String settingsMainMenuNavClass = "nav-setup-";

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "company)][")
	private WebElement settingsCompany;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "employee')]")
	private WebElement settingsEmployee;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "leaves')]")
	private WebElement settingsLeaves;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "att')]")
	private WebElement settingsAttendance;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "payroll')]")
	private WebElement settingsPayroll;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "permission')]")
	private WebElement settingsPermission;

	@FindBy(xpath = "//a[contains(text(),'IMPORT')]")
	private WebElement settingsImport;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "custom')]")
	private WebElement settingsCustomFields;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "hrfiles')]")
	private WebElement settingsHRLetters;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "vibe')]")
	private WebElement settingsVibe;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "reimb')]")
	private WebElement settingsReimb;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "pms')]")
	private WebElement settingsPerformance;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "recruitment')]")
	private WebElement settingsRecruitment;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "formgenerator')]")
	private WebElement settingsWorkFlow;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "onboarding')]")
	private WebElement settingsOnboarding;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "emailtemplate')]")
	private WebElement settingsEmailTemplate;

	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "emaildigest')]")
	private WebElement settingsEmailDigest;

	/**
	 * This method clicks on Company link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickCompany() {
		return objGenHelper.elementClick(settingsCompany, "COMPANY");
	}

	/**
	 * This method clicks on Employee link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickEmployee() {
		return objGenHelper.elementClick(settingsEmployee, "EMPLOYEE");
	}

	/**
	 * This method clicks on Leaves link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickLeaves() {
		return objGenHelper.elementClick(settingsLeaves, "LEAVES");
	}

	/**
	 * This method clicks on Attendance link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickAttendance() {
		return objGenHelper.elementClick(settingsAttendance, "ATTENDANCE");
	}

	/**
	 * This method clicks on Payroll link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickPayroll() {
		return objGenHelper.elementClick(settingsPayroll, "PAYROLL");
	}

	/**
	 * This method clicks on Permission link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickPermission() {
		return objGenHelper.elementClick(settingsPermission, "PERMISSION");
	}

	/**
	 * This method clicks on Import link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickImport() {
		return objGenHelper.elementClick(settingsImport, "IMPORT");
	}

	/**
	 * This method clicks on Custom Fields link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickCustomFields() {
		return objGenHelper.elementClick(settingsCustomFields, "CUSTOM FIELDS");
	}

	/**
	 * This method clicks on HRLetters link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickHRLetters() {
		return objGenHelper.elementClick(settingsHRLetters, "HRLetters");
	}

	/**
	 * This method clicks on Vibe link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickVibe() {
		return objGenHelper.elementClick(settingsVibe, "VIBE");
	}

	/**
	 * This method clicks on Reimb link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickReimb() {
		return objGenHelper.elementClick(settingsReimb, "REIMB");
	}

	/**
	 * This method clicks on Recruitment link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickRecruitment() {
		return objGenHelper.elementClick(settingsRecruitment, "REC");
	}

	/**
	 * This method clicks on WorkFlow link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickWorkFlow() {
		return objGenHelper.elementClick(settingsWorkFlow, "WORK FLOW");
	}

	/**
	 * This method clicks on Onboarding link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickOnboarding() {
		return objGenHelper.elementClick(settingsOnboarding, "ONBOARDING");
	}

	/**
	 * This method clicks on EmailTemplate link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickEmailTemplate() {
		return objGenHelper.elementClick(settingsEmailTemplate, "Email Template");
	}

	/**
	 * This method clicks on EmailDigest link
	 * 
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickEmailDigest() {
		return objGenHelper.elementClick(settingsEmailDigest, "Email Digest");
	}

}
