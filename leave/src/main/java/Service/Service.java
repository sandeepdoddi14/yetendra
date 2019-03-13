package Service;

import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Service extends TestBase {

    public  String doGet(String url, Map<String, String> headers) {
        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }
            request.addHeader("Cookie", getCookies());
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
                    .setSocketTimeout(3 * 60 * 1000)
                    .build();
            if (headers != null) {
                if (headers.get("Cookie") == null) {
                    headers.put("Cookie", getCookies());
                }
            } else {
                headers = new HashMap<String, String>();
                headers.put("Cookie", getCookies());
            }

            return customPost(requestConfig, url, headers,formData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }


    public String customPost(RequestConfig requestConfig, String url, Map<String, String> headers, List<NameValuePair> fromData) {

        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpPost request = new HttpPost(url);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }
            request.setEntity(new UrlEncodedFormEntity(fromData));
            log.debug("POST Call: " + url);
            log.debug("Request body: " + fromData);
            HttpResponse result = httpClient.execute(request);
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
}
