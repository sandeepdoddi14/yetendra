package com.darwinbox.leaves.Application;


import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.Services;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restrictions extends TestBase {

    LeaveService leaveService;
    LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    LoginPage loginpage;
    CommonAction commonAction;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects(){
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

        leaveService=new LeaveService();
        leavePolicyObject =new LeavePolicyObject();
        mapUtils=new MapUtils();
        leaveAdmin=new LeaveAdmin();
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyRestrictions(Map<String,String> data){
        String field = data.get("Field");
        leavePolicyObject =new LeavePolicyObject();


        if (field.equalsIgnoreCase("restrictconditions")) {

            EmployeeServices empServices = new EmployeeServices();
            Employee employee = empServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
            // List<Employee> employees = empServices.generateRandomEmployees("no", "Working Days (DO NOT TOUCH)", 1, true);

           // CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month");
            Map<String, String> defaultBody = leaveService.getDefaultforLeaveDeduction();
            Map<String, String> request = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
            request.put("Leaves[p2_max_per_month]", leavePolicyObject.getMaximum_leave_allowed_per_month()+"");
            defaultBody.putAll(request);
            List<NameValuePair> formdata = new Services().mapToFormData(defaultBody);


            //Employee employee = employees.get(0);
            String emptype = employee.getEmployeeTypeID();
            String locationId = employee.getLocationID();
            String jobLevelID = employee.getJobLevel();


            HashMap departments = new Services().getDepartments(leavePolicyObject.groupCompanyMongoId);
            HashMap empTypes = new Services().getEmployeeTypes();
            HashMap locations = new Services().getOfficeLocations(leavePolicyObject.groupCompanyMongoId);
            HashMap grades = new Services().getGrades();
            HashMap bands = new Services().getBands();

            HashMap<String, HashMap<String, String>> jobLevelData = new Services().getJobLevel();
            String gradeOfEmp = jobLevelData.get(jobLevelID).keySet().toString().replace("[", "").replace("]", "");

            HashMap<String, String> gradesWithBands = new Services().getGradeAndBand();
            String bandofEmp = gradesWithBands.get(gradeOfEmp);


            String[] locationSet = new String[]{"empty", "LOC_" + locationId, "LOC_" + mapUtils.notContainsInMap(locations, locationId)};
            String[] empTypeSet = new String[]{"empty", "TYP_" + emptype, "TYP_" + mapUtils.notContainsInMap(empTypes, emptype)};
            String[] departmentSet = new String[]{"empty", mapUtils.notContainsInMap(departments, "empty")};//only 2
            String[] bandsSet = new String[]{"empty", "BAND_" + bands.get(bandofEmp), "BAND_" + mapUtils.notContainsInMap(bands, bandofEmp)};
            String[] gradesSet = new String[]{"empty", "GRADE_" + grades.get(gradeOfEmp), "GRADE_" + mapUtils.notContainsInMap(grades, gradeOfEmp)};

            for (int i = 0; i <= 1; i++) {
                if (i == 1) {
                    Reporter("Switching to OR Condition", "Info");
                    formdata.removeIf(x -> x.getName().contains("Leaves[or_and]"));
                    formdata.add(new BasicNameValuePair("Leaves[or_and]", "1"));
                }
                for (String empType : empTypeSet) {
                    //List<NameValuePair> formData1=new ArrayList<>();
                    if (!empType.equalsIgnoreCase("empty")) {
                        formdata.add(new BasicNameValuePair("Leaves[restrictOther][]", empType));
                    }
                    for (String department : departmentSet) {
                        if (!department.equalsIgnoreCase("empty")) {
                            formdata.add(new BasicNameValuePair("Leaves[restrictOther][]", department));
                        }

                        for (String grade : gradesSet) {
                            if (!grade.equalsIgnoreCase("empty")) {
                                formdata.add(new BasicNameValuePair("Leaves[restrictOther][]", grade));
                            }
                            for (String band : bandsSet) {
                                if (!band.equalsIgnoreCase("empty")) {
                                    formdata.add(new BasicNameValuePair("Leaves[restrictOther][]", band));
                                }

                                for (String location : locationSet) {
                                    if (!location.equalsIgnoreCase("empty")) {
                                        formdata.add(new BasicNameValuePair("Leaves[restrictOther][]", location));
                                        leaveService.createLeave(formdata, leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
                                        formdata.removeIf(x -> x.getName() == "Leaves[restrictOther][]");
                                        String id = empServices.getEmployeeId(employee.getFirstName()).split("id")[1].substring(4, 17);
                                        Reporter("Restrict Conditions   EmpType=" + mapUtils.getValueOfReversedMap(empTypes,empType.replace("TYP_",""))
                                                                           + ";Department=" + mapUtils.getValueOfReversedMap(departments,department) +
                                                                            ";grade=" + mapUtils.getValueOfReversedMap(grades,grade.replace("GRADE_","")) +
                                                                            ";band=" + mapUtils.getValueOfReversedMap(bands,band.replace("BAND_","")) +
                                                                           ";location=" + mapUtils.getValueOfReversedMap(locations,location.replace("LOC_","")), "Info");
                                        Reporter("Employee Condtions    EmpType=" + mapUtils.getValueOfReversedMap(empTypes,emptype.replace("TYP_","")) +
                                                                            ";Department=" + "empty" +
                                                                             ";grade=" + mapUtils.getValueOfReversedMap(grades,grades.get(gradeOfEmp).toString()) +
                                                                             ";band=" + mapUtils.getValueOfReversedMap(bands,bands.get(bandofEmp).toString()) +
                                                                              ";location=" + mapUtils.getValueOfReversedMap(locations,locationId.replace("LOC_","")), "Info");

                                        if (i == 0) {
                                            if (checkANDRestriction(department, location, empType, grade, band, employee) && leaveAdmin.checkifLeaveIsDisplayedForUser(id, leavePolicyObject.getLeave_Type()))
                                                Reporter("Leave is enabled for user", "pass");

                                            else
                                                Reporter("Leave is disabled for user", "pass");
                                        } else {
                                            if (checkORRestriction(department, location, empType, grade, band, employee) && leaveAdmin.checkifLeaveIsDisplayedForUser(id, leavePolicyObject.getLeave_Type()))
                                                Reporter("Leave is enabled for user", "pass");

                                            else
                                                Reporter("Leave is disabled for user", "pass");

                                        }

                                    } else {
                                        leaveService.createLeave(formdata, leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
                                        Reporter("Restrict Conditions   EmpType=" + mapUtils.getValueOfReversedMap(empTypes,empType.replace("TYP_",""))
                                                + ";Department=" + mapUtils.getValueOfReversedMap(departments,department) +
                                                ";grade=" + mapUtils.getValueOfReversedMap(grades,grade.replace("GRADE_","")) +
                                                ";band=" + mapUtils.getValueOfReversedMap(bands,band.replace("BAND_","")) +
                                                ";location=" + mapUtils.getValueOfReversedMap(locations,location.replace("LOC_","")), "Info");
                                        Reporter("Employee Condtions    EmpType=" + mapUtils.getValueOfReversedMap(empTypes,emptype.replace("TYP_","")) +
                                                ";Department=" + "empty" +
                                                ";grade=" + mapUtils.getValueOfReversedMap(grades,grades.get(gradeOfEmp).toString()) +
                                                ";band=" + mapUtils.getValueOfReversedMap(bands,bands.get(bandofEmp).toString()) +
                                                ";location=" + mapUtils.getValueOfReversedMap(locations,locationId.replace("LOC_","")), "Info");

                                        String id = empServices.getEmployeeId(employee.getFirstName()).split("id")[1].substring(4, 17);


                                        if (i == 0) {
                                            if (checkANDRestriction(department, location, empType, grade, band, employee) && leaveAdmin.checkifLeaveIsDisplayedForUser(id, leavePolicyObject.getLeave_Type()))
                                                Reporter("Leave Type :"+ leavePolicyObject.getLeave_Type()+ " is enabled for user", "pass");

                                            else
                                                Reporter("Leave Type :"+ leavePolicyObject.getLeave_Type()+ "is disabled for user", "pass");
                                        } else {
                                            if (checkORRestriction(department, location, empType, grade, band, employee) && leaveAdmin.checkifLeaveIsDisplayedForUser(id, leavePolicyObject.getLeave_Type()))
                                                Reporter("Leave Type :"+ leavePolicyObject.getLeave_Type()+" enabled for user", "pass");

                                            else
                                                Reporter("Leave Type "+ leavePolicyObject.getLeave_Type()+"  is disabled for user", "pass");

                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    public boolean checkORRestriction(String dep, String loc, String empType, String grade, String band, Employee e) {
        String empLoaction = e.getLocationID();
        String emptype = e.getEmployeeType();

        return checkDepartment(dep, "empty") || checkBand(band, "empty") || checkGrade(grade, "empty") || checkLocation(loc, e.getLocationID()) || checkType(empType, e.getEmployeeTypeID());
    }


    public boolean checkANDRestriction(String dep, String loc, String empType, String grade, String band, Employee e) {
        String empLoaction = e.getLocationID();
        String emptype = e.getEmployeeType();

        return checkDepartment(dep, "empty") && checkBand(band, "empty") && checkGrade(grade, "empty") && checkLocation(loc, e.getLocationID()) && checkType(empType, e.getEmployeeTypeID());
    }

    public boolean checkDepartment(String dep, String empDepartment) {
        if (!dep.equalsIgnoreCase("empty")) {
            return dep.equals(empDepartment);
        } else {
            return true;
        }

    }

    public boolean checkLocation(String loc, String empLocation) {
        if (!loc.equalsIgnoreCase("empty")) {
            return loc.equals("LOC_" + empLocation);
        } else {
            return true;
        }

    }

    public boolean checkBand(String band, String empBand) {
        if (!band.equalsIgnoreCase("empty")) {
            return band.equals(empBand);
        } else {
            return true;
        }

    }

    public boolean checkGrade(String grade, String empGrade) {
        if (!grade.equalsIgnoreCase("empty")) {
            return grade.equals(empGrade);
        } else {
            return true;
        }

    }

    public boolean checkType(String type, String empType) {
        if (!type.equalsIgnoreCase("empty")) {
            return type.equals("TYP_" + empType);
        } else {
            return true;
        }

    }
}
