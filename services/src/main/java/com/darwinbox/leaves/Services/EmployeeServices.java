package com.darwinbox.leaves.Services;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmployeeServices extends Services {

    public boolean resetPassword(Employee employee, String password) {
        String actReqURL = data.get("@@url") + "/employee/resetPassword";
        List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("User[id]", employee.getUserID()));
        formData.add(new BasicNameValuePair("UserChangePassword[newPassword]", password));
        formData.add(new BasicNameValuePair("UserChangePassword[verifyNewPassword]", password));
        formData.add(new BasicNameValuePair("UserChangePassword[notify_user]", "0"));
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        try {
            JSONObject actResponse = new JSONObject(doPost(actReqURL, headers, formData));
            if (actResponse != null && actResponse.getString("status").equals("success")) {
                log.info("Success: " + actResponse.getString("message"));
                employee.setPassword(password);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method
     *
     * @param userID
     * @return Response of Delete call
     */
    public String deletePendingEmployee(String userID) {
        String reqURL = getData("@@url") + "/employee/deleteInactive";
        List<NameValuePair> formData = new ArrayList<NameValuePair>();
        HashMap headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("referer", getData("@@url") + "/employee/list/users/pending");
        formData.add(new BasicNameValuePair("user", userID));
        return doPost(reqURL, headers, formData);
    }

    public void addEmployee(Employee employee) {
        String reqURL = getData("@@url") + "/employee/add";
        List<NameValuePair> formData = new ArrayList<NameValuePair>();
        formData.add(new BasicNameValuePair("yt0", "Add Employee"));
        formData.add(new BasicNameValuePair("UserAddForm[selfservice]", employee.getSelfService()));
        formData.add(new BasicNameValuePair("UserAddForm[firstname]", employee.getFirstName()));
        formData.add(new BasicNameValuePair("UserAddForm[lastname]", employee.getLastName()));
        formData.add(new BasicNameValuePair("UserAddForm[dob]", employee.getDob()));
        formData.add(new BasicNameValuePair("UserAddForm[doj]", employee.getDoj()));
        formData.add(new BasicNameValuePair("UserAddForm[email]", employee.getEmailID()));
        formData.add(new BasicNameValuePair("UserAddForm[gender]", employee.getGender().equals("male") ? "1" : "2"));
        formData.add(new BasicNameValuePair("UserAddForm[parent_company_id]", employee.getCompanyID()));
        formData.add(new BasicNameValuePair("UserAddForm[officelocation]", employee.getLocationID()));
        formData.add(new BasicNameValuePair("UserAddForm[designation]", employee.getDesignationID()));
        formData.add(new BasicNameValuePair("UserAddForm[type]", employee.getEmployeeTypeID()));
        formData.add(new BasicNameValuePair("UserAddForm[candidate_id]", employee.getCandidateID()));
        formData.add(new BasicNameValuePair("UserAddForm[job_level]",employee.getJobLevel()));

        if(employee.getProbation()!=null){
            formData.add(new BasicNameValuePair("UserAddForm[probation_period]",employee.getProbation()));
        }
        doPost(reqURL, null, formData);
    }

    public void generateRandomEmployees(String isParent, String gcName, int count) {
        generateRandomEmployees(isParent, gcName, count, false,"random","no");
    }

    public Employee createAnEmployee() {
        Employee employee = generateAnEmployee(getData("isParent"), getData("companyName"),"random","no");
        log.info("INFO: Employee object : " + new JSONObject(employee));
        employee.setCompanyID("main");
        if (!getData("isParent").equalsIgnoreCase("yes"))
            employee.setCompanyID(getGroupCompanyIds().get(getData("CompanyName")));
        Map<String, String> userAndEmpIds = getUserIDAndEmpID(employee.getCandidateID());
        employee.setUserID(userAndEmpIds.get("userID"));
        employee.setEmployeeID(userAndEmpIds.get("employeeID"));
        Map<String, String> ma = getEmpIDAndMongoID(employee.getEmployeeID());
        employee.setMongoID(ma.get("userMongoID"));

        return employee;
    }

    public Map<String, String> getUserIDAndEmpID(String searchTerm) {
        String url = getData("@@url") + "/dashboard/GetEmployeesUserId?term=" + searchTerm;
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONArray objResponse = new JSONArray(doGet(url, headers));
        Map<String, String> empDetails = new HashMap<String, String>();
        empDetails.put("employeeID", "");
        if (objResponse != null) {
            empDetails.put("userID", objResponse.getJSONObject(0).getJSONArray("id").getString(0));
            String value = objResponse.getJSONObject(0).getString("value");
            Matcher m = Pattern.compile("\\(\\w+\\)").matcher(value);
            if (m.find()) {
                empDetails.put("employeeID", m.group(0).replaceAll("[()]", ""));
            }
        } else {
            log.error(objResponse);
            throw new RuntimeException("ERROR: Unable to get Employee ids");
        }
        return empDetails;
    }

    public Map<String, String> getEmpIDAndMongoID(String searchTerm) {
        String url = getData("@@url") + "/dashboard/GetEmployeesId?term=" + searchTerm;
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONArray objResponse = new JSONArray(doGet(url, headers));
        Map<String, String> empDetails = new HashMap<String, String>();
        empDetails.put("employeeID", "");
        if (objResponse != null) {
            empDetails.put("userMongoID", objResponse.getJSONObject(0).getJSONArray("id").getString(0));
            String value = objResponse.getJSONObject(0).getString("value");
            Matcher m = Pattern.compile("\\(\\w+\\)").matcher(value);
            if (m.find()) {
                empDetails.put("employeeID", m.group(0).replaceAll("[()]", ""));
            }
        } else {
            log.error(objResponse);
            throw new RuntimeException("ERROR: Unable to get Employee ids");
        }
        return empDetails;
    }

    /**
     * Generate employees using data fetched from specified company
     *
     * @param gcName   name of the company for which employees to be created
     * @param isParent true if it is a parent company else false
     * @param count    no of employees to be created
     * @return list of employees that are generated
     */
    public List<Employee> generateRandomEmployees(String isParent, String gcName, int count, boolean isEmpObjectsReqd, String dateOfJoining, String probation) {

        Employee employee = new Employee();
        employee.setCompany(gcName);
        List<Employee> empIds = new ArrayList<Employee>();
        String id;
        if (isParent.equalsIgnoreCase("yes")) {
            id = "main";
        } else {
            id = getGroupCompanyIds().get(gcName);
            if (id.isEmpty()) {
                log.error("ERROR: Unable to fetch Group company ID for - " + gcName);
                return null;
            }
        }
        Faker f = new Faker();

        HashMap<String, String> locMap = getOfficeLocations(id);
        Object[] locations;
        if (locMap == null) {
            log.warn("Might need cron run for location");
            locations = getOffices(gcName).values().toArray();
        } else {
            locations = getOfficeLocations(id).values().toArray();
        }
        HashMap<String,String> empTypes = getEmployeeTypes();
        if (locations.length == 0 || empTypes.keySet().size() == 0) {
            log.error("ERROR: Unable to fetch " + ((locations.length == 0) ? "locations" : "employee types"));
            return null;
        }

        Object[] jobLevelIDS=getJobLevelIDS().values().toArray();
        if (jobLevelIDS.length == 0) {
            log.error("ERROR: Unable to fetch jobLevelIDS" );
            return null;
        }
        JSONObject obj = getDesignations(id);
        if (obj == null) {
            log.error("ERROR: Unable to fetch Designations");
            return null;
        }
        Object[] keys = obj.keySet().toArray();
        for (int i = 0; i < count; i++) {

            Calendar gc = Calendar.getInstance();
//            gc.add(Calendar.DAY_OF_YEAR, - (new Random().nextInt(150) + 90));
//            String doj = String.format("%d-%02d-%02d", gc.get(Calendar.YEAR), (gc.get(Calendar.MONTH) + 1), new Random().nextInt(25) + 1);

            gc.add(Calendar.MONTH, -2);
            String doj;
            if(dateOfJoining.equalsIgnoreCase("random"))
                doj= String.format("%d-%02d-%02d", gc.get(Calendar.YEAR), (gc.get(Calendar.MONTH) + 1), new Random().nextInt(15) + 10);
            else
                doj=dateOfJoining;
            String empID = RandomStringUtils.randomAlphabetic(1).toUpperCase() + new Date().getTime();
            JSONObject kv = obj.getJSONObject((String) keys[new Random().nextInt(keys.length)]);
            Object[] designations = kv.keySet().toArray();

            employee.setFirstName(f.name().firstName());
            //Don't change LastName, made as Unique value, easy to fetch employee details
//            employee.setLastName(empID);
            employee.setLastName(f.name().lastName());
            employee.setDoj(doj);
            employee.setDob(new DateTimeHelper().getRandomDateBetween(1970, 1990));
            employee.setEmailID(empID + "@yopmail.com");
            employee.setGender(new Random().nextBoolean() ? "male" : "female");
            employee.setCompanyID(id.equals("main") ? "" : id);
            employee.setLocationID((String) locations[new Random().nextInt(locations.length)]);
            employee.setDesignationID((String) designations[new Random().nextInt(designations.length)]);
            employee.setEmployeeTypeID((String) empTypes.get("Full Time"));
            employee.setSelfService("1");
            employee.setCandidateID(empID);
            employee.setJobLevel((String) jobLevelIDS[new Random().nextInt(jobLevelIDS.length)]);
            if(probation!=null || probation!="no"){
                employee.setProbation(probation);
            }
            addEmployee(employee);
            if (isEmpObjectsReqd) {
                empIds.add(employee);
            }
        }
        return empIds;
    }


    public List<Employee> generateRandomFullTimeEmployees(String isParent, String gcName, int count, boolean isEmpObjectsReqd, String dateOfJoining, String probation) {

        Employee employee = new Employee();
        employee.setCompany(gcName);
        List<Employee> empIds = new ArrayList<Employee>();
        String id;
        if (isParent.equalsIgnoreCase("yes")) {
            id = "main";
        } else {
            id = getGroupCompanyIds().get(gcName);
            if (id.isEmpty()) {
                log.error("ERROR: Unable to fetch Group company ID for - " + gcName);
                return null;
            }
        }
        Faker f = new Faker();

        HashMap<String, String> locMap = getOfficeLocations(id);
        Object[] locations;
        if (locMap == null) {
            log.warn("Might need cron run for location");
            locations = getOffices(gcName).values().toArray();
        } else {
            locations = getOfficeLocations(id).values().toArray();
        }
        Object[] empTypes = getEmployeeTypes().values().toArray();
        if (locations.length == 0 || empTypes.length == 0) {
            log.error("ERROR: Unable to fetch " + ((locations.length == 0) ? "locations" : "employee types"));
            return null;
        }

        Object[] jobLevelIDS=getJobLevelIDS().values().toArray();
        if (jobLevelIDS.length == 0) {
            log.error("ERROR: Unable to fetch jobLevelIDS" );
            return null;
        }
        JSONObject obj = getDesignations(id);
        if (obj == null) {
            log.error("ERROR: Unable to fetch Designations");
            return null;
        }
        Object[] keys = obj.keySet().toArray();
        for (int i = 0; i < count; i++) {

            Calendar gc = Calendar.getInstance();
//            gc.add(Calendar.DAY_OF_YEAR, - (new Random().nextInt(150) + 90));
//            String doj = String.format("%d-%02d-%02d", gc.get(Calendar.YEAR), (gc.get(Calendar.MONTH) + 1), new Random().nextInt(25) + 1);

            gc.add(Calendar.MONTH, -2);
            String doj;
            if(dateOfJoining.equalsIgnoreCase("random"))
                doj= String.format("%d-%02d-%02d", gc.get(Calendar.YEAR), (gc.get(Calendar.MONTH) + 1), new Random().nextInt(15) + 10);
            else
                doj=dateOfJoining;
            String empID = RandomStringUtils.randomAlphabetic(1).toUpperCase() + new Date().getTime();
            JSONObject kv = obj.getJSONObject((String) keys[new Random().nextInt(keys.length)]);
            Object[] designations = kv.keySet().toArray();

            employee.setFirstName(f.name().firstName());
            //Don't change LastName, made as Unique value, easy to fetch employee details
//            employee.setLastName(empID);
            employee.setLastName(f.name().lastName());
            employee.setDoj(doj);
            employee.setDob(new DateTimeHelper().getRandomDateBetween(1970, 1990));
            employee.setEmailID(empID + "@yopmail.com");
            employee.setGender(new Random().nextBoolean() ? "male" : "female");
            employee.setCompanyID(id.equals("main") ? "" : id);
            employee.setLocationID((String) locations[new Random().nextInt(locations.length)]);
            employee.setDesignationID((String) designations[new Random().nextInt(designations.length)]);
            employee.setEmployeeTypeID((String) empTypes[0]);
            employee.setSelfService("1");
            employee.setCandidateID(empID);
            employee.setJobLevel((String) jobLevelIDS[new Random().nextInt(jobLevelIDS.length)]);
            if(probation!=null || probation!="no"){
                employee.setProbation(probation);
            }
            addEmployee(employee);
            if (isEmpObjectsReqd) {
                empIds.add(employee);
            }
        }
        return empIds;
    }

    public Employee
    generateAnEmployee(String isParent, String gcName, String dateOfJoining, String probation) {
        List<Employee> emps = generateRandomEmployees(isParent, gcName, 1, true,dateOfJoining,probation);
        if (emps.isEmpty()) {
            throw new RuntimeException("Unable to create an employee");
        }
        String companyID = "";
        if (!isParent.equalsIgnoreCase("yes")) {
            companyID = getGroupCompanyIds().get(gcName);
            if (companyID.isEmpty() || companyID == null) {
                throw new RuntimeException("Unable to create an employee");
            }
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        String userID = getUserIDOfPendingEmployees(emps.get(0), isParent, companyID);
        emps.get(0).setUserID(userID);

        activateEmployee(userID);

        Map<String, String> userAndEmpIds = getUserIDAndEmpID(emps.get(0).getCandidateID());
        emps.get(0).setUserID(userAndEmpIds.get("userID"));
        emps.get(0).setEmployeeID(userAndEmpIds.get("employeeID"));

        Map<String, String> ma = getEmpIDAndMongoID(emps.get(0).getEmployeeID());
        emps.get(0).setMongoID(ma.get("userMongoID"));

        resetPassword(emps.get(0),"123456Aa!");

        return emps.get(0);
    }

    public Employee generateAnFullTimeEmployee(String isParent, String gcName, String dateOfJoining, String probation) {
        List<Employee> emps = generateRandomFullTimeEmployees(isParent, gcName, 1, true,dateOfJoining,probation);
        if (emps.isEmpty()) {
            throw new RuntimeException("Unable to create an employee");
        }
        String companyID = "";
        if (!isParent.equalsIgnoreCase("yes")) {
            companyID = getGroupCompanyIds().get(gcName);
            if (companyID.isEmpty() || companyID == null) {
                throw new RuntimeException("Unable to create an employee");
            }
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        String userID = getUserIDOfPendingEmployees(emps.get(0), isParent, companyID);
        emps.get(0).setUserID(userID);

        activateEmployee(userID);

        Map<String, String> userAndEmpIds = getUserIDAndEmpID(emps.get(0).getCandidateID());
        emps.get(0).setUserID(userAndEmpIds.get("userID"));
        emps.get(0).setEmployeeID(userAndEmpIds.get("employeeID"));

        Map<String, String> ma = getEmpIDAndMongoID(emps.get(0).getEmployeeID());
        emps.get(0).setMongoID(ma.get("userMongoID"));

        resetPassword(emps.get(0),"123456Aa!");

        return emps.get(0);
    }


    public String getUserIDOfPendingEmployees(Employee employee, String isParentCompany, String companyID) {

        String reqURL = getData("@@url") + "/dashboard/GetEmployeesUserIdAdmin?term=" + employee.getCandidateID() + "&grpcompsend=" + (isParentCompany.equalsIgnoreCase("yes") ? "" : companyID);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("referer", getData("@@url") + "/employee/list/users/pending");
        String resBody = doGet(reqURL, headers);
        log.info("Response: " + resBody);
        Reporter("Info -- Employee "+employee.getFirstName()+"  created Successfully","Info");
        JSONObject response;
        try{
            response = new JSONObject(resBody);
        }
        catch (Exception e){
            throw new RuntimeException("ERROR: Unable to find Pending Employee with CandidateID: " + employee.getCandidateID());
        }
        if (response == null) {
            throw new RuntimeException("ERROR: Unable to find Pending Employee with CandidateID: " + employee.getCandidateID());
        }
        JSONObject ob = (JSONObject) response.get((String) (response.keySet().toArray())[0]);
        String userID = ob.getString("id").replace("NAME_", "");
        if (userID == null || userID.isEmpty()) {
            throw new RuntimeException("ERROR: Unable to find UserID with CandidateID: " + employee.getCandidateID());
        }
        return userID;
    }


    public void activateEmployee(String userID) {
        String actReqURL = getData("@@url") + "/employee/activateforefully";
        List<NameValuePair> formData = new ArrayList<NameValuePair>();
        formData.add(new BasicNameValuePair("user", userID));
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject actResponse = new JSONObject(doPost(actReqURL, headers, formData));
        if (!(actResponse != null && actResponse.getString("status").equals("success"))) {
            throw new RuntimeException("ERROR: Unable to Activate employee");
        }
       /* if((actResponse != null && actResponse.getString("status").equals("success"))){
            Reporter("Info : Employee Activated Successfully","Info");
        }*/
    }


    public String getEmployeeId(String fName) {
        String url = data.get("@@url") + "/dashboard/GetEmployeesId?term=" + fName.toLowerCase();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        return response;
    }

    public HashMap<String,String> getGenders(){
        HashMap<String,String> genders=new HashMap<String,String>();
        genders.put("For All","0");
        genders.put("male","1");
        genders.put("female","2");
        genders.put("No Disclosure Only","3");
        genders.put("others","other");
        return  genders;
    }

    /*
    Bulk Action Call
    Update Weekly Off for an Employee
     */
    public String  createWeeklyOffForAnEmp(String userId,String weeklyOffId){
        String url=data.get("@@url")+ UtilityHelper.getProperty("ServiceUrls","createWeeklyOffForAEmployee");
        HashMap<String,String> requestBody=new HashMap<>();
        requestBody.put("UserWeeklyOffForm[user_id][]",userId);
        requestBody.put("UserWeeklyOffForm[weekly_off_id]",weeklyOffId);
        //HashMap<String,String> defaultBody=createWeeklyOffDefaultBody();
        //defaultBody.putAll(requestBody);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        return doPost(url,headers,mapToFormData(requestBody));

    }

    public HashMap<String,String> createWeeklyOffDefaultBody(){
        HashMap<String,String> defaultBody=new HashMap<>();
        defaultBody.put("UserWeeklyOffForm[user_id][]","");
        defaultBody.put("UserWeeklyOffForm[weekly_off_id]","");

        return defaultBody;
    }

    public String addUserEmployment(String userMongoID,String empType,String empTypeId,String effectiveDate){

        String url=data.get("@@url")+ UtilityHelper.getProperty("ServiceUrls","addUserEmployement");


        HashMap<String,String> requestBody=addUserEmployementDefaultBody();

        requestBody.put("UserMongo[id]",userMongoID);
        requestBody.put("UserEmployeementForm[type]",empType);
        requestBody.put("UserEmployeementForm[employee_type_id]",empTypeId);
        requestBody.put("UserEmployeementForm[from_date]",effectiveDate);


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        return doPost(url,headers,mapToFormData(requestBody));

    }

    public HashMap<String,String>  addUserEmployementDefaultBody(){

        HashMap<String,String> defaultBody = new HashMap<>();
        defaultBody.put("UserMongo[id]","");
        defaultBody.put("UserEmployeementForm[type]","");
        defaultBody.put("UserEmployeementForm[type_name]","employee_type_id");
        defaultBody.put("UserEmployeementForm[employee_type_id]","");
        defaultBody.put("Events[event]","");
        defaultBody.put("UserEmployeementForm[from_date]","");

        return  defaultBody;

    }


}