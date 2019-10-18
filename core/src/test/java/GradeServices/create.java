package GradeServices;

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
import java.util.Set;

public class create extends TestBase {

    GradeServices gradeServices = new GradeServices();

    Grade grade = null;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void createGrade(HashMap<String, String> testData) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();


        grade = new Grade();
        grade.setGradeName(testData.get("GradeName"));
        grade.setDescription(testData.get("Description"));

        String bandName=new BandServices().getBands().keySet().toArray()[0].toString();
        grade.setBandName(bandName);

        gradeServices.createGrade(grade);


        HashMap<String, String> grades = gradeServices.getGrades();


        Assert.assertTrue(grades.get(grade.getGradeName()) != null, "Grade Not Created Successfully");


    }
}
