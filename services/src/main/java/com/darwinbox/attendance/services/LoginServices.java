package com.darwinbox.attendance.services;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginServices extends Services {

    public String doLogin(String userName, String password) {

        String url = data.get("@@url") + "/user/login";
        String phpSessionID = "";

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("UserLogin[username]", userName));
        body.add(new BasicNameValuePair("UserLogin[password]", password));

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.setEntity(new UrlEncodedFormEntity(body));
            log.debug("POST Call: " + url);
            log.debug("Request body: " + body);
            HttpResponse result = httpClient.execute(request);
            phpSessionID = result.getHeaders("Set-Cookie")[1].getValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("EXCEP: login call failed");
        }
        return phpSessionID;
    }

}
