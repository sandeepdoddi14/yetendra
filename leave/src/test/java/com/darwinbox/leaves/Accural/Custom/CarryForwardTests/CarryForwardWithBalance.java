package com.darwinbox.leaves.Accural.Custom.CarryForwardTests;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.ProbationPeriodForLeaveValidity;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;


public class CarryForwardWithBalance extends LeaveAccuralBase {

    Employee employee = new Employee();

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

        LeavePolicyObject carryForwardBalance = getCarryForwardPolicy(testData);
        super.carryForward=true;
        //making default to begin of month for calculation
        if(carryForwardBalance.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=carryForwardBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            carryForwardBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        //leave validity also needs to be set to zero for carry forward
        if(carryForwardBalance.getProbation_period_before_leave_validity().custom &&
            !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            carryForwardBalance.getProbation_period_before_leave_validity().customMonths=0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
            ///carryForwardBalance.getProbation_period_before_leave_validity()

        super.setLeavePolicyObject(carryForwardBalance);

        leaveCycleStartDate = LocalDate.parse("2019-05-01");
        leaveCycleEndDate = LocalDate.parse("2020-04-30");



        new DateTimeHelper().changeServerDate(driver, LocalDate.now().toString());

        try {
            employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.toString(), "no"));
        } catch (Exception e) {
            try {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.toString(), "no"));
            } catch (Exception e1) {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.toString(), "no"));


            }
        }

            super.employee = employee;


            new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.plusDays(1).toString());
            serverChangedDate = leaveCycleEndDate.plusDays(1).toString();
            serverDateInFormat = LocalDate.parse(serverChangedDate);

            double actualLeaveBalance = 0.0D;
            double expecetedLeaveBalance = 0.0D;

            doj= LocalDate.parse(employee.getDoj());
            leavesAction.setEmployeeID(employee.getEmployeeID());
            while (!doj.isBefore(leaveCycleStartDate)) {
                if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {

                    changeEmployeeDOJ(doj,employee);
                    employee.setDoj(doj.toString());


                    expecetedLeaveBalance = calculateLeaveBalance(doj.toString(), leaveCycleEndDate.toString());


                    //restore the policy object
                    //1.reset accural
                    if(testData.get("Accrual").equalsIgnoreCase("yes")?true:false){
                        Credit_On_Accural_Basis credit_on_accural_basis= new Credit_On_Accural_Basis();
                        credit_on_accural_basis.setIndicator(true);

                        if(!testData.get("Monthly").equalsIgnoreCase("yes")?true:false)
                            credit_on_accural_basis.setMonthlyAccuralSetting(false,false,false);
                        else
                            credit_on_accural_basis.setMonthlyAccuralSetting(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

                        if(!testData.get("Quarterly").equalsIgnoreCase("yes")?true:false)
                            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
                        else
                            credit_on_accural_basis.setQuarterlyAccural(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

                        credit_on_accural_basis.setBiAnnual(testData.get("Biannually").equalsIgnoreCase("yes")?true:false);

                        carryForwardBalance.setCredit_on_accural_basis(credit_on_accural_basis);
                    }

                    //2.reset probation before leave validity
                    ProbationPeriodForLeaveValidity probationPeriodForLeaveValidity = new ProbationPeriodForLeaveValidity();
                    probationPeriodForLeaveValidity.custom=testData.get("Leave Probation Period according to Custom Months").equalsIgnoreCase("yes")?true:false;
                    probationPeriodForLeaveValidity.probation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;
                    //if(probationPeriodForLeaveValidity.probation)
                    //employeeProbation=testData.get("Employee Probation Period");
                    if(probationPeriodForLeaveValidity.custom)
                        probationPeriodForLeaveValidity.customMonths=Integer.parseInt(testData.get("Probation period before leave validity months").replace(".0",""));

                    carryForwardBalance.setProbation_period_before_leave_validity(probationPeriodForLeaveValidity);

                    super.setLeavePolicyObject(carryForwardBalance);

                    carryForward = false;
                    //call leave balance for one day
                    //this will add leave balance to carry forward balnce for one day
                    expecetedLeaveBalance=expecetedLeaveBalance+calculateLeaveBalance(leaveCycleEndDate.plusDays(1).toString(), getServerOrLocalDate().toString());
                    Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");


                    //leavesAction.removeEmployeeCarryForwardLeaveLogs();
                    leavesAction.runCarryFrowardCronByEndPointURL();




                    actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getBalance();
                    Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");

                    if (expecetedLeaveBalance == actualLeaveBalance)
                        Reporter("Passed |||| actual and expected are same", "Pass");
                    else
                        Reporter("Failed |||| actual and expected are not same", "Fail");


                }
                doj = doj.minusDays(1);
            }


        }
    }

