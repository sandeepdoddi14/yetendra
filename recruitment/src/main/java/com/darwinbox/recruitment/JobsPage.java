package com.darwinbox.recruitment;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class JobsPage extends TestBase {

    /*Below objects and methods are from edit job details page-1*/

    private GenericHelper genericHelper;
    private WebDriver driver;

    public JobsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        genericHelper = PageFactory.initElements(driver, GenericHelper.class);
    }

    @FindBy (xpath = "//*[@id=\"RaiseRequisition_designation_display_name\"]")
    WebElement displayNameOfJob;

    @FindBy (xpath = "//*[@id=\"is_refer_post\"]")
    WebElement isPostToRefer;

    @FindBy (xpath = "//*[@id=\"is_ijp_post\"]")
    WebElement isPostToIJP;

    @FindBy (xpath = "//div[@id=\"refer_assignment_framework_chosen\"]/ul/li/input")
    WebElement referFramework;

    @FindBy (xpath = "//div[@id=\"ijp_assignment_framework_chosen\"]/ul/li/input")
    WebElement IJPFramework;

    @FindBy (xpath = "//*[@id=\"RaiseRequisition_internal_job_code\"]")
    WebElement save;


    public boolean clickOnPostToRefer(){
        new WaitHelper(driver).waitElementToBeClickable(isPostToRefer);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", isPostToRefer);
     return  true; //genericHelper.toggleElementStatus(isPostToRefer,"Enable","Post to refer checkbox is selected");
    }
    public boolean clickOnPostToIJP(){
        new WaitHelper(driver).waitElementToBeClickable(isPostToIJP);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", isPostToIJP);
        return true; // genericHelper.toggleElementStatus(isPostToIJP,"Enable","Post to IJP checkbox is selected");
    }
    public boolean clickOnSave(){
          genericHelper.elementClick(save,"Save and Continue is clicked");
          save.sendKeys(Keys.ENTER);
          return true;
    }
    public boolean assignReferFramework(String referAssignment){
        referFramework.clear();
        return genericHelper.setElementText(referFramework,"Assignment is set into refer",referAssignment+Keys.ENTER);
    }
    public boolean assignIJPFramework(String IJPAssignment){
        IJPFramework.clear();
        return genericHelper.setElementText(IJPFramework,"Assignment is set into IJP",IJPAssignment+Keys.ENTER);
    }
    public boolean setDisplayNameForJob(String name){
        displayNameOfJob.clear();
        return genericHelper.setElementText(displayNameOfJob,"Display name is given",name);
    }

    /*Below method returns count of number of positions present
    *  under Add and Manage Position, in job edit page-1*/

    public int getNumberOfPositions(){

        List<WebElement> element = driver.findElements(By.xpath("//*[@id=\"position_table\"]/tbody/tr"));
        int numOfPositionsReflecting = element.size();

        return numOfPositionsReflecting;
    }


}
