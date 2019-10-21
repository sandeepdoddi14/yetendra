package BusinessUnit;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.core.employee.objects.BusinessUnit;
import com.darwinbox.core.employee.objects.Grade;
import com.darwinbox.core.services.BandServices;
import com.darwinbox.core.services.BusinessUnitServices;
import com.darwinbox.core.services.CostCenterServices;
import com.darwinbox.core.services.GradeServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.darwinbox.framework.uiautomation.base.TestBase.ms;

public class create extends TestBase {

    BusinessUnitServices businessUnitServices = new BusinessUnitServices();

    BusinessUnit businessUnit = null;

    String groupCompany=null;
    String costCenter=null;
    List<Employee> headOfBusinessUnits=new ArrayList<>();

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void createBusinessUnit(HashMap<String, String> testData) throws Exception {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();


        businessUnit = new BusinessUnit();
        businessUnit.setBusinessUnitName(testData.get("BusinessUnitName"));
        businessUnit.setBusinessUnitAddress(testData.get("BusinessUnitAddress"));

        if (testData.get("GroupCompany") == "") {
           groupCompany=data.get("@@url").replaceAll(".qa.darwinbox.io","").replaceAll("https://","");
           businessUnit.setGroupCompany(groupCompany);
        }

        if(testData.get("CostCenter")==""){
            costCenter=new CostCenterServices().getCostCenters().keySet().toArray()[0].toString();
            businessUnit.setCostCenter(costCenter);
        }

        if(testData.get("HeadOfBusinessUnit")==""){

            headOfBusinessUnits.add(new EmployeeServices().generateAnEmployee("yes", "main", "random", "no"));

            businessUnit.setHeadOFBusniessUnits(headOfBusinessUnits);
        }

        businessUnitServices.createBusinessUnit(businessUnit);


        HashMap<String, String> businessUnits = businessUnitServices.getBusinessUnits();


        Assert.assertTrue(businessUnits.get(businessUnit.getBusinessUnitName()) != null, "Business Unit Not Created Successfully");




    }
}