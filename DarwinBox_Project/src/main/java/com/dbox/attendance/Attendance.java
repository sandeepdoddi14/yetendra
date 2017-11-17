package com.dbox.attendance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.dbox.commonPage.CommonPageObject;

public class Attendance extends CommonPageObject {

	WebDriver driver;

	public Attendance(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	@FindBy(xpath ="//a[@class = 'nav-setup-att']")
	private WebElement navMenuAttendance;
	
	
	
	
	public void abc() {

	}

}
