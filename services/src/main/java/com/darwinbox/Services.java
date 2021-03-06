package com.darwinbox;

import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.testng.Assert;

import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Services extends TestBase {

    public String getCurrentInstanceGroupCompany(){
        return data.get("@@url").replaceAll(".qa.darwinbox.io","").replaceAll("https://","");
    }

    public HashMap<String, HashMap<String, String>> getProbations() {
        String url = data.get("@@url") + "/settings/getProbation";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");

        int i = 0;
        HashMap<String, HashMap<String, String>> probationInfo = new HashMap<>();
        while (i < arr.length()) {
            String name = arr.getJSONArray(i).get(0).toString();
            String value = arr.getJSONArray(i).get(1).toString();
            String id = arr.getJSONArray(i).get(2).toString().substring(7, 20);
            HashMap<String, String> ids = new HashMap();
            ids.put(value, id);
            probationInfo.put(name, ids);
            i++;
        }
        return probationInfo;
    }




    public Map<String,String> createWeeklyOffDeafultBody() {
        Map<String, String> defaultBody = new HashMap<>();

        defaultBody.put("WeeklyOffForm[weekly_off_name]", "");
        defaultBody.put("WeeklyOffForm[description]", "automation generated weekly off");
        defaultBody.put("WeeklyOffForm[non_working_days][day][6]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][6]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][day][0]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][0]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][day][1]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][1]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][day][2]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][2]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][day][3]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][3]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][day][4]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][4]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][day][5]", "0");
        defaultBody.put("WeeklyOffForm[non_working_days][frequency][5]", "0");
        defaultBody.put("AttendancePolicyForm[change_date]", "0");

        return defaultBody;
    }


    public String createWeeklyOff(String days) {

        String url = data.get("@@url") + UtilityHelper.getProperty("ServiceUrls", "createWeeklyOff");

        Map<String, String> deafultBody = createWeeklyOffDeafultBody();


        Map<String, String> request = new HashMap<>();
        for (String day : days.split(",")) {
            if (day.equalsIgnoreCase("monday")) {
                request.put("WeeklyOffForm[non_working_days][day][1]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][1]", "0");

            }
            if (day.equalsIgnoreCase("tuesday")) {
                request.put("WeeklyOffForm[non_working_days][day][2]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][2]", "0");

            }
            if (day.equalsIgnoreCase("wednesday")) {
                request.put("WeeklyOffForm[non_working_days][day][3]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][3]", "0");

            }
            if (day.equalsIgnoreCase("thursday")) {
                request.put("WeeklyOffForm[non_working_days][day][4]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][4]", "0");

            }
            if (day.equalsIgnoreCase("friday")) {
                request.put("WeeklyOffForm[non_working_days][day][5]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][5]", "0");

            }
            if (day.equalsIgnoreCase("saturday")) {
                request.put("WeeklyOffForm[non_working_days][day][6]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][6]", "0");

            }
            if (day.equalsIgnoreCase("sunday")) {
                request.put("WeeklyOffForm[non_working_days][day][0]", "1");
                request.put("WeeklyOffForm[non_working_days][frequency][0]", "0");

            }
        }
        request.put("WeeklyOffForm[weekly_off_name]",days.replace(",","And"));

        deafultBody.putAll(request);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        return doPost(url, headers, mapToFormData(deafultBody));
    }


    public Map<String,String> getWeeklyOFFlist(){
        String url=data.get("@@url")+UtilityHelper.getProperty("ServiceUrls","getWeeklyOffList");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response=doGet(url,headers);
        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            ids.put(arr.getJSONArray(i).getString(0).split("/a>")[1],
                    arr.getJSONArray(i).getString(2).substring(7,20));
            i++;
        }

        return ids;
    }

    /*
  get job level info
  @@returns String--> JobLevel
            Hashmap-->Grade,JobLevelId
   */
    public HashMap<String, HashMap<String, String>> getJobLevel() {
        String url = data.get("@@url") + "/settings/getjoblevel";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");

        int i = 0;
        HashMap<String, HashMap<String, String>> jobLevelInfo = new HashMap<>();
        while (i < arr.length()) {
            String jobLevel = arr.getJSONArray(i).get(0).toString();
            String grade = arr.getJSONArray(i).get(1).toString();
            String jobLevelId = arr.getJSONArray(i).get(2).toString().substring(7, 20);
            HashMap<String, String> ids = new HashMap();
            ids.put(grade, jobLevel);
            jobLevelInfo.put(jobLevelId, ids);
            i++;
        }
        return jobLevelInfo;
    }



    public String doGet(String url, Map<String, String> headers) {

        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            if (headers == null)
                headers = new HashMap<>();

            headers.putIfAbsent("Cookie",getCookies());

            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }

            HttpResponse result = httpClient.execute(request);
            return EntityUtils.toString(result.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doPost(String url, Map<String, String> headers, List<NameValuePair> formData) {

        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(60 * 1000)
                    .setSocketTimeout(3* 60 * 1000)
                    .build();
            if (headers != null) {
                if (headers.get("Cookie") == null) {
                    headers.put("Cookie", getCookies());
                }
            } else {
                headers = new HashMap<>();
                headers.put("Cookie", getCookies());
            }

            formData.add(new BasicNameValuePair(getCookies().toString().split(";")[1].split("=")[0],
                    getCookies().toString().split(";")[1].split("=")[1]));
            return customPost(requestConfig, url, headers,formData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }


    public String customPost(RequestConfig requestConfig, String url, Map<String, String> headers, List<NameValuePair> formData) {

        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpPost request = new HttpPost(url);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }
            request.setEntity(new UrlEncodedFormEntity(formData));
            log.debug("URL " + url);
            log.debug("Request body: " + formData);
            HttpResponse result = httpClient.execute(request);
            /*if(result.getStatusLine().getStatusCode()==400) {
                Assert.assertTrue(false,"Getting 400 Code for "+url);
            }
            if(result.getStatusLine().getStatusCode()==200) {
                Reporter("Url -->"+url,"info");

                Reporter("Body -->"+formData.toString(),"info");
                Reporter("Raw Code -->"+result.getStatusLine().getStatusCode(),"info");

                Assert.assertTrue(true,"Getting 200 Code for "+url);
            }*/

            return EntityUtils.toString(result.getEntity(), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCookies() {
        Set<Cookie> cookieSet = driver.manage().getCookies();
        String cookie = "";
        for (Cookie c : cookieSet) {
            String name = c.getName();
            String val = c.getValue();
            cookie = cookie + ";" + name + "=" + val;
        }
        return cookie;
    }

    public List<NameValuePair> mapToFormData(Map<String,String> data) {

        List<NameValuePair> formData = new ArrayList<>();
        Iterator it = data.keySet().iterator();

        while(it.hasNext()) {
            String entry = (String) it.next();
            formData.add(new BasicNameValuePair(entry, data.get(entry)));
        }

        return formData;
    }

    public String encodeUrl(String params) {

        try{
            params = URLEncoder.encode( params ,"UTF-8");
        }catch(Exception e){}

        return params;
    }

    public HashMap<String, String> getGroupCompanyIds() {
        String response = doGet(getData("@@url") + "/settings/getGroupCompanies", null);
        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            Pattern p = Pattern.compile("id=\"\\w+\"");
            Matcher m = p.matcher(arr.getJSONArray(i).getString(1));
            if (m.find()) {
                ids.put(arr.getJSONArray(i).getString(0), StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
            } else {
                ids.put(arr.getJSONArray(i).getString(0), "");
            }
            i++;
        }
        return ids;
    }

    public HashMap getDepartments(String companyID) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String resonseBody = doGet(getData("@@url") + "/DependentDrop/Departments?id=" + companyID, headers);
        JSONObject response = new JSONObject(resonseBody);
        if (!response.getString("status").equals("success") || (response.has("error") && response.get("error").equals("No Parent Department available"))) {
            return null;
        }
        JSONObject deptsMap = response.getJSONObject("update");
        HashMap ids = new HashMap();
        for (String key : deptsMap.keySet()) {
            String value = deptsMap.getString(key);
            value = value.replaceFirst(" +-+ +", "");
            ids.put(value, key);
        }
        return ids;
    }

    public HashMap<String, String> getOfficeLocations(String companyID) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject response = new JSONObject(doGet(getData("@@url") + "/DependentDrop/OfficeLocations?id=" + companyID, headers));
        if (!response.getString("status").equals("success")) {
            return null;
        }
        JSONObject locMap = response.getJSONObject("update");
        HashMap ids = new HashMap();
        for (String key : locMap.keySet()) {
            ids.put(locMap.getString(key).trim(), key);
        }
        return ids;
    }

    public HashMap<String, String> getOffices(String companyName) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject response = new JSONObject(doGet(getData("@@url") + "/settings/getOffices", headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            if (arr.getJSONArray(i).getString(2).equals(companyName)) {
                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(arr.getJSONArray(i).getString(3));
                if (m.find()) {
                    ids.put(arr.getJSONArray(i).getString(0).replaceAll("[()]", "").trim(), StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
                } else {
                    ids.put(arr.getJSONArray(i).getString(0).replaceAll("[()]", "").trim(), "");
                }
            }
            i++;
        }
        return ids;
    }



    public HashMap<String, String> getEmployeeTypes() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject response = new JSONObject(doGet(getData("@@url") + "/settings/getEmpTypeList", headers));

       JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            Pattern p = Pattern.compile("id=\"\\w+\"");
            Matcher m = p.matcher(arr.getJSONArray(i).getString(1));
            if (m.find()) {
                ids.put(arr.getJSONArray(i).getString(0), StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
            } else {
                ids.put(arr.getJSONArray(i).getString(0), "");
            }
            i++;
        }
        return ids;
    }


    public String deleteEmpTyppes(String empUserID,String typeID) {

        String url = data.get("@@url") +"/employeement/deleteRecord";

        HashMap<String, String> deafultBody = new HashMap<>();

        deafultBody.put("user",empUserID);
        deafultBody.put("resource",typeID);
        deafultBody.put("mode","delete");





        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        return doPost(url, headers, mapToFormData(deafultBody));


    }



    public JSONObject getDesignations(String companyID) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String resBody = doGet(getData("@@url") + "/DependentDrop/Designation?id=" + companyID, headers);
        JSONObject response = new JSONObject(resBody);

//        TODO: need to check response when no designations available
        if (!response.getString("status").equals("success") || (response.has("error"))) {
            return null;
        }
        return response.getJSONObject("update");
    }

    public void waitForUpdate(int n) {
        try{
            Thread.sleep(n * 1000 );
        } catch (Exception e){

        }
    }


    /**
     * Added below methods from Leave/ service class
     */
      // TODO : later please clean up Service class in Leaves or Services Class in Attendance

 /*
    gets grades
     */
    public HashMap<String, String> getGrades() {
        String url = data.get("@@url") + "/settings/GetGrades";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String value = arr.getJSONArray(i).getString(2).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }

    /*
   gets grades and bamds
    */
    public HashMap<String, String> getGradeAndBand() {
        String url = data.get("@@url") + "/settings/GetGrades";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String band = arr.getJSONArray(i).getString(1);

            //  if (m.find()) {
            ids.put(grade_name, band);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }


    /*
    gets bands
     */
    public HashMap<String, String> getBands() {
        String url = data.get("@@url") + "/settings/GetBands";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String value = arr.getJSONArray(i).getString(1).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }


    public HashMap<String, String> getUserAssignments() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        JSONObject response = new JSONObject(doGet(getData("@@url") + "/settings/getAssignment", headers));

        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            Pattern p = Pattern.compile("id=\"\\w+\"");
            Matcher m = p.matcher(arr.getJSONArray(i).getString(1));
            if (m.find()) {
                ids.put(arr.getJSONArray(i).getString(0), StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
            } else {
                ids.put(arr.getJSONArray(i).getString(0), "");
            }
            i++;
        }
        return ids;
    }

    /*
get job level info
@@returns String--> JobLevelIDs
 */
    public HashMap<String, String> getJobLevelIDS() {
        String url = data.get("@@url") + "/settings/getjoblevel";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");

        int i = 0;
        HashMap<String, String> ids = new HashMap<>();
        while (i < arr.length()) {
            String jobLevel = arr.getJSONArray(i).get(1).toString();
            String grade = arr.getJSONArray(i).get(2).toString();
            String jobLevelId = arr.getJSONArray(i).get(3).toString().substring(7, 20);
            ids.put(jobLevel, jobLevelId);
            i++;
        }
        return ids;
    }


}
