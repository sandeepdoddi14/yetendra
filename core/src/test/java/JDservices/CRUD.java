package JDservices;

import com.darwinbox.HelperServices;
import com.darwinbox.core.company.objects.Division;
import com.darwinbox.core.company.objects.JobDescription;
import com.darwinbox.core.services.BusinessUnitServices;
import com.darwinbox.core.services.CostCenterServices;
import com.darwinbox.core.services.DivisionServices;
import com.darwinbox.core.services.JobDescriptionServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CRUD extends TestBase {

    JobDescriptionServices jobDescriptionServices = new JobDescriptionServices();

    JobDescription jobDescription = null;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void crudJD(HashMap<String, String> testData) throws  Exception{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();


        jobDescription = new JobDescription();


        // String title=new String("testJD"+Math.random()).substring(0,10).replace(".","");

        jobDescription.setJobDescriptionTitle("testJobDescription");





    }
}


