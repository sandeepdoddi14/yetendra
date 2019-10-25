package com.darwinbox.recruitment.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.recruitment.objects.Permissions;

import java.util.HashMap;
import java.util.Map;

public class PermissionService extends Services {


    public void createRequisitionPermission(Permissions permissions){

        String url = getData("@@url") + "/settings/permissions/assign";

        Map<String, String> body = new HashMap<>();
        body.putAll(permissions.toMap());

        doPost(url, null,mapToFormData(body));
    }
}
