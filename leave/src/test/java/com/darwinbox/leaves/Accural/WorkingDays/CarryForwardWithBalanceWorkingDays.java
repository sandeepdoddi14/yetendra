package com.darwinbox.leaves.Accural.WorkingDays;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.ProbationPeriodForLeaveValidity;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;


public class CarryForwardWithBalanceWorkingDays extends LeaveAccuralBase {

    LeavePolicyObject carryForwardBalance=null;
    LeavePolicyObject balancePolicyObject=null;



    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;


    static LocalDate serverDateInFormat = null;
    LocalDate doj=null;

    LeavesAction leavesAction = null;



    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leavesAction= new LeavesAction(driver);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyCarryForwardBalance(Map<String, String> testData) {

        setDenominatorForWorkingDays(testData);
        carryForwardBalance= getCarryForwardWorkingDaysPolicy(testData);
        balancePolicyObject =carryForwardBalance;

        super.carryForward=true;

        //leave validity also needs to be set to zero for carry forward
        if(carryForwardBalance.getProbation_period_before_leave_validity().custom &&
            !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            carryForwardBalance.getProbation_period_before_leave_validity().customMonths=0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
            ///carryForwardBalance.getProbation_period_before_leave_validity()




        
        Assert.assertTrue(setEmployeeId("E1582725086492"), "Employee ID is set Mnually");

        leavesAction.setEmployeeID("E1582725086492");
        Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;


        leaveCycleStartDate=LocalDate.parse("2019-04-01");
        leaveCycleEndDate = LocalDate.parse("2020-03-31");





            new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.toString());
            serverChangedDate = leaveCycleEndDate.toString();
            serverDateInFormat = LocalDate.parse(serverChangedDate);

            double actualLeaveBalance = 0.0D;
            double expecetedLeaveBalance = 0.0D;

            doj= leaveCycleEndDate;
           // leavesAction.setEmployeeID(employee.getEmployeeID());
            while (!doj.isBefore(leaveCycleStartDate)) {
                if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {

                    leavesAction.removeEmployeeLeaveLogs();
                    changeEmployeeDOJ(doj);
                    DateOfJoining=doj.toString();
                    Reporter("DOJ is changed to "+doj.toString(),"Info");

                    super.carryForward=true;

                    //leave validity also needs to be set to zero for carry forward
                    if(carryForwardBalance.getProbation_period_before_leave_validity().custom &&
                            !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
                        carryForwardBalance.getProbation_period_before_leave_validity().customMonths=0;


                    super.setLeavePolicyObject(carryForwardBalance);
                    //carry forward balance
                    DateOfJoining=doj.toString();
                    expecetedLeaveBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(doj.toString(), leaveCycleEndDate.toString());


                    //expecetedLeaveBalance= Math.round(expecetedLeaveBalance);

                    Reporter("Expected Balance at Leave Cycle End Date is --"+Math.round(expecetedLeaveBalance *100)/100,"Info");
                    Reporter("Actual Balance at Leave Cycle End Date is --"+ new LeaveBalanceAPI(EmployeeId,carryForwardBalance.getLeave_Type()).getTotalBalance(),"Info");


                    new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.plusDays(10).toString());
                    serverChangedDate = leaveCycleEndDate.plusDays(10).toString();
                    serverDateInFormat = LocalDate.parse(serverChangedDate);


                    //2.reset probation before leave validity
                    ProbationPeriodForLeaveValidity probationPeriodForLeaveValidity = new ProbationPeriodForLeaveValidity();
                    probationPeriodForLeaveValidity.custom=testData.get("Leave Probation Period according to Custom Months").equalsIgnoreCase("yes")?true:false;
                    probationPeriodForLeaveValidity.probation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;
                    //if(probationPeriodForLeaveValidity.probation)
                    //employeeProbation=testData.get("Employee Probation Period");
                    if(probationPeriodForLeaveValidity.custom)
                        probationPeriodForLeaveValidity.customMonths=Integer.parseInt(testData.get("Probation period before leave validity months").replace(".0",""));

                    balancePolicyObject.setProbation_period_before_leave_validity(probationPeriodForLeaveValidity);


                    super.setLeavePolicyObject(balancePolicyObject);

                    leaveCycleStartDate=leaveCycleStartDate.plusYears(1);
                    leaveCycleEndDate=leaveCycleEndDate.plusYears(1);

                    carryForward = false;

                    //call leave balance for one day
                    //this will add leave balance to carry forward balnce for one day
                    expecetedLeaveBalance=expecetedLeaveBalance+calculateLeaveBalanceAsPerEmployeeWorkingDays(leaveCycleStartDate.toString(), getServerOrLocalDate().minusDays(1).toString());

                    expecetedLeaveBalance = Math.round(expecetedLeaveBalance * 100.0) / 100.0;

                    Reporter("Expected Leave Balance after carry forward  is --" + expecetedLeaveBalance, "Info");



                    //it is mandatory
                   // leavesAction.removeEmployeeCarryForwardLeaveLogs();
                    leavesAction.runCarryFrowardCronByEndPointURL();




                    actualLeaveBalance = new LeaveBalanceAPI(EmployeeId,carryForwardBalance.getLeave_Type()).getBalance();

                    Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");

                    if (expecetedLeaveBalance == actualLeaveBalance)
                        Reporter("Passed |||| actual and expected are same", "Pass");
                    else
                        Reporter("Failed |||| actual and expected are not same", "Fail");

                    leaveCycleStartDate=leaveCycleStartDate.minusYears(1);
                    leaveCycleEndDate=leaveCycleEndDate.minusYears(1);

                }
                doj = doj.minusDays(1);
            }


        }
    }

