package com.darwinbox.recruitment;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.core.employee.objects.Department;
import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DepartmentServices;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.*;
import com.darwinbox.recruitment.objects.staffingModel.PositionData;
import com.darwinbox.recruitment.objects.staffingModel.PositionSettings;
import com.darwinbox.recruitment.services.DesignationsService;
import com.darwinbox.recruitment.services.PermissionService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.json.JSONArray;
import org.testng.Assert;

import java.util.*;

public class RequisitionTestBase extends TestBase {

    private String fileName;
    private static HashMap<String, RequisitionTestBase> obj;
    public static Employee employee = null;

    public RequisitionTestBase() {

    }

    private RequisitionTestBase(String fileName) {
        this.fileName = fileName;
    }

    private List<Map<String, String>> readDatafromSheet(String sheetname) {

        Map<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "BaseData/" + fileName);
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }

    public static RequisitionTestBase getObject(String baseFile) {

        if (obj == null)
            obj = new HashMap<>();

        RequisitionTestBase baseData = obj.get(baseFile);

        if (baseData == null) {
            baseData = new RequisitionTestBase(baseFile);
            //baseData.loadData();
            obj.put(baseFile, baseData);
        }
        return baseData;
    }

    /*Below method creates requisition based on Req ID passed */
    public Requisition createRequisition(String type) {

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        Requisition requisition = new Requisition();
        RequisitionService requisitionService = new RequisitionService();

        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");

            if (reqName.equalsIgnoreCase(type)) {

                requisition.toObject(data);
                requisitionService.createRequisition(requisition);
                break;
            }
        }
        return requisition;
    }

    /*To create req with specific designation such as Hiring Lead to create requisiton*/

    public Requisition desgHirLead(String type,Designations designations) {

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        Requisition requisition = new Requisition();
        RequisitionService requisitionService = new RequisitionService();
        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                requisition.toObject(data);
                requisition.setDesignationAndDepartment(designations.getId()); //desgn ID
                requisitionService.createRequisition(requisition);
                break;
            }
        }
        return requisition;
    }

    public Designations createDesignations(String type) {

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        Designations designations = new Designations();
        DesignationsService designationsService = new DesignationsService();

        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                String testDataName = data.get("DesignationName");
                String namePresent = designationsService.getDesignationsID(testDataName).getDesignation();

                if (employee != null)
                    designations.setHiringLead(employee.getUserID());

                designations.setId(designationsService.getDesignationsID(testDataName).getId());
                designations.setNumberOfPositions(data.get("NumOfPositions"));
                designations.setStaffingModel(
                        com.darwinbox.recruitment.objects.Designations.staffingModel.valueOf(data.get("StaffingModel")));
                if (testDataName.equalsIgnoreCase(namePresent)) {
                    designations.setDefaultForDesignation(namePresent,data);
                    designationsService.updateDesignation(designations);
                    Reporter("Designation is updated ::" + testDataName, "INFO");
                } else {
                    designations.setDefaultForDesignation(namePresent,data);
                    designationsService.createDesignation(designations, designations.getStaffingModel().s); //check desgn name id
                    Reporter("Designation is created ::" + testDataName, "INFO");
                }

                if (designations.getHiringLead() == null)
                    Reporter("Hiring lead is not set in designation -" + testDataName, "ERROR");
                break;
            }
        }
        return designations;
    }
