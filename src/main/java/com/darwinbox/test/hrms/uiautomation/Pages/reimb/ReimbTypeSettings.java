package com.darwinbox.test.hrms.uiautomation.Pages.reimb;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class ReimbTypeSettings extends TestBase {

    public static final Logger log = Logger.getLogger(ReimbTypeSettings.class);
    private WebDriver driver;
    private GenericHelper helper;

    public ReimbTypeSettings(WebDriver driver) {
        this.driver = driver;
        helper = new GenericHelper(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Create Reimb Types")
    private WebElement lnkcreateReimb;

    @FindBy(linkText = "Manage Reimb Types")
    private WebElement lnkmanageReimb;

    @FindBy(id = "TenantReimbursement_name")
    private WebElement txtReimbName;

    @FindBy(id = "TenantReimbursement_description")
    private WebElement txtReimbDesc;

    @FindBy(id = "dept_grp_company")
    private WebElement sldCompany;

    @FindBy(name = "TenantReimbursement[units]")
    private WebElement sldUnit;

    @FindBy(xpath = "//li[@class='search-field']/input")
    private WebElement txtApplicableTo;

    @FindBy(id = "TenantReimbursement_approval_flow")
    private WebElement sldApprovalFlow;

    @FindBy(id = "update_reimb_btn")
    private WebElement btnSaveReimb;

    @FindBy(name = "TenantReimbursement[ledger]")
    private WebElement sldLedger;

    @FindBy (id = "TenantReimbursement_can_exceed")
    private WebElement rdCanExceed;

    @FindBy (id = "TenantReimbursement_project_code")
    private WebElement rdprjCode;

    @FindBy(id = "TenantReimbursement_invoice_number")
    private WebElement rdInvoicenum;

    @FindBy(id = "TenantReimbursement_attachment_compulsory")
    private WebElement rdAttachment;

    @FindBy( id = "Reimb_set_rupees")
    private WebElement txtuppercaplimit;

    @FindBy( id = "Reimb_set_upper_cap_unit")
    private WebElement txtuppercapUnit;

    @FindBy( id = "Reimb_set_auto_cal_and_non_editable")
    private WebElement sldAutoCalculate;

    public boolean clickcreateReimb(){
       helper.elementClick(lnkcreateReimb, "Create Reimb Types");
       new WaitHelper(driver).waitForElement(txtReimbName,5);
       return true;
    }

    public void saveReimb(){
        btnSaveReimb.submit();
    }

    public boolean createReimbType(){
       try {
           clickcreateReimb();
           fillReimbursementType();
           saveReimb();
           return true;
       }catch (Exception e){
           return false;
       }
    }

    public boolean fillReimbursementType(){

        try{

            helper.setElementText(txtReimbName,getData("Name"),"Reimbursement Name");
            helper.setElementText(txtReimbDesc,getData("Description"),"Reimbursement Desc");

            if (getData("Company").length() != 0){
                helper.selectDropdown(sldCompany,getData("Company"),"Company");
            }

            if(getData("Applicable To").length() != 0) {

                String ar[] = getData("Applicable To").split(",");
                if (ar.length != 0) {

                    for (String s : ar) {
                        helper.setElementTextinSelection(txtApplicableTo, "Data to be applicable",s,  false);
                        txtApplicableTo.sendKeys(Keys.ENTER);
                    }
                }
            }

            helper.selectDropdown(sldUnit,getData("Unit"),"Unit");

            if (getData("Approval Flow").length() != 0){
                helper.selectDropdown(sldApprovalFlow,getData("Approval Flow"),"Company");
            }

            if (getData("Ledger").equalsIgnoreCase("Yes")){
                helper.selectDropdown(sldLedger,getData("Ledger"),"Ledger");
            }

            helper.toggleElement(rdCanExceed,getData("Exceed").equalsIgnoreCase("Yes"),"Exceed Reimbursement limits");

            helper.toggleElement(rdprjCode,getData("Project Code").equalsIgnoreCase("Yes"),"Project code");

            helper.toggleElement(rdInvoicenum,getData("Invoice").equalsIgnoreCase("Yes"),"Invoice Num");

            helper.toggleElement(rdAttachment,getData("Attachments").equalsIgnoreCase("Yes"),"Attachment");

            String rlims = getData("Limits");
            if(rlims.length() == 0) {
                rlims="All Roles,All Office Locations,1000,1000,NO";
            }

            fillReimbursementLimts(rlims.split("/n"),0);

            return true;
        }catch(Exception e){
    e.printStackTrace();

            return false;

        }


    }

    private void fillReimbursementLimts(String[] rlims,int counter) {

        for(String rlim : rlims){

            driver.findElement(By.id("add_more_fields")).click();
            String data[] = rlim.split(",");

            String role= " "+data[0];
            String location = " "+data[1];
            String upperlimit = data[2];
            String uppercap = data[3];
            String autoCalc = data[4];

            String desg = "#Reimb_set_designation_0_"+counter+"_undefined_chosen input";
            if (counter == 0)  desg = "#Reimb_set_designation_0_chosen input";

            helper.setElementTextinSelection(driver.findElement(By.cssSelector(desg)),"Role Limit",role,false);
            driver.findElement(By.cssSelector(desg)).sendKeys(Keys.ENTER);

            String loc = "#Reimb_set_location_0_"+counter+"_undefined_chosen input";
            if (counter == 0)  loc = "#Reimb_set_location_0_chosen input";

            helper.setElementTextinSelection(driver.findElement(By.cssSelector(loc)),"Location Limit",location,false);
            driver.findElement(By.cssSelector(loc)).sendKeys(Keys.ENTER);

            String perunit = "#field_"+counter+" #Reimb_set_rupees";
            String upperCap = "#field_"+counter+" #Reimb_set_upper_cap_unit";
            String autocal = "#field_"+counter+" #Reimb_set_auto_cal_and_non_editable";

            helper.setElementTextinSelection(driver.findElement(By.cssSelector(perunit)),"Per Unit Limit",upperlimit,false);
            helper.setElementTextinSelection(driver.findElement(By.cssSelector(upperCap)),"Max Unit Limit",uppercap,false);

            new Select(driver.findElement(By.cssSelector(autocal))).selectByVisibleText(autoCalc);

            counter++;
    }

    WebElement delete = driver.findElement(By.id("remove_field_"+counter));
    new Actions(driver).moveToElement(delete).perform();
    delete.click();

    }

    public boolean createReABoolean(){
        return helper.elementClick(lnkmanageReimb,"Create Reimbursements");
    }
}
