package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.RecruitmentSettings;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RecruitmentSettingsService extends Services {

    public void createSettings(RecruitmentSettings recruitmentSettings){

        String url = getData("@@url") + "/settings/recruitment/user";

        List<NameValuePair> list = new ArrayList<>();
        list.addAll(recruitmentSettings.toMap());

        doPost(url, null,list);
    }
}
