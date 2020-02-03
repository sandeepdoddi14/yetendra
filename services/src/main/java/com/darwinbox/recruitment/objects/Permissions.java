package com.darwinbox.recruitment.objects;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permissions {


    public String permissionGroup;
    public String restrictedGroups;
    public String permissionUsers;

    public String getRestrictedGroups() {
        return restrictedGroups;
    }

    public void setRestrictedGroups(String restrictedGroups) {
        this.restrictedGroups = restrictedGroups;
    }

    public String getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(String permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public String getPermissionUsers() {
        return permissionUsers;
    }

    public void setPermissionUsers(String permissionUsers) {
        this.permissionUsers = permissionUsers;
    }

    public void toObject(Map<String,String> body, String employeeUserID) {

         setPermissionGroup(body.get("permissionGroup"));
         setPermissionUsers(employeeUserID);
    }

    public Map<String, String> toMap() {

        Map<String, String> body = new HashMap<>();

        body.put("yt0","SAVE");
        body.put("Permission[group]",getPermissionGroup());
        body.put("Permission[restriction][0][]",getRestrictedGroups());
        body.put("Permission[users][0][]",getPermissionUsers());

        return body;
    }

    }
