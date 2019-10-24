package com.darwinbox.leaves.actionClasses;


import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.Arrays;


public class LeaveApplicationHelperBackupObject extends LeavePolicyObject {

    //service objects
    LeaveService leaveService = new LeaveService();
    LeaveAdmin leaveAdmin = new LeaveAdmin();

    //driver object
    DateTimeHelper dateTimeHelper = new DateTimeHelper();
    LeavesPage leavePage = PageFactory.initElements(TestBase.driver, LeavesPage.class);

    LocalDate[] restrictDays;
    String userMongoId = UtilityHelper.getProperty("config", "Employee.mongoId");


    /**
     * validates MaxAllowedPerYearSetting
     *
     * @return
     */
    public boolean verifyMaxAllowedperYearSetting() {
        for (int i = -1; i <= 1; i++) {
            LocalDate serverChangedDate = dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);

            leavePage.setFromAndToDatesWithoutProperty(getMaximum_leave_allowed_per_year() + i, serverChangedDate);

            LocalDate fromDate = leavePage.workingDays[0];
            LocalDate toDate = leavePage.workingDays[leavePage.workingDays.length - 1];
            String leaveID = new LeaveService().getLeaveID(getLeave_Type(), groupCompanyMongoId);

            Reporter("Leave Applying From " + leavePage.workingDays[0].toString() + "; to " + leavePage.workingDays[leavePage.workingDays.length - 1] + "; in Single Application", "Info");
            String response = leaveAdmin.ApplyLeave(userMongoId, fromDate.toString(), toDate.toString(), leaveID);
            if (i != 1) {
                if (response.contains("success") && response.contains("Your Leave has been applied successfully"))//succecss
                {
                    Reporter("Leave Applied Successfully From Date=" + leavePage.workingDays[0].toString() + "to Date = " + leavePage.workingDays[leavePage.workingDays.length - 1] +
                            "Leave Setting is Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + " Leave Applied for" + getMaximum_leave_allowed_per_year() + i, "Pass");
                    leaveAdmin.revokeLeave(getLeave_Type(), leavePage.workingDays[0].toString());
                } else
                    Reporter("User Cannot Apply Leave From Date=" + leavePage.workingDays[0].toString() + "to Date = " + leavePage.workingDays[leavePage.workingDays.length - 1] +
                            "Leave Setting is Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + " Leave Applied for" + getMaximum_leave_allowed_per_year() + i, "Fail");
            } else if (i == 1) {
                if (response.contains("You have insufficient leave balance, and over-utilizing this leave is not allowed"))//error
                    Reporter("User Cannot Apply Leave From Date=" + leavePage.workingDays[0].toString() + "to Date = " + leavePage.workingDays[leavePage.workingDays.length - 1] +
                            "Leave Setting is Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + " Leave Applied for" + getMaximum_leave_allowed_per_year() + i, "Pass");
                else
                    Reporter("User Cannot Apply Leave From Date=" + leavePage.workingDays[0].toString() + "to Date = " + leavePage.workingDays[leavePage.workingDays.length - 1] +
                            "Leave Setting is Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + " Leave Applied for" + getMaximum_leave_allowed_per_year() + i, "Fail");
            }
        }
        return true;
    }

    /*
    validates ConsecutiveLeaveAllowed,MaxleaveAllowedPerMonth Combination
     */
    public boolean verifyConsequtiveLeaveAllowedAndMaxLeaveAllowedPerMonth() {

        LocalDate serverChangedDate = dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);
        leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed(), serverChangedDate);


        String singleApplyResponse = singleApply(leavePage.workingDays);

        //single application for all consecutive days
        Reporter("Applying Leave For " + getConsecutive_leave_allowed() + "in a single Application", "INFO");
        if (singleApplyResponse.contains("Your Leave has been applied successfully")) {
            Reporter("Leave Applied From" + leavePage.workingDays[0].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Pass");
        } else if (singleApplyResponse.contains("failure")) {
            Reporter("Unable to apply leave From" + leavePage.workingDays[0].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Fail");

        }


        new LeaveAdmin().revokeLeave(getLeave_Type(), leavePage.workingDays[0].toString());

        //check for additonal leave
        leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed() + 1, serverChangedDate);
        String failedResponse = singleApply(leavePage.workingDays);
        verifyLeaveResponse(failedResponse, "fail");
        if (getMaximum_leave_allowed_per_month()==getConsecutive_leave_allowed())
            Assert.assertTrue(failedResponse.contains("Over-utilizing this leave is not allowed.As you have utilized max per month leaves for this month you cannot apply leave for this month"), "Error Message is not as expected");

        else
            Assert.assertTrue(failedResponse.contains("Over-utilizing this leave is not allowed as max consecutive days limit exceeded."), "Error Message is not as expected");


        // 5 1 5 condition
        if (getMaximum_leave_allowed_per_month() > getConsecutive_leave_allowed()) {
            Reporter("Applying Leave For " + getConsecutive_leave_allowed() + "in a single Application", "INFO");
            leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed(), serverChangedDate);
            String response1 = singleApply(leavePage.workingDays);
            verifyLeaveResponse(response1, "pass");

            leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed(), serverChangedDate.plusDays(getConsecutive_leave_allowed() + 1));

            Reporter("Applying Leave For " + getConsecutive_leave_allowed() + "in a single Application", "INFO");
            String response2 = singleApply(leavePage.workingDays);
            if (response2.contains("Your Leave has been applied successfully")) {
                Reporter("Leave Applied From" + leavePage.workingDays[0].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Pass");
            } else if (response2.contains("failure")) {
                Reporter("Leave Applied From" + leavePage.workingDays[0].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Fail");
            }
            //revoke leaves
            leaveAdmin.revokeLeave(getLeave_Type(), leavePage.workingDays[0].toString());
            leaveAdmin.revokeLeave(getLeave_Type(), leavePage.workingDays[0].minusDays(getConsecutive_leave_allowed()).toString());
        }

     /*   //multiple apply
        Reporter("Applying Leave For " + getConsecutive_leave_allowed() + "in a Multiple Applications", "INFO");
        leavePage.setFromAndToDatesWithoutProperty(Integer.parseInt(getConsecutive_leave_allowed().replace(".0", "")), serverChangedDate);
        for (int i = 0; i < leavePage.workingDays.length; i++) {
            String multipleApplyResponse = singleApply(leavePage.workingDays[i]);
            if (multipleApplyResponse.contains("Your Leave has been applied successfully")) {
                Reporter("Leave Applied From" + leavePage.workingDays[i].toString() + "toDate" + leavePage.workingDays[i] + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Pass");
            } else if (multipleApplyResponse.contains("failure")) {
                Reporter("Unable To Apply Leave From" + leavePage.workingDays[i].toString() + "toDate" + leavePage.workingDays[i] + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Fail");

            }
        }*/

      /*  ////apply for additional leaves
        Reporter("Applying More Than getConsecutive_leave_allowed()=" + getConsecutive_leave_allowed() + "APPROVED Leaves till are from" + leavePage.workingDays[0].toString() + "to" + leavePage.workingDays[leavePage.workingDays.length - 1].toString(), "INFO");

        leavePage.setFromAndToDatesWithoutProperty(Integer.parseInt(getConsecutive_leave_allowed().replace(".0", "")) + 1, serverChangedDate);
        String failedResponse1 = singleApply(leavePage.workingDays[leavePage.workingDays.length - 1]);
        if (failedResponse.contains("failure") && failedResponse.contains("Over-utilizing this leave is not allowed.As you have utilized max per month leaves for this month you cannot apply leave for this month")) {
            Reporter("Unable to apply leave  From" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "User Has APPROVED Leaves=" + getConsecutive_leave_allowed() + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month() + "Response is" + failedResponse, "Pass");
        } else if (failedResponse.contains("success")) {
            Reporter("Leave Applied From" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "User Has APPROVED Leaves=" + getConsecutive_leave_allowed() + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month() + "Response is" + failedResponse, "Fail");
        }

        //revoke leaves
        for (int i = 0; i < leavePage.workingDays.length - 1; i++)
            leaveAdmin.revokeLeave(getLeave_Type(), leavePage.workingDays[i].toString());

        // 5 1 5 condition
        if (Integer.parseInt(getMaximum_leave_allowed_per_month().replace(".0", "")) > Integer.parseInt(getConsecutive_leave_allowed().replace(".0", ""))) {
            leavePage.setFromAndToDatesWithoutProperty(Integer.parseInt(getConsecutive_leave_allowed().replace(".0", "")), serverChangedDate.plusDays(Integer.parseInt(getConsecutive_leave_allowed()) + 1));

            Reporter("Applying Leave For " + getConsecutive_leave_allowed() + "in a Multiple Applications", "INFO");

            for (int i = 0; i < leavePage.workingDays.length; i++) {
                String multipleApplyResponse = singleApply(leavePage.workingDays[i]);
                if (multipleApplyResponse.contains("Your Leave has been applied successfully")) {
                    Reporter("Leave Applied From" + leavePage.workingDays[i].toString() + "toDate" + leavePage.workingDays[i] + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Pass");
                } else if (multipleApplyResponse.contains("failure")) {
                    Reporter("Leave Applied From" + leavePage.workingDays[i].toString() + "toDate" + leavePage.workingDays[i] + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Fail");

                }
            }
        }
*/
        return true;
    }

    /*
    Applies Leave with respect to number of days given in argument
     */
    public String singleApply(LocalDate... workingDays) {

        LocalDate fromDate = workingDays[0];
        LocalDate toDate = workingDays[workingDays.length - 1];
        String leaveID = new LeaveService().getLeaveID(getLeave_Type(), groupCompanyMongoId);

        return leaveAdmin.ApplyLeave(userMongoId, fromDate.toString(), toDate.toString(), leaveID);
    }


    public boolean verifyMaximumAllowedPerYearAndMaxAllowedPerMpnth() {
        LocalDate serverChangedDate = dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);

        int maximumNumberOfDaysInMonth;

        if (getMaximum_leave_allowed_per_month()==0)
            maximumNumberOfDaysInMonth = serverChangedDate.lengthOfMonth();

        else
            maximumNumberOfDaysInMonth = getMaximum_leave_allowed_per_month();


        leavePage.setFromAndToDatesWithoutProperty(maximumNumberOfDaysInMonth, serverChangedDate);

        String singleApplyResponse = singleApply(leavePage.workingDays);

        //single application for all  days in a month
        Reporter("Applying Leave For All days ion a Month in a single Application,Leave Settings:getMaximum_leave_allowed_per_year()=" + getMaximum_leave_allowed_per_year() + "MaxiMumAllowedperMonth=" + getMaximum_leave_allowed_per_month(), "INFO");
        if (singleApplyResponse.contains("Your Leave has been applied successfully")) {
            Reporter("Leave Applied From" + leavePage.workingDays[0].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "Policy Settings are Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Pass");
        } else if (singleApplyResponse.contains("failure")) {
            Reporter("Leave Applied From" + leavePage.workingDays[0].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "Policy Settings are Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Fail");

        }


        leaveAdmin.revokeLeave(getLeave_Type(), leavePage.workingDays[0].toString());


        Reporter("Applying Leave For All days ion a Month in a Multiple Applications,Leave Settings:getMaximum_leave_allowed_per_year()=" + getMaximum_leave_allowed_per_year() + "MaxiMumAllowedperMonth=" + getMaximum_leave_allowed_per_month(), "INFO");

        for (int i = 0; i < leavePage.workingDays.length; i++) {
            String multipleApplyResponse = singleApply(leavePage.workingDays[i]);
            if (multipleApplyResponse.contains("Your Leave has been applied successfully")) {
                Reporter("Leave Applied From" + leavePage.workingDays[i].toString() + "toDate" + leavePage.workingDays[i] + "Policy Settings are Maximum Allowed Per year =" + getMaximum_leave_allowed_per_year() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Pass");
            } else if (multipleApplyResponse.contains("failure")) {
                Reporter("Leave Applied From" + leavePage.workingDays[i].toString() + "toDate" + leavePage.workingDays[i] + "Policy Settings are Maximum Allowed Per Year =" + getMaximum_leave_allowed_per_year() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month(), "Fail");

            }
        }


        if (getMaximum_leave_allowed_per_month() != 0) {
            Reporter("Applying More Than Maximum AllowedinMonth=" + getMaximum_leave_allowed_per_month() + "APPROVED Leaves till are from" + leavePage.workingDays[0].toString() + "to" + leavePage.workingDays[leavePage.workingDays.length - 1].toString(), "INFO");

            leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed() + 1, serverChangedDate);
            String failedResponse = singleApply(leavePage.workingDays[leavePage.workingDays.length - 1]);
            if (failedResponse.contains("failure") && failedResponse.contains("Over-utilizing this leave is not allowed.As you have utilized max per month leaves for this month you cannot apply leave for this month")) {
                Reporter("Unable to apply Leave  From" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "User Has APPROVED Leaves=" + getConsecutive_leave_allowed() + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month() + "Response is" + failedResponse, "Pass");
            } else if (failedResponse.contains("success")) {
                Reporter("Leave Applied From" + leavePage.workingDays[leavePage.workingDays.length - 1].toString() + "toDate" + leavePage.workingDays[leavePage.workingDays.length - 1] + "User Has APPROVED Leaves=" + getConsecutive_leave_allowed() + "Policy Settings are Consequtive leave Allowed =" + getConsecutive_leave_allowed() + "Maximum Leave Allowed Leave Per Month" + getMaximum_leave_allowed_per_month() + "Response is" + failedResponse, "Fail");
            }

            //MOVE TO NEXT MONTH AND APPLY LEAVES..
        }

        for (int i = 0; i < leavePage.workingDays.length; i++) {
            leaveAdmin.revokeLeave(getLeave_Type(), leavePage.workingDays[i].toString());
        }

        return true;
    }


    public boolean verifyConsequitiveDaysAllowedAndRestrictDays() {
        LocalDate serverChangedDate = dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);


        String[] days = data.get("Restrict_to_Week_Days").split(",");


        String[] daystoApply = new String[2];
        daystoApply[0] = days[0];
        daystoApply[1] = days[1];

        restrictDays = new LocalDate[2];

        String leaveID = new LeaveService().getLeaveID(getLeave_Type(), groupCompanyMongoId);


        applyLeaveThisDays(daystoApply, serverChangedDate);
        String response = leaveAdmin.ApplyLeave(userMongoId, restrictDays[0].toString(), restrictDays[restrictDays.length - 1].toString(), leaveID);

        if (days[0].equalsIgnoreCase("Monday") && days[1].equalsIgnoreCase("tuesday")) {
            Reporter("Applying Leave From Monday ,Tuesday with Policy Settings: Restrict To WeekDays=" + data.get("Restrict_to_Week_Days") + "Consequitive Days Allowed =" + getConsecutive_leave_allowed(), "INFO");
            if (response.contains("success")) {
                Reporter("Leave Applied SuccesFully From" + restrictDays[0].toString() + restrictDays[restrictDays.length - 1].toString() + "Policy Settings:getConsecutive_leave_allowed() is" + getConsecutive_leave_allowed() + "Restrict Days is" + days, "Pass");
            } else {
                Reporter("Leave Applied SuccesFully On" + restrictDays[0].toString() + restrictDays[restrictDays.length - 1].toString() + "Policy Settings:getConsecutive_leave_allowed() is" + getConsecutive_leave_allowed() + "Restrict Days is" + days, "Fail");
            }

            new LeaveAdmin().revokeLeave(getLeave_Type(), restrictDays[0].toString());
        } else {
            Reporter("Applying Leave From Monday to Wednesday with Policy Settings: Restrict To WeekDays=" + data.get("Restrict_to_Week_Days") + "Consequitive Days Allowed =" + getConsecutive_leave_allowed(), "INFO");
            if (response.contains("failure") && response.contains("Over-utilizing this leave is not allowed as max consecutive days limit exceeded")) {
                Reporter("Cannot Apply Leave From" + restrictDays[0].toString() + restrictDays[restrictDays.length - 1].toString() + "Policy Settings:getConsecutive_leave_allowed() is" + getConsecutive_leave_allowed() + "Restrict Days is" + days, "Pass");
            } else {
                Reporter("Leave Applied SuccesFully On" + restrictDays[0].toString() + restrictDays[restrictDays.length - 1].toString() + "Policy Settings:getConsecutive_leave_allowed() is" + getConsecutive_leave_allowed() + "Restrict Days is" + days, "Fail");
            }

            //new LeaveAdmin().revokeLeave(getLeave_Type(),restrictDays[0].toString());
        }
        return true;
    }