/*Below method creates designation, by passing desgn and dept names in base data*/
    public Designations createDeptWiseDesignation(String type) {

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        Designations designations = new Designations();
        DesignationsService designationsService = new DesignationsService();
        Department department = new Department();
        DepartmentServices departmentServices = new DepartmentServices();

        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                String testDataName = data.get("DesignationName");
                String namePresent = designationsService.getDesignationsID(testDataName).getDesignation();

                designations.setDefaultForUpdateDesignation(namePresent);
                department=departmentServices.getAllDepartments(department,data.get("DepartmentName"));
                designations.setDepartment(department.getId());
                designations.setId(designationsService.getDesignationsID(testDataName).getId());
                designations.setNumberOfPositions(data.get("NumOfPositions"));
                designations.setJobCode(designationsService.getDesignationsID(testDataName).getJobCode());
                designations.setStaffingModel(
                        com.darwinbox.recruitment.objects.Designations.staffingModel.valueOf(data.get("StaffingModel")));

                designationsService.updateDesignation(designations);
                Reporter("Designation is updated ::" + testDataName+" with Dept."+department.getDepartmentName(), "INFO");

                department.toObject(data);
                department.setHeadOfDepartment(employee.getUserID());

                departmentServices.editDepartment(department);
                Reporter("Department is updated ::" + department.getDepartmentName(), "INFO");

                break;
            }

        }
        return designations;
    }

    public Designations updateDesWithDeptOtherThanExisting(String deptID,String type){

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");

        Designations designations = new Designations();
        DesignationsService designationsService = new DesignationsService();
        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                String testDataName = data.get("DesignationName");
                String namePresent = designationsService.getDesignationsID(testDataName).getDesignation();

                designations.setDefaultForUpdateDesignation(namePresent);
                designations.setDepartment(deptID);
                designations.setId(designationsService.getDesignationsID(testDataName).getId());
                designations.setNumberOfPositions(data.get("NumOfPositions"));
                designations.setJobCode(designationsService.getDesignationsID(testDataName).getJobCode());
                designations.setStaffingModel(
                        com.darwinbox.recruitment.objects.Designations.staffingModel.valueOf(data.get("StaffingModel")));

                designationsService.updateDesignation(designations);
                Reporter("Designation is updated ::" + testDataName + " with other Department "+deptID, "INFO");
                break;
            }
        }
        return designations;
    }

    /*Below methods updates Permission role holder across department*/
    public Designations updatePermissionRoleHolder(String type){

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        Permissions  permissions = new Permissions();
        PermissionService permissionService = new PermissionService();
        Designations designations = new Designations();
        DesignationsService designationsService = new DesignationsService();
        Department department = new Department();
        DepartmentServices departmentServices = new DepartmentServices();

        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                permissions.toObject(data,employee.getUserID());
                department=departmentServices.getAllDepartments(department,data.get("DepartmentName"));
                permissions.setRestrictedGroups("DEPT_"+department.getId());
                permissionService.createRequisitionPermission(permissions);
                Reporter("Permission to raise requisition is given to :"+employee.getUserID(),"INFO");
//create des based on dept, copy code here  of updating des with specified dept

                String testDataName = data.get("DesignationName");
                String namePresent = designationsService.getDesignationsID(testDataName).getDesignation();

                designations.setDefaultForUpdateDesignation(namePresent);
                department=departmentServices.getAllDepartments(department,data.get("DepartmentName"));
                designations.setDepartment(department.getId());
                designations.setId(designationsService.getDesignationsID(testDataName).getId());
                designations.setNumberOfPositions(data.get("NumOfPositions"));
                designations.setJobCode(designationsService.getDesignationsID(testDataName).getJobCode());
                designations.setStaffingModel(
                        com.darwinbox.recruitment.objects.Designations.staffingModel.valueOf(data.get("StaffingModel")));

                designationsService.updateDesignation(designations);
                Reporter("Designation is updated ::" + testDataName+" with Dept."+department.getDepartmentName(), "INFO");

                break;

            }
        }
        return designations;
    }

/*Below method creates des with timestamp */

    public Requisition designationWithTimeStamp(String type){

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        DesignationNames  designationNames = new DesignationNames();
        DesignationNamesServices  designationNamesServices = new DesignationNamesServices();
        Designations designation = new Designations();
        DesignationsService designationsService = new DesignationsService();
        Requisition requisition = new Requisition();
        RequisitionService requisitionService = new RequisitionService();
        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                String designationName = designationNames.toObject();
                designationNamesServices.createDesignationName(designationNames);
                Reporter("Designation Name created is : " + designationName, "INFO");
                Assert.assertNotNull(designationName, "Designation Name is NOT created");

                designation.setDefaultForDesignation(designationNames.getDesignationName(), data);
                designationsService.createDesignation(designation, "");
                Reporter("Number of Positions limit set is ::"+designation.getNumberOfPositions(),"INFO");

                requisition.toObject(data);
                requisition.setTotalPositions(designation.getNumberOfPositions()+1);
                requisitionService.createRequisition(requisition);
                Reporter("Raising requisition for number of positions are ::"+requisition.getTotalPositions(),"INFO");
            }

        }

