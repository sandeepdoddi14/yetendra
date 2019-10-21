package GradeServices;

import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.employee.objects.Grade;
import com.darwinbox.core.services.BandServices;
import com.darwinbox.core.services.GradeServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

public class update extends TestBase {

    GradeServices gradeServices = new GradeServices();
    Grade grade = null;


    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void updateBand(HashMap<String, String> testData) throws InterruptedException{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();

        grade = new Grade();
        grade.setGradeName("updatedGrade");
        grade.setDescription("Automation Created Description");

        Thread.sleep(200);
        String bandName=new BandServices().getBands().keySet().toArray()[0].toString();
        grade.setBandName(bandName);

        gradeServices.createGrade(grade);



        grade.setId(new GradeServices().getGrades().get(grade.getGradeName()));


        grade.setGradeName(testData.get("GradeName"));
        grade.setDescription(testData.get("Description"));

        gradeServices.updateGrade(grade);


        HashMap<String, String> grades = gradeServices.getGrades();

        Assert.assertTrue(gradeServices.getGrades().get(grade.getGradeName()) != null, "Grade Not Updated Successfully");


    }
}