    /*
    verify ConsexutiveDaysAllowed with Max/Min Leave Allowed in Single Application
     */
    public boolean verifyConsequtiveDaysAllowedAndMaxMinCombination() {
        LocalDate serverChangedDate = dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);
        leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed(), serverChangedDate);

        int minConseDay = getMinimum_consecutive_days_allowed_in_a_single_application();
        int maxConsDay = getMaximum_consecutive_days_allowed_in_a_single_application();
        int consequtiveDays = getConsecutive_leave_allowed();

        Reporter("Leave Settings:ConsecutiveDaysAllowed-" + getConsecutive_leave_allowed() + "  MinimumConsecutiveDaysAllowedInSingleApplication " + getMinimum_consecutive_days_allowed_in_a_single_application()
                + "MaximumConsecutiveDaysAllowedInSingleApplication-" + getMaximum_consecutive_days_allowed_in_a_single_application(), "Info");


        //less than Min Consecitive Days
        for (int i = minConseDay - 1; i > 0; i--) {
            Reporter("Applying Leave From " + leavePage.workingDays[0].toString() + " to  " + leavePage.workingDays[i].toString(), "info");
            String fail = singleApply(Arrays.copyOf(leavePage.workingDays, i));
            verifyLeaveResponse(fail, "fail");
            Assert.assertTrue(fail.contains("Minimum consecutive leave days should be " + getMinimum_consecutive_days_allowed_in_a_single_application()), "Failed to Verify Error Message For Min Consecutive days Allowed in Single Application");

        }

        //in btween min and max
        for (int i = minConseDay; i <= maxConsDay; i++) {
            Reporter("Applying Leave From " + leavePage.workingDays[0].toString() + " to  " + leavePage.workingDays[i].toString(), "info");
            String sucess = singleApply(Arrays.copyOf(leavePage.workingDays, i));
            verifyLeaveResponse(sucess, "pass");
            new LeaveAdmin().revokeLeave(getLeave_Type(), leavePage.workingDays[0].toString());
        }

        //greater than max
        leavePage.setFromAndToDatesWithoutProperty(getMaximum_consecutive_days_allowed_in_a_single_application() + 1, serverChangedDate);
        String fail = singleApply(leavePage.workingDays);
        verifyLeaveResponse(fail, "fail");
        Assert.assertTrue(fail.contains("Maximum consecutive leave days allowed is " + getMaximum_consecutive_days_allowed_in_a_single_application()), "Failed to Verify Error Message For Max Consecutive days Allowed in Single Application");
        return true;
    }

    /*
    To Indicate pass/fail in the report
     */
    public void verifyLeaveResponse(String response, String indicator) {
        if (indicator.equalsIgnoreCase("pass")) {
            if (response.contains("success"))
                Reporter("Leave is Applied Successfully", "pass");
            else if (response.contains("failure"))
                Reporter("Cannot Apply Leave On this Day", "fail");
        }

        if (indicator.equalsIgnoreCase("fail")) {
            if (response.contains("fail"))
                Reporter("Cannot Apply Leave On this Day", "pass");
            else if (response.contains("failure"))
                Reporter("Leave Applied Successfully", "fail");
        }

    }

    /*
    Validates Consecutive leave Allowed Combination(In Between Months)
     */
    public boolean verifyConsequtiveLeaveAllowedInBetweenMonths() {
        LocalDate today = dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);
        int lastDayOfMonth = today.lengthOfMonth();

        dateTimeHelper.changeServerDateToStartOfFirstmonth(driver);
        int conseqDaysAllowed = getConsecutive_leave_allowed();

        LocalDate serverChangeDate = today.plusDays(today.lengthOfMonth() - (conseqDaysAllowed) + 2);
        dateTimeHelper.changeServerDate(driver, today.plusDays(today.lengthOfMonth() - (conseqDaysAllowed) + 2).toString());

        leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed(), serverChangeDate);

        //single Apply
        Reporter("In Between Months Single ApplyLeave Settings : ConsecutiveDaysAllowed-" + getConsecutive_leave_allowed(), "Info");

        String response = singleApply(leavePage.workingDays);
        verifyLeaveResponse(response, "pass");


        new LeaveAdmin().revokeLeave(getLeave_Type(), leavePage.workingDays[0].toString());

        //apply failure case
        leavePage.setFromAndToDatesWithoutProperty(getConsecutive_leave_allowed() + 1, serverChangeDate);
        String fail = singleApply(leavePage.workingDays);
        verifyLeaveResponse(fail, "fail");
        Assert.assertTrue(fail.contains("Over-utilizing this leave is not allowed as max consecutive days limit exceeded"), "Error Message Verification Failed");
        return true;
    }

    /*
    Applies Leave only on the days given in argument
     */
    public void applyLeaveThisDays(String[] days, LocalDate serverDate) {
        LocalDate today = serverDate;
        LocalDate tempDate;
        String leaveID = new LeaveService().getLeaveID(getLeave_Type(), groupCompanyMongoId);
        int c = 0;

        for (String day : days) {
            for (int i = 0; i < 7; i++) {
                tempDate = today.plusDays(i);
                if (tempDate.getDayOfWeek().toString().equalsIgnoreCase(day)) {
                    restrictDays[c] = tempDate;
                    c++;
                }
            }
        }
    }

}



