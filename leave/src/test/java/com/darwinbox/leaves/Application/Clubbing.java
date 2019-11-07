package com.darwinbox.leaves.Application;


import com.darwinbox.Services;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.ERROR_MESSAGES;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class Clubbing<Employee> extends LeaveBase {

    EmployeeServices employeeServices;
    LeaveService leaveService;
    LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;

    LeavesPage leavePage;
    LoginPage loginpage;
    CommonAction commonAction;

    com.darwinbox.attendance.objects.Employee employee;
    String employeeProbation = null;
    String[] leaves = new String[]{"randomLeaveType1", "randomLeaveTyp2"};
    Clubbings clubbing = null;
    String action = null;
    String mongoIdForCompOff="5cc278f8d61ea";
    String empIDForCompOff="compoff";
    Boolean CompOff=false;


    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

        leaveService = new LeaveService();

        mapUtils = new MapUtils();
        leaveAdmin = new LeaveAdmin();

        defaultBody = leaveService.getDefaultforLeaveDeduction();
        employeeProbation = UtilityHelper.getProperty("employeeConfig", "employeeProbation");

        leavePage = PageFactory.initElements(driver, LeavesPage.class);
        employeeServices = new EmployeeServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void clubbing(Map<String, String> data) {

        leavePolicyObject = new LeavePolicyObject();
        LeavePolicyObject present_LT = new LeavePolicyObject(data.get("Present_LT"));
        LeavePolicyObject after_LT = new LeavePolicyObject(data.get("After_LT"));
        LeavePolicyObject before_LT = new LeavePolicyObject(data.get("Before_LT"));

        //check if comp off
        if(present_LT.getLeave_Type().contains("Compensatory Off")
                || after_LT.getLeave_Type().contains("Compensatory Off")
                || before_LT.getLeave_Type().contains("Compensatory Off")){
            CompOff=true;
        }

        if(!CompOff)
        employee = employeeServices.generateAnEmployee("no", present_LT.getGroup_Company(), "random", "no");

        // dummy leave object for unapid/Compensatory Off
        if (!present_LT.getLeave_Type().equalsIgnoreCase("unpaid")
           && !present_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off") ) {
            Map<String, String> mandatoryFieldsForPresent = leaveService.mandatoryFieldsToCreateLeave(present_LT);
            leaveService.createLeaveForPolicy(mandatoryFieldsForPresent, present_LT);
        }
        if (!before_LT.getLeave_Type().equalsIgnoreCase("unpaid")
          && !before_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
            Map<String, String> mandatoryFieldsForBefore = leaveService.mandatoryFieldsToCreateLeave(before_LT);
            leaveService.createLeaveForPolicy(mandatoryFieldsForBefore, before_LT);
        }
        if (!after_LT.getLeave_Type().equalsIgnoreCase("unpaid" )
                && !after_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
            Map<String, String> mandatoryFieldsForAfter = leaveService.mandatoryFieldsToCreateLeave(after_LT);
            leaveService.createLeaveForPolicy(mandatoryFieldsForAfter, after_LT);

        }
        //CREATE/UPDATE random leves from clubbing with different
     /*   for (String randomLeave : leaves) {
            LeavePolicyObject random = new LeavePolicyObject(randomLeave);
            leaveService.createLeaveForPolicy(leaveService.mandatoryFieldsToCreateLeave(random), random);
        }*/

        leavePage.setFromAndToDatesWithoutProperty(3, LocalDate.now());

        if (present_LT.getLeave_Type() != after_LT.getLeave_Type() && present_LT.getLeave_Type() !=before_LT.getLeave_Type()) {
            for (Object conditionForPresentLT :
                    present_LT.getLeave_Type().equalsIgnoreCase("unpaid") || present_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off") ?  EnumSet.allOf(ClubbingsForUnpaidAndComp.class)  : EnumSet.allOf(Clubbings.class)) {
               if(!(present_LT.getLeave_Type().equalsIgnoreCase("unpaid")
                        || present_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")))
                {
                    updateClubbingLeaveTypeAccordingToScenario(conditionForPresentLT, present_LT);
                }
                if (!after_LT.getLeave_Type().equalsIgnoreCase("unpaid")
               && !after_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
                    for (Clubbings conditionForAfterLT : EnumSet.allOf(Clubbings.class)) {
                        updateClubbingLeaveTypeAccordingToScenario(conditionForAfterLT, after_LT);
                        clubbing = conditionForAfterLT;
                        action = "after";
                        verifyClubbing(present_LT, after_LT);
                    }
                }
                if (after_LT.getLeave_Type().equalsIgnoreCase("unpaid")
                 || after_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
                    clubbing = Clubbings.YESWITHANYLEAVE;
                    action = "after";
                    verifyClubbing(present_LT, after_LT);
                }
            }

            for (Object conditionForPresentLT :
                    present_LT.getLeave_Type().equalsIgnoreCase("unpaid") || present_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off") ?  EnumSet.allOf(ClubbingsForUnpaidAndComp.class)  : EnumSet.allOf(Clubbings.class)) {
                if(!(present_LT.getLeave_Type().equalsIgnoreCase("unpaid")
                        || present_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")))
                {
                    updateClubbingLeaveTypeAccordingToScenario(conditionForPresentLT, present_LT);
                }
                if (present_LT.getLeave_Type() != before_LT.getLeave_Type() &&
                        !before_LT.getLeave_Type().equalsIgnoreCase("unpaid")
                && !before_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
                    for (Clubbings conditionForBeforeLT : EnumSet.allOf(Clubbings.class)) {
                        updateClubbingLeaveTypeAccordingToScenario(conditionForBeforeLT, before_LT);
                        clubbing = conditionForBeforeLT;
                        action = "before";
                        verifyClubbing(present_LT, before_LT);
                    }
                }
                if (after_LT.getLeave_Type().equalsIgnoreCase("unpaid")
                        || after_LT.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
                    clubbing = Clubbings.YESWITHANYLEAVE;
                    action = "before";
                    verifyClubbing(present_LT, after_LT);
                }
            }
        } else {
            for (Clubbings condition : EnumSet.allOf(Clubbings.class)) {
                clubbing = condition;
                if (condition == Clubbings.NO) {
                    clubbing(false, false, false, "empty", present_LT);
                    verifyClubbing(present_LT, present_LT);
                }
                if (condition == Clubbings.YESWITHANYLEAVE) {
                    clubbing(true, true, false, "empty", present_LT);
                    verifyClubbing(present_LT, present_LT);
                }

                if (condition == Clubbings.YESWITHSAME) {
                    clubbing(true, false, true, "same", present_LT);
                    verifyClubbing(present_LT, present_LT);
                }
                if (condition == Clubbings.YESWITHDIFFERENT) {
                    clubbing(true, false, true, "diff", present_LT);
                    verifyClubbing(present_LT, present_LT);
                }
            }
        }
    }

    public void updateClubbingLeaveTypeAccordingToScenario(Object clubbing, LeavePolicyObject leaveType) {
        if (clubbing == Clubbings.NO) {
            clubbing(false, false, false, "empty", leaveType);
            //verifyClubbing(present_LT, present_LT);
        }
        if (clubbing == Clubbings.YESWITHANYLEAVE) {
            clubbing(true, true, false, "empty", leaveType);
            //verifyClubbing(present_LT, present_LT);
        }

        if (clubbing == Clubbings.YESWITHSAME) {
            clubbing(true, false, true, "same", leaveType);
            //verifyClubbing(present_LT, present_LT);
        }
        if (clubbing == Clubbings.YESWITHDIFFERENT) {
            clubbing(true, false, true, "diff", leaveType);
            // verifyClubbing(present_LT, present_LT);
        }
    }


    public void clubbing(boolean indicator, boolean anyLeave, boolean someLeave, String leaveList, LeavePolicyObject leavePolicyObject) {
        mandatoryFields = new LeaveService().mandatoryFieldsToCreateLeave(leavePolicyObject);
        List<NameValuePair> leavesList = new ArrayList<>();

        if (!indicator) {
            mandatoryFields.put("LeavePolicy_ClubbingOthers[status]", "0");
            leavePolicyObject.getClubbing().indicator = false;
            leavePolicyObject.getClubbing().clubWithAnyLeave=false;
            leavePolicyObject.getClubbing().clubWithFollowingLeave=false;
        } else {
            if (indicator) {
                mandatoryFields.put("LeavePolicy_ClubbingOthers[status]", "1");
                leavePolicyObject.getClubbing().indicator = true;
            }
            if (anyLeave) {
                mandatoryFields.put("LeavePolicy_ClubbingOthers[clubbing_type]", "0");
                leavePolicyObject.getClubbing().clubWithAnyLeave = true;
                leavePolicyObject.getClubbing().clubWithFollowingLeave = false;
            }
            if (someLeave) {
                mandatoryFields.put("LeavePolicy_ClubbingOthers[clubbing_type]", "1");
                leavePolicyObject.getClubbing().clubWithAnyLeave = false;
                leavePolicyObject.getClubbing().clubWithFollowingLeave = true;
            }
            if (someLeave & leaveList == "same") {
                leavesList.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[clubbing_leaves_list][]", leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId)));
                leavePolicyObject.getClubbing().leaveList = leavePolicyObject.getLeave_Type();
            }
            if (someLeave & leaveList == "diff") {
                for (String leave : leaves) {
                    String groupCompanyMongoId = new Services().getGroupCompanyIds().get("Working Days (DO NOT TOUCH)");
                    leavesList.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[clubbing_leaves_list][]", leaveService.getLeaveID(leave, leavePolicyObject.groupCompanyMongoId)));

                }
                leavePolicyObject.getClubbing().leaveList = leaves[0] + "," + leaves[1];
            }


        }


        defaultBody = new LeaveService().getDefaultforLeaveDeduction();
        defaultBody.putAll(mandatoryFields);
        if (someLeave) {


            List<NameValuePair> request = new LeaveService().mapToFormData(defaultBody);
            request.addAll(leavesList);
            new LeaveService().createLeaveForPolicy(request, leavePolicyObject);
        } else
            new LeaveService().createLeaveForPolicy(defaultBody, leavePolicyObject);

    }

    private void verifyClubbing(LeavePolicyObject LT1, LeavePolicyObject LT2) {
        String response1, response2 = null;
        int i = 0, j = 0;
        if (action.equalsIgnoreCase("before")) {
            i = 0;
            j = 1;
        }
        if (action.equalsIgnoreCase("after")) {
            i = 2;
            j = 1;
        }
        if (checkClubbingCompatability(LT1, LT2)) {

       if(!CompOff) {
             response1 = applyLeave(employee, LT1, leavePage.workingDays[j]);
             response2 = applyLeave(employee, LT2, leavePage.workingDays[i]);
         }
         else {
             response1 = applyLeave(mongoIdForCompOff, LT1, leavePage.workingDays[j]);
             response2 = applyLeave(mongoIdForCompOff, LT2, leavePage.workingDays[i]);
         }


           Reporter("Info -- Leave Response On " + leavePage.workingDays[j].toString() + response1, "Info");
            Reporter("Info -- Leave Response On " + leavePage.workingDays[i].toString() + response2, "Info");
            if (response1.contains("success") && response2.contains("success")) {
                Reporter("Pass -- Leave Clubbing is Allowed -- ", "Pass");
                if(!CompOff) {
                    new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    new LeaveAdmin().revokeLeave(employee, LT2.getLeave_Type(), leavePage.workingDays[i].toString());
                }
                else{
                    new LeaveAdmin().revokeLeave(empIDForCompOff, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    new LeaveAdmin().revokeLeave(empIDForCompOff, LT2.getLeave_Type(), leavePage.workingDays[i].toString());
                }


            } else {
                Reporter("Fail -- Clubbing is not Allowed --", "Fail");
                if(!CompOff)
                    new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                else
                    new LeaveAdmin().revokeLeave(empIDForCompOff, LT1.getLeave_Type(), leavePage.workingDays[j].toString());

            }
        } else {

            if(!CompOff)
                response1 = applyLeave(employee, LT1, leavePage.workingDays[j]);
            else
                response1 = applyLeave(mongoIdForCompOff, LT1, leavePage.workingDays[j]);

            if(!CompOff)
                response2 = applyLeave(employee, LT2, leavePage.workingDays[i]);
            else
                response2 = applyLeave(mongoIdForCompOff, LT2, leavePage.workingDays[i]);


            Reporter("Info -- Leave Response On " + leavePage.workingDays[j].toString() + response1, "Info");
            Reporter("Info -- Leave Response On " + leavePage.workingDays[i].toString() + response2, "Info");


            if (clubbing == Clubbings.NO) {
                if (response1.contains("success")
                        && response2.contains("failure")
                        && response2.contains(ERROR_MESSAGES.ClubbingNotAllowed)) {
                    Reporter("Pass --Error Message "+ERROR_MESSAGES.ClubbingNotAllowed  +" Verified Successfully   ", "Pass");
                    if(!CompOff)
                        new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    else
                        new LeaveAdmin().revokeLeave(empIDForCompOff, LT1.getLeave_Type(), leavePage.workingDays[j].toString());

                } else {
                    Reporter("Fail --Unable to Verify Error Message  "+ERROR_MESSAGES.ClubbingNotAllowed  , "Fail");
                    if(!CompOff)
                        new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    else
                        new LeaveAdmin().revokeLeave(empIDForCompOff, LT1.getLeave_Type(), leavePage.workingDays[j].toString());

                    if(!CompOff)
                        new LeaveAdmin().revokeLeave(employee, LT2.getLeave_Type(), leavePage.workingDays[i].toString());
                    else
                        new LeaveAdmin().revokeLeave(empIDForCompOff, LT2.getLeave_Type(), leavePage.workingDays[i].toString());

                    //new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    //new LeaveAdmin().revokeLeave(employee, LT2.getLeave_Type(), leavePage.workingDays[i].toString());
                }

            }
            if (clubbing == Clubbings.YESWITHDIFFERENT || clubbing == Clubbings.YESWITHSAME) {
                if (response1.contains("success")
                        && response2.contains("failure")
                        && response2.contains(ERROR_MESSAGES.ClubbingAllowedForSomeLeaveType)) {
                    Reporter("Pass -- Successfully Verified Error Message "+ERROR_MESSAGES.ClubbingAllowedForSomeLeaveType , "Pass");
                    if(!CompOff)
                        new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    else
                        new LeaveAdmin().revokeLeave(empIDForCompOff, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                   // new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());

                } else {
                    Reporter("Fail -- Unable To Verify Error Message  "+ ERROR_MESSAGES.ClubbingAllowedForSomeLeaveType , "Fail");
                    if(!CompOff)
                        new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                    else
                        new LeaveAdmin().revokeLeave(empIDForCompOff, LT1.getLeave_Type(), leavePage.workingDays[j].toString());

                    if(!CompOff)
                        new LeaveAdmin().revokeLeave(employee, LT2.getLeave_Type(), leavePage.workingDays[i].toString());
                    else
                        new LeaveAdmin().revokeLeave(empIDForCompOff, LT2.getLeave_Type(), leavePage.workingDays[i].toString());

                    //new LeaveAdmin().revokeLeave(employee, LT1.getLeave_Type(), leavePage.workingDays[j].toString());
                   // new LeaveAdmin().revokeLeave(employee, LT2.getLeave_Type(), leavePage.workingDays[i].toString());
                }
            }

        }
    }


}




