package com.sample.framework.uiautomation.Utility;

import com.sample.framework.uiautomation.helper.Action.ActionHelper;
import com.sample.framework.uiautomation.helper.Alert.AlertHelper;
import com.sample.framework.uiautomation.helper.Browser.BrowserHelper;
import com.sample.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.sample.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.helper.Wait.WaitHelper;
import com.sample.framework.uiautomation.helper.genericHelper.GenericHelper;
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



}
