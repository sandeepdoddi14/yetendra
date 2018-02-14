package com.darwinbox.test.hrms.uiautomation.Pages.reimb;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Reimbursement extends TestBase {

    private WebDriver driver;
    private GenericHelper helper;

    Reimbursement(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        helper = new GenericHelper(driver);
    }

    @FindBy (id = "expenses_dynamic_tabs")
    private WebElement filter;

    @FindBy( css = "a[href='/expenses/expenses/applyreim']")
    private WebElement lnkApply;

    @FindBy (id = "UserExpensesForm_title")
    private WebElement txtTitle;

    @FindBy(css = "a.chosen-single")
    private WebElement sldreimbType;

    @FindBy(id = "total_amt")
    private WebElement reimbAmt;

    @FindBy(id = "noOfDays")
    private WebElement days;

    @FindBy (name = "UserExpensesForm[itemName]")
    private WebElement desc;

    @FindBy( id = "UserExpensesForm_project_code")
    private WebElement prjCode;

    @FindBy( id = "UserExpensesForm_invoice_number")
    private WebElement invoiceCode;

    @FindBy( id = "uploadBtn")
    private WebElement addAttachment;

    @FindBy( xpath = "//input[@value='DISCARD']")
    private WebElement discard;

    @FindBy( xpath = "//input[@value='Submit']")
    private WebElement submit;

    @FindBy( linkText= "SAVE AS DRAFT")
    private WebElement saveDraft;

    public boolean clickAddReimbursement(){
        return helper.elementClick(lnkApply,"Apply Reimbursement");
    }

    public boolean saveAsDraft(){
        return helper.elementClick(saveDraft,"Save as Draft");
    }

    public boolean discardReimb(){
        return helper.elementClick(discard,"Discard ");
    }

    public boolean submitReimburesement(){
        return helper.elementClick(submit,"Submit Reimbursement");
    }

    public boolean fillReimbursement(){

        boolean status = true;
        try {

            helper.setElementText(txtTitle,"Reimbursemnet Title",getData("title"));
            helper.selectDropdown(sldreimbType,getData("Name"),"Reimbtype selection");


        }catch (Exception e){
            status = false;
        }
        return status;

    }

}
