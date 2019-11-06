package com.darwinbox.leaves.Services;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Holiday;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import org.json.simple.JSONArray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.*;

public class HolidayService extends Services {


    public String createHoliday(List<Holiday> holidays) {

        String url = getData("@@url") + UtilityHelper.getProperty("ServiceUrls","updateHoliday");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONArray arr = new JSONArray();
        for ( Holiday holiday : holidays ){
            arr.add(holiday.toService());
        }

        Map<String,String > data = new HashMap<String, String>();
        data.put("holidays", arr.toJSONString());
       return  doPost(url, headers, mapToFormData(data));

    }

    public String createHoliday(Holiday holiday) {

     return createHoliday(Arrays.asList(holiday));

    }

    public void deleteHolidays() {

        driver.get(data.get("@@url")+"/settings/leaves/holidays");

        List<WebElement> holidayElements = driver.findElements(By.cssSelector("table#tenantHolidaysList tbody tr"));

        List<String> holidayIds = new ArrayList<>();

        for ( WebElement element : holidayElements) {

            String holidayId = element.getAttribute("id");
            if(holidayId == null)
                continue;

            holidayId = holidayId.replace("holiday_", "");

            holidayIds.add(holidayId);

        }

        for( String holidayId : holidayIds) {

            String url = data.get("@@url")+"/settings/holidays";

            Map<String,String> headers = new HashMap<>();
            headers.put("x-requested-with","XMLHttpRequest" );

            deleteHoliday(url,headers,holidayId);
        }
    }

    private void deleteHoliday(String url, Map<String,String> headers, String holidayId) {


        Map<String,String> body = new HashMap<>();
        body.put("action","delete");
        body.put("id",holidayId);

        String res = doPost(url, headers, mapToFormData(body));

        if (res.contains("Holiday has been removed successfully"))
            log.info("Holiday deleted successfully");

        else {

            body = new HashMap<>();
            body.put("action","open_modal");
            body.put("id",holidayId);

            String response = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            try{
                response = doPost(url, headers, mapToFormData(body));
                response = response.split("TenantHolidays_date")[1].split("value=")[1].split("class")[0].replace("\\\"","");;
            } catch (Exception e){

            }

            body = new HashMap<>();
            body.put("action","edit");
            body.put("id",holidayId);

            body.put("TenantHolidays[name]","DeletedHoliday");
            body.put("TenantHolidays[date]",response);
            body.put("TenantHolidays[for_location][]","");
            body.put("TenantHolidays[optional]","0");
            body.put("TenantHolidays[holiday_repeats]","0");

            doPost(url, headers, mapToFormData(body));

            deleteHoliday(url,headers,holidayId);

        }

    }


    public String getHolidaysForUser(String userId) {
        HashMap<String, String> h = new HashMap<>();
        h.put("x-requested-with", "XMLHttpRequest");


        String applicationUrl = data.get("@@url");
        return doGet(applicationUrl + "/settings/holidayslist?user_id=" + userId, h);
    }
}
