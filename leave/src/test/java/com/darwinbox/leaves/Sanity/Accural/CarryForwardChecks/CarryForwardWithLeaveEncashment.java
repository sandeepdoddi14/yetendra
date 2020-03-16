package com.darwinbox.leaves.Sanity.Accural.CarryForwardChecks;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.CarryForwardUnusedLeave;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CarryForwardWithLeaveEncashment extends LeaveAccuralBase {


    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj = null;

    LeavesPage leavePage;
    LeavesAction leavesAction = null;


    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();

        commonAction.changeApplicationAccessMode("Admin");
        leavesAction = new LeavesAction(driver);

        leavePage = PageFactory.initElements(driver, LeavesPage.class);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Create_Leaves_for_Multiple_Allotment_Leave_Transfer(Map<String, String> testData) throws  Exception{


        int noOfLeaves =2;
        leaveCycleStartDate = LocalDate.parse("2019-04-01");
        leaveCycleEndDate = LocalDate.parse("2020-03-31");

        LeavePolicyObject carryForwardBalance = new LeavePolicyObject();
        carryForwardBalance.setFields(testData);
        carryForwardBalance.setEncashmentData(testData);
        if(testData.get("Carry forward").equalsIgnoreCase("yes")){
            CarryForwardUnusedLeave carryForwardUnusedLeave = new CarryForwardUnusedLeave();
            carryForwardUnusedLeave.indicator=testData.get("Carry forward").equalsIgnoreCase("yes")?true:false;

            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("all"))
            {
                carryForwardUnusedLeave.carryForwardAllUnusedLeave=true;
            }
                if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("fixed")){
                carryForwardUnusedLeave.carryForwardOnly=true;
                carryForwardUnusedLeave.fixed=true;
                carryForwardUnusedLeave.fixedValue=Integer.parseInt(testData.get("Fixed/Percentage value"));
            }
            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("percentage")){
                carryForwardUnusedLeave.carryForwardOnly=true;
                carryForwardUnusedLeave.percentage=true;
                carryForwardUnusedLeave.percentageValue=Integer.parseInt(testData.get("Fixed/Percentage value"));
            }

            carryForwardBalance.setCarryForwardUnusedLeave(carryForwardUnusedLeave);

        }

        List<NameValuePair> body = carryForwardBalance.createRequest();
        new LeaveService().createLeaveForPolicy(body, carryForwardBalance);




        changeServerDate(LocalDate.now());


            try {
                  employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusYears(1).toString(), "no"));
            } catch (Exception e) {
                try {
                    employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusYears(1).toString(), "no"));
                } catch (Exception e1) {
                    employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusYears(1).toString(), "no"));

                }
            }

            leavesAction.setEmployeeID(employee.getEmployeeID());
            super.employee = employee;

            Reporter("Employee DOJ  is  --> " + employee.getDoj(),"Info");


            changeServerDate(leaveCycleStartDate.minusYears(1));


                 logoutFromSession();
               // Thread.sleep(5000);
                loginpage.loginToApplication(employee.getEmailID(), employee.getPassword());
                new LeaveAdmin().applyEncashmentWithEmpSession(employee,serverChangedDate,noOfLeaves+"",new LeaveService().getLeaveID(carryForwardBalance.getLeave_Type(),carryForwardBalance.groupCompanyMongoId));
                Reporter("No of Leaves Encashed   -->" +noOfLeaves+";Date -->"+serverChangedDate,"Info");


                logoutFromSession();
                loginpage.loginToApplication();


                String messageId = new LeaveAdmin().getMessageId(employee);
                new LeaveAdmin().encashmentAction(messageId, "accept");


            Reporter("Leave Cycle Start Date is --> " + leaveCycleStartDate.toString(), "Info");
            Reporter("Leave Cycle End Date is --> " + leaveCycleEndDate.toString(), "Info");
            changeServerDate(leaveCycleStartDate);
            Reporter("Carry Forward Cron Run Date is --> " + serverChangedDate, "Info");



            super.carryForward = true;
        //making default to begin of month for calculation
        if (carryForwardBalance.getCredit_on_accural_basis().getIndicator()) {
            Credit_On_Accural_Basis credit_on_accural_basis = carryForwardBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true, true, false);
            credit_on_accural_basis.setQuarterlyAccural(false, false, false);
            credit_on_accural_basis.setBiAnnual(false);
            carryForwardBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        //leave validity also needs to be set to zero for carry forward
        if (carryForwardBalance.getProbation_period_before_leave_validity().custom &&
                !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            carryForwardBalance.getProbation_period_before_leave_validity().customMonths = 0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
        ///carryForwardBalance.getProbation_period_before_leave_validity()

        super.setLeavePolicyObject(carryForwardBalance);


        //leavesAction.removeEmployeeLeaveLogs();
        leavesAction.runCarryFrowardCronByEndPointURL();

            double actualLeaveBalance = 0.0D;
            double expecetedLeaveBalance = 0.0D;

           // super.carryForward= true;

            expecetedLeaveBalance = calculateLeaveBalance(employee.getDoj(), leaveCycleEndDate.minusYears(1).toString())-noOfLeaves;
            Reporter("Expected CF Leave Balance is --" + expecetedLeaveBalance, "Info");


            actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), carryForwardBalance.getLeave_Type()).getCarryForwardBalance();
            Reporter("Actual CF Leave Balance is ---" + actualLeaveBalance, "Info");

            Assert.assertTrue(actualLeaveBalance==expecetedLeaveBalance,"Actual and Expected Leave Balance are not same");


        }

    }

