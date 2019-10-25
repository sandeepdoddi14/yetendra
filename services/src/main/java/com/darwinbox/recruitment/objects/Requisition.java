package com.darwinbox.recruitment.objects;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.recruitment.services.RequisitionService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.*;

public class Requisition extends Services {

    private String companyName;
    private String designationAndDepartment;
    private String functionalArea;
    private String totalPositions;
    private String totalNewPositions;
    private String totalReplacementPositions;
    private List<String> replacementEmployees;

    private String employeeType;
    private String locations;
    private String recruitmentStartDate;
    private String expFrom;
    private String expTo;
    private String currency;
    private String minSalary;
    private String maxSalary;

    public enum yearsOrMonths{
        YEARS("Years"),
        MONTHS("Months");
        public final String s;
        yearsOrMonths(String s){
            this.s = s;
        }
    }
    public static yearsOrMonths yearsOrMonths;

    public static Requisition.yearsOrMonths getYearsOrMonths() {
        return yearsOrMonths;
    }

    public static void setYearsOrMonths(Requisition.yearsOrMonths yearsOrMonths) {
        Requisition.yearsOrMonths = yearsOrMonths;
    }

    private String hiringLead;
    private String skillsRequired;
    private String comments;
    private String assetRequirements;

    private String existingJob;
    public enum jobOpeningOptions{
        DUMMY,
        LINKTOEXISTINGJOB,
        CREATENEWJOBOPENING;
    }
    public static jobOpeningOptions jobOpeningOptions;
    public Requisition.jobOpeningOptions getJobOpeningOptions() {
        return jobOpeningOptions;
    }
    public  void setJobOpeningOptions(Requisition.jobOpeningOptions jobOpeningOptions) {
        Requisition.jobOpeningOptions = jobOpeningOptions;
    }

    public String getExistingJob() {
        return existingJob;
    }

    public void setExistingJob(String existingJob) {
        this.existingJob = existingJob;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignationAndDepartment() {
        return designationAndDepartment;
    }

    public void setDesignationAndDepartment(String designationAndDepartment) {
        this.designationAndDepartment = designationAndDepartment;
    }
    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }

    public String getTotalPositions() {
        return totalPositions;
    }

    public void setTotalPositions(String totalPositions) {
        this.totalPositions = totalPositions;
    }

    public String getTotalNewPositions() {
        return totalNewPositions;
    }

    public void setTotalNewPositions(String totalNewPositions) {
        this.totalNewPositions = totalNewPositions;
    }

    public String getTotalReplacementPositions() {
        return totalReplacementPositions;
    }

    public void setTotalReplacementPositions(String totalReplacementPositions) {
        this.totalReplacementPositions = totalReplacementPositions;
    }
    public List<String> getReplacementEmployees() {
        return replacementEmployees;
    }

