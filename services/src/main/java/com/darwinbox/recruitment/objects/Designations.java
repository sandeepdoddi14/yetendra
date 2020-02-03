package com.darwinbox.recruitment.objects;

import com.darwinbox.Services;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.core.employee.objects.Department;
import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DepartmentServices;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.*;

public class Designations extends Services {

    private List<DesignationsPositionsLineWise> designationsPositionsLineWises = new ArrayList<>();


    private String designation;
private String groupCompany;
private String department;
private String hiringLead;
private String jobEvaluation;
private String jobDescription;
private String functionalArea;
private String numberOfPositions;
private String jobCode;
private String id;
private boolean isOverHiringAllowed;
private boolean isCountNoticePeriod;

    public enum staffingModel{
        POSITIONBASED("2"),
        JOBBASED("1"),
        NOSTAFFING("");
        public final String s;
        staffingModel(String s){
            this.s = s;
        }
    }
    public static staffingModel staffingModel;

    public static void setStaffingModel(Designations.staffingModel staffingModel) {
        Designations.staffingModel = staffingModel;
    }

    public static Designations.staffingModel getStaffingModel() {
        return staffingModel;
    }
    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public boolean isOverHiringAllowed() {
        return isOverHiringAllowed;
    }

    public void setOverHiringAllowed(boolean overHiringAllowed) {
        isOverHiringAllowed = overHiringAllowed;
    }

    public boolean isCountNoticePeriod() {
        return isCountNoticePeriod;
    }

