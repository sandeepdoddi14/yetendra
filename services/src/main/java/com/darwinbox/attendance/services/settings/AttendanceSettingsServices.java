package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.AttendanceSettingsPage;
import com.darwinbox.Services;

public class AttendanceSettingsServices extends Services {

    public void createSettings(AttendanceSettingsPage attendanceSettingsPage) {
        String url = getData("@@url") + "/settings/attendance/attendancesettings";
        doPost(url, null,mapToFormData(attendanceSettingsPage.toMap()));
    }
}
