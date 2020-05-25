package com.testvagrant.goodreads.pageObjects;

import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignInModal extends TestBase {
    @FindBy(xpath = "/html/body/div[3]/div/div/div[1]")
    public WebElement modal;
    @FindBy(xpath = "/html/body/div[3]/div/div/div[1]")
    public WebElement closeButton;
    GenericHelper genericHelper;

    public Boolean isModalDisplayed() {
        return genericHelper.isDisplayed(modal, "SignIn Modal");
    }

    public Boolean clickOnCloseButton() {
        return genericHelper.elementClick(closeButton, "Sign In Modal Close button");
    }

    public void closeIfModalIsPresent() {
        if (isModalDisplayed())
            clickOnCloseButton();
    }

}