    public void setReplacementEmployees(List<String> replacementEmployees) {
        this.replacementEmployees = replacementEmployees;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getRecruitmentStartDate() {
        return recruitmentStartDate;
    }

    public void setRecruitmentStartDate(String recruitmentStartDate) {
        this.recruitmentStartDate = recruitmentStartDate;
    }

    public String getExpFrom() {
        return expFrom;
    }

    public void setExpFrom(String expFrom) {
        this.expFrom = expFrom;
    }

    public String getExpTo() {
        return expTo;
    }

    public void setExpTo(String expTo) {
        this.expTo = expTo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(String minSalary) {
        this.minSalary = minSalary;
    }

    public String getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(String maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getHiringLead() {
        return hiringLead;
    }

    public void setHiringLead(String hiringLead) {
        this.hiringLead = hiringLead;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAssetRequirements() {
        return assetRequirements;
    }

    public void setAssetRequirements(String assetRequirements) {
        this.assetRequirements = assetRequirements;
    }

    public List<NameValuePair> toMap() {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> list = new ArrayList<>();

        body.put("yt0","APPLY");
        body.put("RequestRequisition[parent_company_id]",getCompanyName());
        body.put("RequestRequisition[designation]",getDesignationAndDepartment());
        body.put("RequestRequisition[functional_area]",getFunctionalArea());
        body.put("RequestRequisition[positions]",getTotalPositions());
        body.put("RequestRequisition[no_of_employees_new]",getTotalNewPositions());
        body.put("RequestRequisition[no_of_employees_replacement]",getTotalReplacementPositions());

       /* for (String getreplacedUsers  : getReplacementEmployees()) {
            list.add(new BasicNameValuePair("RequestRequisition[replaced_users]", getreplacedUsers));
        }*/

        body.put("RequestRequisition[employee_type]",getEmployeeType());
        body.put("RequestRequisition[office_locations][]",getLocations());
        body.put("RequestRequisition[recruitment_start_date]",getRecruitmentStartDate());
        body.put("RequestRequisition[experience_from]",getExpFrom());
        body.put("RequestRequisition[experience_to]",getExpTo());
        body.put("RequestRequisition[experience_yrs_month]",getYearsOrMonths().s);
        body.put("RequestRequisition[salary_currency]",getCurrency());
        body.put("RequestRequisition[salary_min]",getMinSalary());
        body.put("RequestRequisition[salary_max]",getMaxSalary());

        body.put("RequestRequisition[job_opening_option]",""+getJobOpeningOptions().ordinal());
        body.put("RequestRequisition[linked_to_job]",getExistingJob());

        body.put("RequestRequisition[hiring_lead]",getHiringLead());
        body.put("RequestRequisition[key_skills]",getSkillsRequired());
        body.put("RequestRequisition[comments]",getComments());

        list.addAll(mapToFormData(body));
        return list;
    }

    public void toObject(Map<String,String> body) {

        List<String> data = new ArrayList<>();

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        JSONObject obj = getDesignations(id);
        Object[] keys = obj.keySet().toArray();
        JSONObject kv = obj.getJSONObject((String) keys[new Random().nextInt(keys.length)]);
        Object[] designations = kv.keySet().toArray();

        Object[] location;
        location = getOfficeLocations(id).values().toArray();

        Object[] empTypes = getEmployeeTypes().values().toArray();
        if (location.length == 0 || empTypes.length == 0) {
            log.error("ERROR: Unable to fetch " + ((location.length == 0) ? "locations" : "employee types"));
        }

        setCompanyName(id);
        setDesignationAndDepartment((String) designations[new Random().nextInt(designations.length)]);
        //setFunctionalArea(body.get("FunctionalArea"));
        setTotalPositions(body.get("TotalPositions"));
        setTotalNewPositions(body.get("NewPositions"));
        setTotalReplacementPositions(body.get("ReplacedPositions"));
        //setReplacementEmployees(data);
        setEmployeeType((String) empTypes[new Random().nextInt(empTypes.length)]);
        setLocations((String) location[new Random().nextInt(location.length)]);
        Date date = new Date();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        setRecruitmentStartDate(dateTimeHelper.formatDateTo(dateTimeHelper.getNextDate(date),"dd-MM-yyyy"));
        setExpFrom(body.get("ExperienceFrom"));
        setExpTo(body.get("ExperienceTo"));
        setYearsOrMonths(yearsOrMonths.valueOf(body.get("YearsOrMonths")));
        setCurrency(body.get("currency"));
        setMinSalary(body.get("MinSalary"));
        setMaxSalary(body.get("MaxSalary"));
        setJobOpeningOptions(Requisition.jobOpeningOptions.valueOf(body.get("jobOpeningOption")));

        if(body.get("jobOpeningOption").equalsIgnoreCase("LINKTOEXISTINGJOB")){

            RequisitionService requisitionService = new RequisitionService();
            setExistingJob(requisitionService.searchActiveJobs());

        }

        setHiringLead(body.get("HiringLead"));
        setSkillsRequired(body.get("Skills"));
        setComments(body.get("comments"));
    }

    /*Below objects and methods are for Searching using text on requisition page*/

    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Map<String, String> toMapSearch() {

        Map<String, String> body = new HashMap<>();

        body.put("draw","1");
        body.put("columns[0][data]","requisition_id");
        body.put("columns[0][name]","");
        body.put("columns[0][searchable]","true");
        body.put("columns[0][orderable]","true");
        body.put("columns[0][search][value]","");
        body.put("columns[0][search][regex]","false");
        body.put("columns[1][data]","job");
        body.put("columns[1][name]","");
        body.put("columns[1][searchable]","true");
        body.put("columns[1][orderable]","true");
        body.put("columns[1][search][value]","");
        body.put("columns[1][search][regex]","false");
        body.put("columns[2][data]","department");
        body.put("columns[2][name]","");
        body.put("columns[2][searchable]","true");
        body.put("columns[2][orderable]","true");
        body.put("columns[2][search][value]","");
        body.put("columns[2][search][regex]","false");
        body.put("columns[3][data]","positions");
        body.put("columns[3][name]","");
        body.put("columns[3][searchable]","true");
        body.put("columns[3][orderable]","true");
        body.put("columns[3][search][value]","");
        body.put("columns[3][search][regex]","false");
        body.put("columns[4][data]","no_of_employees_new");
        body.put("columns[4][name]","");
        body.put("columns[4][searchable]","true");
        body.put("columns[4][orderable]","true");
        body.put("columns[4][search][value]","");
        body.put("columns[4][search][regex]","false");
        body.put("columns[5][data]","no_of_employees_replacement");
        body.put("columns[5][name]","");
        body.put("columns[5][searchable]","true");
        body.put("columns[5][orderable]","true");
        body.put("columns[5][search][value]","");
        body.put("columns[5][search][regex]","false");
        body.put("columns[6][data]","role");
        body.put("columns[6][name]","");
        body.put("columns[6][searchable]","true");
        body.put("columns[6][orderable]","false");
        body.put("columns[6][search][value]","");
        body.put("columns[6][search][regex]","false");
        body.put("columns[7][data]","status");
        body.put("columns[7][name]","");
        body.put("columns[7][searchable]","true");
        body.put("columns[7][orderable]","false");
        body.put("columns[7][search][value]","");
        body.put("columns[7][search][regex]","false");
        body.put("order[0][column]","department");
        body.put("order[0][dir]","asc");
        body.put("start","0");
        body.put("length","10");
        body.put("search[value]","");
        body.put("search[regex]","false");
        body.put("tab","3");

        body.put("searchText",getSearchText());

        return body;
    }
    public void toObjectSearch(String searchText) {

        setSearchText(searchText);
    }

    /*Below objects and methods are for searching using text
      filtered by Drafts jobs on jobs page*/

    public Map<String, String> toMapJobSearch() {

        Map<String, String> body = new HashMap<>();

        body.put("draw","1");
        body.put("columns[0][data]","internal_job_code");
        body.put("columns[0][name]","");
        body.put("columns[0][searchable]","true");
        body.put("columns[0][orderable]","true");
        body.put("columns[0][search][value]","");
        body.put("columns[0][search][regex]","false");

        body.put("columns[1][data]","job");
        body.put("columns[1][name]","");
        body.put("columns[1][searchable]","true");
        body.put("columns[1][orderable]","true");
        body.put("columns[1][search][value]","");
        body.put("columns[1][search][regex]","false");

        body.put("columns[2][data]","department");
        body.put("columns[2][name]","");
        body.put("columns[2][searchable]","true");
        body.put("columns[2][orderable]","true");
        body.put("columns[2][search][value]","");
        body.put("columns[2][search][regex]","false");

        body.put("columns[3][data]","active_positions");
        body.put("columns[3][name]","");
        body.put("columns[3][searchable]","true");
        body.put("columns[3][orderable]","true");
        body.put("columns[3][search][value]","");
        body.put("columns[3][search][regex]","false");

        body.put("columns[4][data]","open_positions");
        body.put("columns[4][name]","");
        body.put("columns[4][searchable]","true");
        body.put("columns[4][orderable]","true");
        body.put("columns[4][search][value]","");
        body.put("columns[4][search][regex]","false");

        body.put("columns[5][data]","offer_pending");
        body.put("columns[5][name]","");
        body.put("columns[5][searchable]","true");
        body.put("columns[5][orderable]","true");
        body.put("columns[5][search][value]","");
        body.put("columns[5][search][regex]","false");

        body.put("columns[6][data]","offer_accepted");
        body.put("columns[6][name]","");
        body.put("columns[6][searchable]","true");
        body.put("columns[6][orderable]","true");
        body.put("columns[6][search][value]","");
        body.put("columns[6][search][regex]","false");

        body.put("columns[7][data]","appliaction");
        body.put("columns[7][name]","");
        body.put("columns[7][searchable]","true");
        body.put("columns[7][orderable]","true");
        body.put("columns[7][search][value]","");
        body.put("columns[7][search][regex]","false");

        body.put("columns[8][data]","created_on");
        body.put("columns[8][name]","");
        body.put("columns[8][searchable]","true");
        body.put("columns[8][orderable]","true");
        body.put("columns[8][search][value]","");
        body.put("columns[8][search][regex]","false");

        body.put("columns[9][data]","updated_on");
        body.put("columns[9][name]","");
        body.put("columns[9][searchable]","true");
        body.put("columns[9][orderable]","true");
        body.put("columns[9][search][value]","");
        body.put("columns[9][search][regex]","false");

        body.put("columns[10][data]","jobaction");
        body.put("columns[10][name]","");
        body.put("columns[10][searchable]","true");
        body.put("columns[10][orderable]","false");
        body.put("columns[10][search][value]","");
        body.put("columns[10][search][regex]","false");
        body.put("order[0][column]","updated_on");
        body.put("order[0][dir]","desc");

        body.put("start","0");
        body.put("length","10");
        body.put("search[value]","");
        body.put("search[regex]","false");
        body.put("subCompany","ALL");
        body.put("tab","3");

        body.put("searchText",getSearchText());

        return body;
    }

    public void toObjectJobSearch(String searchText) {

        setSearchText(searchText);
    }

    /*Search call with filtered options group company and active jobs*/

    public Map<String, String> toMapActiveJobSearch() {

        Map<String, String> body = new HashMap<>();

        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);

        body.putAll(toMapJobSearch());
        body.put("subCompany",id);
        body.put("tab","ALL");
        setSearchText("");

        return body;
    }

    /*Below variables and method is to post a job, passing job ID as parameter*/

    private String jobID;
    private String hirLead;

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getHirLead() {
        return hirLead;
    }

    public void setHirLead(String hirLead) {
        this.hirLead = hirLead;
    }

    public Map<String, String> toMapPostJob(){

        Map<String, String> body = new HashMap<>();

        body.put("id",getJobID());
        body.put("RaiseRequisition[hiring_lead]",getHirLead());
        body.put("edit","1");
        body.put("RaiseRequisition[screening_team]","");
        body.put("RaiseRequisition[scheduling_team]","");
        body.put("RaiseRequisition[hiring_group]","");
        body.put("RaiseRequisition[cc_email_offer_letter]","");
        body.put("RaiseRequisition[view_rights_interviewer]","");
        return body;

    }
    public void  toObjectPostJob(String jobID, String hirLead) {

        setJobID(jobID);
        setHirLead(hirLead);

    }

    }
