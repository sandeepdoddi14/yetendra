package com.darwinbox.recruitment.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.recruitment.objects.ArchivePosition;
import com.darwinbox.recruitment.objects.Designations;

import java.util.HashMap;
import java.util.Map;

public class DesignationsService extends Services {


    public void createDesignation(Designations designations){

        String url = getData("@@url") + "/settings/company/designations";

        Map<String, String> body = new HashMap<>();
        body.putAll(designations.getDefaultForDesignation());

        doPost(url, null,mapToFormData(body));
    }

}
