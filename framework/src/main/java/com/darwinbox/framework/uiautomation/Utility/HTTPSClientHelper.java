package com.darwinbox.framework.uiautomation.Utility;

import com.darwinbox.framework.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import java.time.LocalDate;

import java.util.*;

public class HTTPSClientHelper extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;
	DateTimeHelper objDateTimeHelper;
	UtilityHelper objUtil;
	ActionHelper objActionHelper;
	BrowserHelper objBrowserHelper;
	ExcelWriter objExcelWriter;

	public static final Logger log = Logger.getLogger(HTTPSClientHelper.class);

	public HTTPSClientHelper(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
		objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
		objDateTimeHelper = PageFactory.initElements(driver, DateTimeHelper.class);
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		objJavaScrHelper = PageFactory.initElements(driver, JavaScriptHelper.class);
		objActionHelper = PageFactory.initElements(driver, ActionHelper.class);
		objBrowserHelper = PageFactory.initElements(driver, BrowserHelper.class);
		objExcelWriter = PageFactory.initElements(driver, ExcelWriter.class);
	}

	private String getCookies() {
		Set<Cookie> cookieSet = driver.manage().getCookies();
		String cookie = "";
		for (Cookie c : cookieSet) {
			String name = c.getName();
			String val = c.getValue();
			cookie = cookie + ";" + name + "=" + val;
		}
		return cookie;
	}

	public JSONObject doGet(String url, Map<String, String> headers) {
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					request.addHeader(key, headers.get(key));
				}
			}
			request.addHeader("Cookie", getCookies());
			log.info("GET Call: " + url);
			HttpResponse result = httpClient.execute(request);
			String response = EntityUtils.toString(result.getEntity(), "UTF-8");
			return new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject doPost(String url, Map<String, String> headers, Map<Object, Object> body) {
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					request.addHeader(key, headers.get(key));
				}
			}
			request.addHeader("Cookie", getCookies());
			request.setEntity(new StringEntity(body.toString(), "UTF-8"));
			log.info("POST Call: " + url);
			log.info("Request body: " + body.toString());
			HttpResponse result = httpClient.execute(request);
			String response = EntityUtils.toString(result.getEntity(), "UTF-8");
			return new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject doPost(String url, Map<String, String> headers, List<NameValuePair> fromData) {
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					request.addHeader(key, headers.get(key));
				}
			}
			request.addHeader("Cookie", getCookies());
			request.setEntity(new UrlEncodedFormEntity(fromData));
			log.info("POST Call: " + url);
			log.info("Request body: " + fromData);
			HttpResponse result = httpClient.execute(request);
			String response = EntityUtils.toString(result.getEntity(), "UTF-8");
			System.out.println(response);
			return new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public boolean createHolidays(String fromDate, String toDate) {
		try {
			LocalDate startDate = LocalDate.parse(fromDate);
			LocalDate endDate = LocalDate.parse(toDate);
			LocalDate iterationDate = endDate;

			while (iterationDate.isAfter(startDate)) {

				if (iterationDate.getDayOfWeek().toString().equals("SUNDAY")
						|| iterationDate.getDayOfWeek().toString().equals("SATURDAY")
						|| iterationDate.getDayOfWeek().toString().equals("FRIDAY")
						|| iterationDate.getDayOfWeek().toString().equals("THURSDAY")) {
				} else {
					String url = data.get("@@url")+ "/settings/updateholiday";
					String jsonString = "[{\"holiday_name\":\"" + iterationDate.toString() + "\",\"holiday_date\":\""
							+ iterationDate.toString()
							+ "\",\"holiday_optional\":0,\"holiday_repeats\":false,\"holiday_location\":[\"city_114494\"],\"errors\":[]}]";
					List<NameValuePair> formData = new ArrayList<>();
					formData.add(new BasicNameValuePair("holidays", jsonString));
					HashMap<String, String> headers = new HashMap<>();
					headers.put("X-Requested-With", "XMLHttpRequest");
					doPost(url, headers, formData);

				}
				iterationDate = iterationDate.minusDays(1);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createOptionalHolidays(String date) {
		try {

			String url = data.get("@@url")+ "/settings/updateholiday";
			String jsonString = "[{\"holiday_name\":\"" + date + "\",\"holiday_date\":\"" + date
					+ "\",\"holiday_optional\":1,\"holiday_repeats\":false,\"holiday_location\":[\"city_114494\"],\"errors\":[]}]";
			List<NameValuePair> formData = new ArrayList<>();
			formData.add(new BasicNameValuePair("holidays", jsonString));
			HashMap<String, String> headers = new HashMap<>();
			headers.put("X-Requested-With", "XMLHttpRequest");
			doPost(url, headers, formData);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// public void updateHoliday() {
	// String reqURL = "https://vijay.qa.darwinbox.io/settings/updateholiday";
	// List<NameValuePair> formData = new ArrayList<>();
	// Map headers = new HashMap();
	// headers.put("X-Requested-With", "XMLHttpRequest");
	// headers.put("Cookie",
	// "ab805d4a680a53f51e16ffb2737d0dc5=434d31c022b0221fd4255c3b94124f160b920488a%3A4%3A%7Bi%3A0%3Bs%3A1%3A%222%22%3Bi%3A1%3Bs%3A10%3A%22azejabegas%22%3Bi%3A2%3Bi%3A604800%3Bi%3A3%3Ba%3A15%3A%7Bs%3A18%3A%22userSessionTimeout%22%3Bi%3A1529320674%3Bs%3A9%3A%22plan_name%22%3Bs%3A0%3A%22%22%3Bs%3A13%3A%22monthly_spend%22%3Bi%3A0%3Bs%3A14%3A%22account_status%22%3Bi%3A2%3Bs%3A7%3A%22is_paid%22%3Bi%3A0%3Bs%3A15%3A%22financial_cycle%22%3Bs%3A1%3A%224%22%3Bs%3A26%3A%22no_of_employees_on_payment%22%3Bi%3A0%3Bs%3A18%3A%22account_created_on%22%3Bi%3A1518596172%3Bs%3A31%3A%22changed_from_trial_to_active_on%22%3Bi%3A0%3Bs%3A12%3A%22mod_is_leave%22%3Bi%3A1%3Bs%3A17%3A%22mod_is_attendance%22%3Bi%3A1%3Bs%3A13%3A%22mod_is_stream%22%3Bi%3A1%3Bs%3A20%3A%22mod_is_reimbursement%22%3Bi%3A1%3Bs%3A14%3A%22mod_is_payroll%22%3Bi%3A1%3Bs%3A9%3A%22expire_on%22%3Bi%3A2123366400%3B%7D%7D;
	// PHPSESSID=nmism22d8ocpe23rmn7pllnbp1");
	// formData.add(new BasicNameValuePair("holidays",
	// "[{\"holiday_name\":\"Test28\",\"holiday_date\":\"2018-06-28\",\"holiday_optional\":0,\"holiday_repeats\":false,\"holiday_location\":[],\"errors\":[]}]"));
	// doPost(reqURL, headers, formData);
	// }

}