return requisition;
            }

    /*Below method creates designation with position based staffing model*/
    List<Designations> designations = new ArrayList<>();
    List<DesignationsPositionsLineWise> designationsPositionsLineWise = new ArrayList<>();

    public String createDesWithPositions(String type){

        List<Map<String, String>> reqData = readDatafromSheet("PositionReq");
        String designationID = "";
        DesignationNames  designationNames = new DesignationNames();
        DesignationNamesServices  designationNamesServices = new DesignationNamesServices();
        PositionSettings positionSettings = new PositionSettings();
        Designations designation = new Designations();
        DesignationsService designationsService = new DesignationsService();

        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                positionSettings.UpdatePositions();

                String designationName = designationNames.toObject();
                designationNamesServices.createDesignationName(designationNames);
                Reporter("Designation Name created is : "+designationName,"INFO");
                Assert.assertNotNull(designationName,"Designation Name is NOT created");

            designation.setDefaultForDesignation(designationNames.getDesignationName(),data);
            designationsService.createDesignation(designation,"PositionBased");

                designationID = designationsService.getDesignationID(designationName);
                designation.toObjectPositionStageOne(data);
                designationsService.createPositionStageOne(designation,designationID);

               // addLineWiseForPositionsDesignation();
                //designationsService.createPositionStageTwo(designations,designationID);

                List<Map<String, String>> positions = readDatafromSheet("PositionReq");
                List<Map<String, String>> positionsLineWiseDataList = readDatafromSheet("PositionsInDesignation");

                for (Map<String, String> data1 : positionsLineWiseDataList) {

                    DesignationsPositionsLineWise designationsPositionsLineWise1 = new DesignationsPositionsLineWise();
                    designationsPositionsLineWise1.toObject(data1);
                    designationsPositionsLineWise.add(designationsPositionsLineWise1);
                }

                for (Map<String, String> data1 : positions) {

                    String bodyObjects = data1.get("PositionsInDesignationPage3");
                    String[] objectTypes = bodyObjects.split(",");

                    for (String value : objectTypes) {
                        int index = Integer.parseInt(value) - 1;
                        designation.add(designationsPositionsLineWise.get(index));
                    }
                    designationsService.createPositionStageTwo(designation,designationID);
                    designations.add(designation);
                    Reporter("Designation with Position matrix is created","INFO");
                }
                break;
            }
        }
        return designationID;
    }
    public String createDesigWithPositions(String type) throws InterruptedException {

        List<Map<String, String>> reqData = readDatafromSheet("DesignationPage");
        String designationID="";
     DesignationsService designationsService=new DesignationsService();
     DesignationNames designationNames=new DesignationNames();
        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {
                DesignationsPage designationsPage = new DesignationsPage(driver);

                driver.navigate().to("https://automation4.qa.darwinbox.io/settings/company/designations");
                designationNames=   designationsPage.createDesignation(data);
                designationsPage.createDesignationStage1(data);
                designationsPage.createDesignationStage2(data);
                designationID = designationsService.getDesignationID(designationNames.getDesignationName());

            }
        }
         return designationID;
    }

    public Requisition raiseReqByFetchingDesID(String type,String desID,String positionID) {

        List<Map<String, String>> reqData = readDatafromSheet("PositionReq");
        Requisition requisition = new Requisition();
        RequisitionService requisitionService = new RequisitionService();
        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                requisition.toObject(data);
                requisition.setDesignationAndDepartment(desID); //desgn ID
                requisition.setPositionID(positionID);
                requisitionService.createRequisition(requisition);
                break;
            }
        }
        return requisition;
    }


    /*Below method creates requisition with position matrix*/

    public void raiseReqPositionMatrix(List<PositionData> linewiseList){

        List<Map<String, String>> positionsMatrix = readDatafromSheet("PositionReq");
    //    List<Map<String, String>> positionsLineWiseDataList = readDatafromSheet("ReqPositionMatrix");

        for (PositionData data1 : linewiseList) {

               // sending position ID's through parameters as there is no line wise data passed from sheet
        }
        for (Map<String, String> data1 : positionsMatrix) {

        }

    }


    public Designations defaultRecruitmentSettings(String type) {

        List<Map<String, String>> reqData = readDatafromSheet("ReqType");
        RecruitmentSettings recruitmentSettings = new RecruitmentSettings();
        Designations designations = new Designations();

        for (Map<String, String> data : reqData) {

            String reqName = data.get("RequisitionTypeID");
            if (reqName.equalsIgnoreCase(type)) {

                recruitmentSettings.toMapDefaultBody(employee,data);
                designations.setDepartment(employee.getDepartmentId());
                designations.setId(employee.getDesignationID());
                break;
            }
        }
        return designations;
    }





}
