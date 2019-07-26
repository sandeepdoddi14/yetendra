package com.darwinbox.attendance.services;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.attendance.objects.EmployeeProfileEmailSettings;
import com.darwinbox.framework.uiautomation.base.TestBase;
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

    public String deletePendingEmployee(String userID) {
        String reqURL = getData("@@url") + "/employee/deleteInactive";
        List<NameValuePair> formData = new ArrayList<>();
        HashMap headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("referer", getData("@@url") + "/employee/list/users/pending");
        formData.add(new BasicNameValuePair("user", userID));
        return doPost(reqURL, headers, formData);
    }

    public void addEmployee(Employee employee) {
        String reqURL = getData("@@url") + "/employee/add";
        List<NameValuePair> formData = new ArrayList<>();
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
        doPost(reqURL, null, formData);
    }

    public Employee createAnEmployee(boolean isParent) {

        Employee employee = generateAnEmployee(isParent, getData("@@group"));
        log.info("INFO: Employee object : " + new JSONObject(employee));
        employee.setCompanyID("main");

        if (!isParent)
            employee.setCompanyID(getGroupCompanyIds().get(getData("@@group")));
        try{
            Thread.sleep(2*1000);
            } catch (Exception e){}

        Map<String, String> userAndEmpIds = getUserIDAndEmpID(employee.getCandidateID());
        employee.setUserID(userAndEmpIds.get("userID"));
        employee.setEmployeeID(userAndEmpIds.get("employeeID"));
        Map<String, String> ma = getEmpIDAndMongoID(employee.getEmployeeID());
        employee.setMongoID(ma.get("userMongoID"));
        employee.setPassword("123456");
        return employee;
    }


        public Map<String, String> getUserIDAndEmpID(String searchTerm) {
            String url = getData("@@url") + "/dashboard/GetEmployeesUserId?term=" + searchTerm;
            Map headers = new HashMap();
            headers.put("X-Requested-With", "XMLHttpRequest");
            JSONArray objResponse = new JSONArray(doGet(url, headers));
            Map<String, String> empDetails = new HashMap<>();
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
            Map<String, String> empDetails = new HashMap<>();
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


        public List<Employee> generateRandomEmployees(boolean isParent, String gcName, int count, boolean isEmpObjectsReqd) {

            Employee employee = new Employee();
            employee.setCompany(gcName);
            List<Employee> empIds = new ArrayList<>();
            String id;
            if (isParent) {
                id = "main";
            } else {
                id = getGroupCompanyIds().get(gcName);
                if (id.isEmpty()) {
                    log.error("ERROR: Unable to fetch Group company ID for - " + gcName);
                    return null;
                }
            }

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
            JSONObject obj = getDesignations(id);
            if (obj == null) {
                log.error("ERROR: Unable to fetch Designations");
                return null;
            }
            Object[] keys = obj.keySet().toArray();

            for (int i = 0; i < count; i++) {

                Faker f = new Faker();

                Calendar gc = Calendar.getInstance();
                gc.add(Calendar.MONTH, -3);

                String doj = String.format("%d-%02d-%02d", gc.get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1, new Random().nextInt(1) + 5);

                String empID = RandomStringUtils.randomAlphabetic(1).toUpperCase() + String.valueOf(new Date().getTime());
                JSONObject kv = obj.getJSONObject((String) keys[new Random().nextInt(keys.length)]);
                Object[] designations = kv.keySet().toArray();

                employee.setFirstName(f.name().firstName());
                employee.setLastName(f.name().lastName());
                employee.setDoj(doj);
                employee.setDob(new DateTimeHelper().getRandomDateBetween(1970, 1990));
                employee.setEmailID(empID + "@yopmail.com");
                employee.setGender(new Random().nextBoolean() ? "male" : "female");
                employee.setCompanyID(id.equals("main") ? "" : id);
                employee.setLocationID((String) locations[new Random().nextInt(locations.length)]);
                employee.setDesignationID((String) designations[new Random().nextInt(designations.length)]);
                employee.setEmployeeTypeID((String) empTypes[new Random().nextInt(empTypes.length)]);
                employee.setSelfService("1");
                employee.setCandidateID(empID);
                addEmployee(employee);
                if (isEmpObjectsReqd) {
                    empIds.add(employee);
                }
            }
            return empIds;
        }

        public Employee generateAnEmployee(boolean isParent, String gcName) {
            List<Employee> emps = generateRandomEmployees(isParent, gcName, 1, true);

            String companyID = "";
            Employee emp =  emps.get(0);


                String userID = getUserIDOfPendingEmployees(emp, isParent, companyID);
                activateEmployee(userID);

                return emp;

        }

        public String getUserIDOfPendingEmployees(Employee employee, boolean isParentCompany, String companyID) {

            String reqURL = getData("@@url") + "/dashboard/GetEmployeesUserIdAdmin?term=" + employee.getCandidateID() + "&grpcompsend=" + (isParentCompany ? "" : companyID);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("X-Requested-With", "XMLHttpRequest");
            headers.put("referer", getData("@@url") + "/employee/list/users/pending");
            String resBody = doGet(reqURL, headers);
            log.info("Response: " + resBody);
            JSONObject response = new JSONObject(resBody);
            if (response == null) {
                throw new RuntimeException("ERROR: Unable to find Pending Employee with CandidateID: " + employee.getCandidateID());
            }
            JSONObject ob = (JSONObject) response.get((String)(response.keySet().toArray())[0]);
            String userID = ob.getString("id").replace("NAME_", "");
            if (userID == null || userID.isEmpty()) {
                throw new RuntimeException("ERROR: Unable to find UserID with CandidateID: " + employee.getCandidateID());
            }
            return userID;
        }


    public void activateEmployee(String userID) {
        String actReqURL = getData("@@url") + "/employee/activateforefully";
        List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("user", userID));
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject actResponse = new JSONObject(doPost(actReqURL, headers, formData));
        if (!(actResponse != null && actResponse.getString("status").equals("success"))) {
            throw new RuntimeException("ERROR: Unable to Activate employee");
        }
        resetPassword(userID, "123456");
    }

    public boolean resetPassword(String userID, String password) {
        String actReqURL = data.get("@@url") + "/employee/resetPassword";
        List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("User[id]", userID));
        formData.add(new BasicNameValuePair("UserChangePassword[newPassword]", password));
        formData.add(new BasicNameValuePair("UserChangePassword[verifyNewPassword]", password));
        formData.add(new BasicNameValuePair("UserChangePassword[notify_user]", "0"));
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        try {
            JSONObject actResponse = new JSONObject(doPost(actReqURL, headers, formData));
            if (actResponse != null && actResponse.getString("status").equals("success")) {
                log.info("Success: " + actResponse.getString("message"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateProfileSettings(Employee employee,Map<String, String> data) {

        String url = TestBase.data.get("@@url") + "/employee/settings";

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", employee.getPhpSessid());

        EmployeeProfileEmailSettings emp_prof = new EmployeeProfileEmailSettings();
        emp_prof.toObject(data);

        doPost(url, headers, (emp_prof.toListObject()));

    }
}