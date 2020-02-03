package com.darwinbox.recruitment;

import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.recruitment.services.DesignationsService;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DesignationsPage extends TestBase {

    private GenericHelper genericHelper;
    private DateTimeHelper dateTimeHelper;
    private WebDriver driver;

    public DesignationsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        genericHelper = PageFactory.initElements(driver, GenericHelper.class);
        dateTimeHelper = PageFactory.initElements(driver,DateTimeHelper.class);
    }
//page1
    @FindBy(xpath = "//a[@id='add_designation_btn']")
    WebElement clickCreate;
    @FindBy(xpath = "//select[@id='dept_grp_company']")
    WebElement groupCompany;
    @FindBy(xpath = "//div[@class='controls']//input[@class='search']")
    WebElement designationID;
    @FindBy(xpath = "//div[@id='parent_dept_load_chosen']//input")
    WebElement department;
    @FindBy(xpath = "//input[@id='UserDesignationsForm_allowed_positions']")
    WebElement numOfPositions;
    @FindBy(xpath = "a//input[@id='UserDesignationsForm_allowed_positions']")
    WebElement staffingModel;
    @FindBy(xpath = "//button[@id='create_position']")
    WebElement createDesignation;

    public DesignationNames createDesignation(Map<String, String> body) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS) ;

        DesignationNames designationNames=new DesignationNames();
        DesignationNamesServices designationNamesServices=new DesignationNamesServices();
        DesignationsService designationsService=new DesignationsService();
        String designationName = designationNames.toObject();
        designationNames.toObject();
        designationNamesServices.createDesignationName(designationNames);
        Reporter("Designation Name created is : "+designationName,"INFO");
        designationNames=  designationNamesServices.getDesignationNamesID(designationName);

        driver.navigate().refresh();

        Thread.sleep(2000);
        genericHelper.elementClick(clickCreate,"Add designation is clicked");
        Thread.sleep(2000);
        genericHelper.setElementText(designationID,"",designationName);
        Thread.sleep(2000);
        designationID.sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//*[@id='manage-designation-form']/div[5]/div[1]/label")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='businessunit_data']/div")).click();
        for (WebElement e:
                driver.findElements(By.xpath("//*[@id='businessunit_data']/div/div[2]/div"))) {
            if(e.getText().toLowerCase().contains(body.get("groupCompany").toLowerCase()))
            {
                e.click();
                Reporter("Group company selected is ::"+e.getText(),"");
                // genericHelper.selectDropdown(groupCompany,body.get("groupCompany"),"");
            }
        }
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[contains(text(),'-Select Department-')]")).click();
        genericHelper.setElementText(department,"",body.get("departmentName"));
        department.sendKeys(Keys.ENTER);
        genericHelper.setElementText(numOfPositions,"",body.get("numberOfPositions"));

        //div[contains(text(),'Position Based')]
        driver.findElement(By.xpath("//div[@class='text'][contains(text(),'Select Here')]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[contains(text(),'Position Based')]")).click();
       // genericHelper.selectDropdown(staffingModel,body.get("staffingModel"),"");
        genericHelper.elementClick(createDesignation,"");
return designationNames;
    }
//page2
    @FindBy(xpath = "//input[@name='UserDesignationsPositionForm[no_of_positions]']")
    WebElement numOfPositionsStage1;
    @FindBy(xpath = "//*[@name='UserDesignationsPositionForm[effective_date]']")
    WebElement date;
    @FindBy(xpath = "//button[@id='create_position_step1_next']")
    WebElement saveStage1;
    public void selectdate(WebDriver driver, WebElement date, String Date) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("arguments[0].setAttribute('value','" + Date + "');", date);
    }
    public void createDesignationStage1(Map<String, String> body){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS) ;
        genericHelper.setElementText(numOfPositionsStage1,"numberOfPositions",body.get("numberOfPositions"));

        Date date1 = new Date();
        date1=dateTimeHelper.addDays(date1,-7);
        String effectiveDate = dateTimeHelper.formatDateTo(date1,"dd-MM-yyyy");

        selectdate(driver,date,effectiveDate);
        genericHelper.elementClick(saveStage1,"");

    }
 //page3
    @FindBy(xpath = "//input[@id='PositionsSet_0_position_id']")
    WebElement positionOne;
    @FindBy(xpath = "//input[@id='PositionsSet_1_position_id']")
    WebElement positionTwo;
    @FindBy(xpath = "//input[@id='PositionsSet_2_position_id']")
    WebElement PositionThree;
    @FindBy(xpath = "//input[@id='PositionsSet_3_position_id']")
    WebElement PositionFour;
    @FindBy(xpath = "//select[@id='PositionsSet_3_need_to_hire']")
    WebElement needToHirePositionFour;
    @FindBy(xpath = "//button[@id='save_position']")
    WebElement saveStage2;

    public void createDesignationStage2(Map<String, String> body){
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS) ;
        genericHelper.setElementText(positionOne,"",body.get("position1"));
        genericHelper.setElementText(positionTwo,"",body.get("position2"));
        genericHelper.setElementText(PositionThree,"",body.get("position3"));
        genericHelper.setElementText(PositionFour,"",body.get("position4"));
        genericHelper.selectDropdown(needToHirePositionFour,body.get("needToHireP4"),"");
        genericHelper.elementClick(saveStage2,"");

    }

}
