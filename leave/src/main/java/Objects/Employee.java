package Objects;

import Service.DashboardServices;

import java.util.List;

public class Employee {

    private String firstName;
    private String lastName;
    private String userID;
    private String employeeID;
    private String candidateID;
    private String mongoID;
    private String emailID;
    private String doj;
    private String dob;
    private String designation;
    private String designationID;
    private String locationID;
    private String company;
    private String companyID;
    private String selfService;
    private String employeeType;
    private String employeeTypeID;
    private String gender;
    private String password;
    private String phpSessid;
    private String jobLevel;
    private Boolean isServingNoticePeriod=false;
    private String probation;
    private String departmentId="5c34d3481aeda";//hard coded

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }



    public Employee getL1Manager() {
        return l1Manager;
    }

    public void setL1Manager(Employee l1Manager) {
        this.l1Manager = l1Manager;
    }

    private Employee l1Manager=null;

    public List<String> getWeeklyOff() {
        return WeeklyOff;
    }

    public void setWeeklyOff(List<String> weeklyOff) {
        WeeklyOff = weeklyOff;
    }

    private List<String> WeeklyOff;//to consider multiple days

    public Boolean getServingNoticePeriod() {
        return isServingNoticePeriod;
    }

    public void setServingNoticePeriod(Boolean servingNoticePeriod) {
        isServingNoticePeriod = servingNoticePeriod;
    }

    public String getProbation() {
        return probation;
    }

    public void setProbation(String probation) {
        this.probation = probation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationID() {
        return designationID;
    }

    public void setDesignationID(String designationID) {
        this.designationID = designationID;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getSelfService() {
        return selfService;
    }

    public void setSelfService(String selfService) {
        this.selfService = selfService;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployeeTypeID() {
        return employeeTypeID;
    }

    public void setEmployeeTypeID(String employeeTypeID) {
        this.employeeTypeID = employeeTypeID;
    }

    public String getMongoID() {
        return mongoID;
    }

    public void setMongoID(String mongoID) {
        this.mongoID = mongoID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhpSessid() {
        if ( this.phpSessid == null)
           this.phpSessid = new DashboardServices().doLogin(this.getEmailID(), this.getPassword());
        return this.phpSessid;
    }

    public void setPhpSessid(String phpSessid) {
        this.phpSessid = phpSessid;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

}
