package com.darwinbox.test.hrms.uiautomation.Pages.reimb;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReimbSettings extends TestBase {

    public static final Logger log = Logger.getLogger(ReimbSettings.class);
    private WebDriver driver;
    private GenericHelper helper;

    public ReimbSettings(WebDriver driver) {
        this.driver = driver;
        helper = new GenericHelper(driver);
        PageFactory.initElements(driver, this);

    }

    @FindBy(linkText = "Reimb Units")
    private WebElement lnkreimbUnits;

    @FindBy(id = "TenantReimbursementUnits_unit_name")
    private WebElement txtUnitName;

    @FindBy(id = "TenantReimbursementUnits_label")
    private WebElement txtUnitLabel;

    @FindBy(id = "TenantReimbursementUnits_unit_desc")
    private WebElement txtUnitDesc;

    @FindBy(id = "add_businessunit_btn")
    private WebElement btnCreateUnit;

    @FindBy(id = "create_businessunit_btn")
    private WebElement btnSaveUnit;

    @FindBy(id = "create_businessunit_btn")
    private WebElement btnCancelUnit;

    @FindBy(linkText = "Create Reimb Types")
    private WebElement lnkcreateReimb;

    @FindBy(linkText = "Manage Reimb Types")
    private WebElement lnkmanageReimb;

    @FindBy( css = "#business_unit_table_filter  label  input")
    private WebElement txtUnitSearch;

    @FindBy( css = " #business_unit_table td.sorting_1")
    private WebElement unitSearchResult;

    public boolean clickReimbUnits() {
        String name = "Reimbuserment Units";
        return helper.elementClick(lnkreimbUnits, name);
    }

    public boolean fillReimbTypesData(String unitType, String name, String desc) {
        try {
            helper.setElementText(txtUnitName, "Reimbursement Unit type", unitType);
            helper.setElementText(txtUnitLabel, "Reimbursement Unit label", name);
            helper.setElementText(txtUnitDesc, "Reimbursement Unit desc", desc);
            return true;
        } catch (Exception e) {
            Reporter("Exception while filling data for reimb unit", "Fail");
            e.printStackTrace();
            return false;
        }
    }

    public boolean createReimbTypes(String unitType, String name, String desc) {

        try {
            helper.elementClick(btnCreateUnit, "Create Reimbursement");

            if (!fillReimbTypesData(unitType, name, desc))
                return false;
            helper.elementClick(btnSaveUnit, "Save Reimbursement");
            return true;

        } catch (Exception e) {
            Reporter("Exception when creating reimb unit ", "Fail");
            e.printStackTrace();
            return false;
        }
    }

    public boolean searchReimbursement(String name){
        return helper.setElementText(txtUnitSearch,"Reimbursement search",name);
    }

    public boolean validateReimbResults(String name,int position){

        return true;


    }
}
