package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.HiringWorkFlow.HiringWorkFlow;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiringWorkFlowService extends Services {

    public void createSettings(HiringWorkFlow hiringWorkFlow){

        String url = getData("@@url") + "/settings/recruitment";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> list = new ArrayList<>();
        list.addAll(hiringWorkFlow.toMap());

        doPost(url, headers,list);
    }

}
