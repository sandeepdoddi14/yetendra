package com.dbox.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.dbox.commonPage.CommonPageObject;

public class Settings extends CommonPageObject{

	WebDriver driver;
	public Settings(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
		this.driver = driver;
		// This initElements method will create all WebElements
		PageFactory.initElements(driver, this);
	}

	final String settingsMainMenuNavClass = "nav-setup-";
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "company)][")
	private WebElement settingsCompany;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "employee')]")
	private WebElement settingsEmployee;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "leaves')]")
	private WebElement settingsLeaves;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "att')]")
	private WebElement settingsAttendance;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "payroll')]")
	private WebElement settingsPayroll;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "permission')]")
	private WebElement settingsPermission;
	
	@FindBy(xpath = "//a[contains(text(),'IMPORT')]")
	private WebElement settingsImport;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "custom')]")
	private WebElement settingsCustomFields;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "hrfiles')]")
	private WebElement settingsHRLetters;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "vibe')]")
	private WebElement settingsVibe;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "reimb')]")
	private WebElement settingsReimb;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "pms')]")
	private WebElement settingsPerformance;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "recruitment')]")
	private WebElement settingsRecruitment;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "formgenerator')]")
	private WebElement settingsWorkFlow;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "onboarding')]")
	private WebElement settingsOnboarding;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "emailtemplate')]")
	private WebElement settingsEmailTemplate;
	
	@FindBy(xpath = "//a[contains(@class,'"+ settingsMainMenuNavClass+ "emaildigest')]")
	private WebElement settingsEmailDigest;


	/**
	 * This method clicks on Company link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickCompany() {
		return click(settingsCompany,"COMPANY");
	}
	
	/**
	 * This method clicks on Employee link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickEmployee() {
		return click(settingsEmployee,"EMPLOYEE");
	}
	
	/**
	 * This method clicks on Leaves link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickLeaves() {
		return click(settingsLeaves,"LEAVES");
	}
	
	/**
	 * This method clicks on Attendance link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickAttendance() {
		return click(settingsAttendance,"ATTENDANCE");
	}
	
	/**
	 * This method clicks on Payroll link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickPayroll() {
		return click(settingsPayroll,"PAYROLL");
	}
	
	/**
	 * This method clicks on Permission link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickPermission() {
		return click(settingsPermission,"PERMISSION");
	}
	
	/**
	 * This method clicks on Import link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickImport() {
		return click(settingsImport,"IMPORT");
	}
	
	/**
	 * This method clicks on Custom Fields link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickCustomFields() {
		return click(settingsCustomFields,"CUSTOM FIELDS");
	}
	
	/**
	 * This method clicks on HRLetters link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickHRLetters() {
		return click(settingsHRLetters,"HRLetters");
	}
	
	/**
	 * This method clicks on Vibe link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickVibe() {
		return click(settingsVibe,"VIBE");
	}
	
	/**
	 * This method clicks on Reimb link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickReimb() {
		return click(settingsReimb,"REIMB");
	}
	
	/**
	 * This method clicks on Recruitment link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickRecruitment() {
		return click(settingsRecruitment,"REC");
	}
	
	/**
	 * This method clicks on WorkFlow link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickWorkFlow() {
		return click(settingsWorkFlow,"WORK FLOW");
	}
	
	/**
	 * This method clicks on Onboarding link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickOnboarding() {
		return click(settingsOnboarding,"ONBOARDING");
	}
	
	/**
	 * This method clicks on EmailTemplate link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickEmailTemplate() {
		return click(settingsEmailTemplate,"Email Template");
	}
	
	/**
	 * This method clicks on EmailDigest link
	 * @author shikhar
	 * @return boolean
	 */
	public boolean clickEmailDigest() {
		return click(settingsEmailDigest,"Email Digest");
	}
	
	
	
	
}
