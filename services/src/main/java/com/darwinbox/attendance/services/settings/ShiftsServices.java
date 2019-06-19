package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.services.Services;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ShiftsServices extends Services {

    public JSONArray getShifts() {
        String url = getData("@@url") + "/attendance/shift/getAllShift";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        if (objResponse != null && objResponse.getString("status").equals("success")) {
            return objResponse.getJSONArray("update");
        } else {
            log.error("Status in Response: " + objResponse);
            throw new RuntimeException("ERROR: Unable to get shifts");
        }
    }

    public Shift getShiftInfo(String shiftName, String companyId) {

        JSONArray shifts = getShifts();
        Shift shiftObj = new Shift();

        if( companyId.length() == 0)
            companyId = "null";
        for (Object shift : shifts) {
            JSONObject obj = (JSONObject) shift;

            if ((obj.getString("shift_name").equalsIgnoreCase(shiftName) &&
                    obj.get("parent_company_id").toString().equals(companyId) )) {
                shiftObj.setShiftName(obj.getString("shift_name"));
                shiftObj.setGroupCompany( companyId.equals("null") ? "" : companyId);
                shiftObj.setShiftID(obj.getString("id"));
                shiftObj.setStartTime(obj.getString("begin_time"));
                shiftObj.setEndTime(obj.getString("end_time"));
                shiftObj.setOverNightShift(obj.getInt("is_next_day") == 1);
                shiftObj.setShowShiftChangeRequest(obj.getInt("show_in_request") == 1);
                shiftObj.setPolicyId(obj.getString("policy_id"));
                shiftObj.setShiftDescription(obj.getString("shift_description"));
                break;
            }
        }
        return shiftObj;
    }

    public void  createShift(Map<String,String> shiftData) {

            String url = getData("@@url") + "/attendance/shift/create";
            Map headers = new HashMap();
            headers.put("X-Requested-With", "XMLHttpRequest");
            doPost(url, headers, mapToFormData(shiftData));

    }

    public void updateShift(Map<String,String> shiftData) {

        String url = getData("@@url") + "/attendance/shift/update";
        Map headers = new HashMap();
        shiftData.put("TenantShiftForm[id]", "");
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(shiftData));

    }

    public void deleteShift(Shift shift) {

        String url = getData("@@url") + "/attendance/shift/delete?id=" + shift.getShiftID();
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doGet(url, headers);
    }

}
