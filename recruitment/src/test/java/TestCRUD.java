import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.*;
import com.darwinbox.recruitment.objects.HiringWorkFlow.HiringWorkFlow;
import com.darwinbox.recruitment.services.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestCRUD extends TestBase {


    LoginPage loginPage;
    CandidateTags candidateTags;
    CandidateTagsService candidateTagsService;
    Recruiters recruiters;
    RecruitersService recruitersService;
    RecruitmentSettings recruitmentSettings;
    RecruitmentSettingsService recruitmentSettingsService;
    EvaluationForms evaluationForms;
    EvaluationFormsService evaluationFormsService;
    HiringWorkFlow hiringWorkFlow;
    HiringWorkFlowService hiringWorkFlowService;
    ArchivePosition archivePosition;
    ArchivePositionService archivePositionService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        candidateTags = new CandidateTags();
        candidateTagsService = new CandidateTagsService();
        recruiters = new Recruiters();
        recruitersService = new RecruitersService();
        recruitmentSettings = new RecruitmentSettings();
        recruitmentSettingsService = new RecruitmentSettingsService();
        evaluationForms = new EvaluationForms();
        evaluationFormsService = new EvaluationFormsService();
        hiringWorkFlow = new HiringWorkFlow();
        hiringWorkFlowService = new HiringWorkFlowService();
        archivePosition = new ArchivePosition();
        archivePositionService = new ArchivePositionService();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class,enabled = false)
    public void testTags(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");





        candidateTagsService.getAllCandidateTags();
        candidateTags.toObject(testData);
        candidateTagsService.createCandidateTags(candidateTags);

        candidateTags= candidateTagsService.getCandidateTagByName("delete check");
        candidateTags.toObject(testData);
        candidateTagsService.editCandidateTag(candidateTags);

        archivePosition.toObject(testData);
        archivePositionService.getArchivePositionIDByName("reason");

        evaluationForms = evaluationFormsService.getEvaluationFormByName("abc","2.0");
        evaluationForms.toObject(testData);
        evaluationFormsService.editEvaluationForm(evaluationForms);

        recruiters.toObject(testData);
        recruitersService.createCandidateTags(recruiters);

        hiringWorkFlow.toObject(testData);
        hiringWorkFlowService.createSettings(hiringWorkFlow);

    }
    }