    public void setCountNoticePeriod(boolean countNoticePeriod) {
        isCountNoticePeriod = countNoticePeriod;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHiringLead() {
        return hiringLead;
    }

    public void setHiringLead(String hiringLead) {
        this.hiringLead = hiringLead;
    }

    public String getJobEvaluation() {
        return jobEvaluation;
    }

    public void setJobEvaluation(String jobEvaluation) {
        this.jobEvaluation = jobEvaluation;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }

    public String getNumberOfPositions() {
        return numberOfPositions;
    }

    public void setNumberOfPositions(String numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Map<String,String> getDefaultForDesignation() {

        Map<String, String> body = new HashMap<>();

        body.put("UserDesignationsForm[designation_name_id]",getDesignation());
        body.put("UserDesignationsForm[parent_company_id]",getGroupCompany());
        body.put("UserDesignationsForm[department_id]",getDepartment());
        body.put("UserDesignationsForm[recruitment_hiring_leads]", getHiringLead());
        body.put("UserDesignationsForm[je_score]","");
        body.put("UserDesignationsForm[jd_id]","");
        body.put("UserDesignationsForm[functional_area]","");
        body.put("UserDesignationsForm[allowed_positions]",getNumberOfPositions());
        body.put("UserDesignationsForm[staffing_model]",getStaffingModel().s);
        body.put("Officelocationset[officelocation][]","");
        body.put("Officelocationset[number_of_position][]","");
        body.put("UserDesignationsForm[auto_numbering]","0");
        body.put("UserDesignationsForm[prefix]","");
        body.put("UserDesignationsForm[next_number]","");

        //job based
       /* body.put("yt0","SAVE");
        body.put("UserDesignationsForm[over_hiring_allowed]", LeaveDeductionsBase.parseToPHP(isOverHiringAllowed()));
        body.put("UserDesignationsForm[cnt_notice_period_employee]",LeaveDeductionsBase.parseToPHP(isCountNoticePeriod()));
*/
        return body;
    }

    public void setDefaultForDesignation(String designationName,Map<String, String> body){

        Department department = new Department();
        DepartmentServices departmentServices = new DepartmentServices();
        String gcName =getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        Object[] departments= getDepartments(id).values().toArray();
        setGroupCompany(id);

        if(body.get("createType").contains("Random")){

            setDepartment(String.valueOf(departments[new Random().nextInt(departments.length)]));

            DesignationNames designationNames = new DesignationNames();
            DesignationNamesServices designationNamesServices = new DesignationNamesServices();
            designationNames=  designationNamesServices.getDesignationNamesID(designationName);

            setDesignation(designationNames.getId().replace("\" ",""));
            //setDesignation("5ddf6a1c4102f");
            setStaffingModel(
                    com.darwinbox.recruitment.objects.Designations.staffingModel.valueOf(body.get("StaffingModel")));
            setNumberOfPositions(body.get("numberOfPositions"));
            //setNumberOfPositions("5");
            setOverHiringAllowed(true);
            setCountNoticePeriod(false);
        }

        if(body.get("createType").contains("Specific")){

            DesignationNames designationNames = new DesignationNames();
            DesignationNamesServices designationNamesServices = new DesignationNamesServices();
            designationNames=  designationNamesServices.getDesignationNamesID(designationName);

            setDesignation(designationNames.getId().replace("\" ",""));

            department=departmentServices.getAllDepartments(department,body.get("DepartmentName"));
            setDepartment(department.getId());

            setNumberOfPositions(body.get("numberOfPositions"));
            setStaffingModel(
                    com.darwinbox.recruitment.objects.Designations.staffingModel.valueOf(body.get("StaffingModel")));
            setCountNoticePeriod(LeaveDeductionsBase.getFilter(body,"IsCountNoticePeriod","true"));


        }


    }

    /*Below methods are data for updating designation*/

    public Map<String,String> getDefaultForUpdateDesignation() {

        Map<String, String> body = new HashMap<>();

        body.put("UserDesignationsForm[job_code]",getJobCode());
        body.put("UserDesignationsForm[staffing_model]",getStaffingModel().s);
        body.put("UserDesignationsForm[designation_name_id]",getDesignation());
        body.put("UserDesignationsForm[department_id]",getDepartment());
        body.put("UserDesignationsForm[recruitment_hiring_leads]",getHiringLead());
        body.put("UserDesignationsForm[je_score]","");
        body.put("UserDesignationsForm[jd_id]","");
        body.put("UserDesignationsForm[functional_area]","");
        body.put("UserDesignationsForm[staffing_model]","");

        return body;
    }
    public void setDefaultForUpdateDesignation(String designationName) {

        DesignationNames designationNames = new DesignationNames();
        DesignationNamesServices designationNamesServices = new DesignationNamesServices();
        designationNames=  designationNamesServices.getDesignationNamesID(designationName);

        setDesignation(designationNames.getId().replace("\" ",""));


    }

        /*Below methods is for position staffing model selected designation page-1*/

    public Map<String,String> toMapPositionStageOne(){

        Map<String,String> body = new HashMap<>();

        Date date = new Date();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        date= dateTimeHelper.addDays(date,-7);
        String effectiveDate = dateTimeHelper.formatDateTo(date,"dd-MM-yyyy");

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        Object[] location;
        location = getOfficeLocations(id).values().toArray();

        Object[] empTypes = getEmployeeTypes().values().toArray();
        if (location.length == 0 || empTypes.length == 0) {
            log.error("ERROR: Unable to fetch " + ((location.length == 0) ? "locations" : "employee types"));
        }
        body.put("UserDesignationsPositionForm[no_of_positions]",getNumberOfPositions());
        body.put("UserDesignationsPositionForm[effective_date]",effectiveDate);
        body.put("UserDesignationsPositionForm[manager]","");
        body.put("UserDesignationsPositionForm[location]",(String) location[new Random().nextInt(location.length)]);
        body.put("UserDesignationsPositionForm[employee_type]",(String) empTypes[new Random().nextInt(empTypes.length)]);
        body.put("UserDesignationsPositionForm[need_to_hire]","1");
        body.put("UserDesignationsPositionForm[reason]","");
        body.put("UserDesignationsPositionForm[bugeted]","1");

/*UserDesignationsPositionForm[designation_id]: 5dd52804380b7
UserDesignationsPositionForm[no_of_positions]: 1
UserDesignationsPositionForm[effective_date]: 19-11-2019
UserDesignationsPositionForm[manager]:
UserDesignationsPositionForm[location]: 5af6c01323583
UserDesignationsPositionForm[employee_type]: 5a855efb55e60
UserDesignationsPositionForm[need_to_hire]: 1
UserDesignationsPositionForm[reason]:
UserDesignationsPositionForm[bugeted]: 1*/

         return body;
    }
    public void toObjectPositionStageOne(Map<String,String> body){

        setNumberOfPositions(body.get("numberOfPositions"));

    }
    /*Below methods is for position staffing model selected designation page-2*/

    public List<NameValuePair> toMapPositionStageTwo(){

        Map<String,String> body = new HashMap<>();

        Date date = new Date();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        date= dateTimeHelper.addDays(date,-7);
        String effectiveDate = dateTimeHelper.formatDateTo(date,"dd-MM-yyyy");

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        Object[] location;
        location = getOfficeLocations(id).values().toArray();

        Object[] empTypes = getEmployeeTypes().values().toArray();
        if (location.length == 0 || empTypes.length == 0) {
            log.error("ERROR: Unable to fetch " + ((location.length == 0) ? "locations" : "employee types"));
        }

        List<NameValuePair> list = new ArrayList<>();

        int count = 0;
        for (DesignationsPositionsLineWise LWbody : designationsPositionsLineWises) {
            list.addAll(list.size(), (LWbody.toMap(count)));
            count++;

        }

        /*body.put("PositionsSet[0][position_id]","test"); //put this field in test class
        body.put("PositionsSet[0][manager]","");
        body.put("PositionsSet[0][location]",(String) location[new Random().nextInt(location.length)]);
        body.put("PositionsSet[0][employee_type]",(String) empTypes[new Random().nextInt(empTypes.length)]);
        body.put("PositionsSet[0][need_to_hire]","1");
        body.put("PositionsSet[0][reason]","");
        body.put("PositionsSet[0][bugeted]","1");
        body.put("PositionsSet[0][effective_date]",effectiveDate);
        list.addAll(mapToFormData(body));
*/
         return list;
    }
    public void toObjectPositionStageTwo(){




    }
    public void add(DesignationsPositionsLineWise designationsPositionsLineWise){
        designationsPositionsLineWises.add(designationsPositionsLineWise);

    }

}
