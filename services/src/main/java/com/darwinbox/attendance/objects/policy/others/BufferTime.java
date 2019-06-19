package com.darwinbox.attendance.objects.policy.others;

import java.io.Serializable;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class BufferTime implements Serializable {

    private int inTime = 0;
    private int outTime = 0;

    public int getInTime() {
        return inTime;
    }

    public int getOutTime() {
        return outTime;
    }

    public String getInTimeMins() {
        return (inTime % 60) + "";
    }

    public void setInTimeMins(String inTimeMins) {
        inTime = inTime + Integer.parseInt(inTimeMins);
    }

    public String getInTimeHrs() {
        return (inTime / 60) + "";
    }

    public void setInTimeHrs(String inTimeHrs) {
        inTime = inTime + (Integer.parseInt(inTimeHrs) * 60);
    }

    public String getOutTimeMins() {
        return (outTime % 60) + "";
    }

    public void setOutTimeMins(String outTimeMins) {
        outTime = outTime + Integer.parseInt(outTimeMins);
    }

    public String getOutTimeHrs() {
        return outTime % 60 + "";
    }

    public void setOutTimeHrs(String outTimeHrs) {
        outTime = outTime + ( Integer.parseInt(outTimeHrs) * 60 );
    }

    public static BufferTime jsonToObject(Map<String,Object> data ) {

        BufferTime buffer = null;

        if (data.get("pre_post").equals("1")) {

            String preHrs = data.get("pre_time_hour").toString();
            String preMins = data.get("pre_time_min").toString();
            String postHrs = data.get("post_time_hour").toString();
            String postMins = data.get("post_time_min").toString();

            buffer.setInTimeHrs(preHrs);
            buffer.setInTimeMins(preMins);
            buffer.setOutTimeHrs(postHrs);
            buffer.setOutTimeMins(postMins);

        }

        return buffer;
    }

    public static Map<String, String> getMap(BufferTime  bufferTime) {

        Map<String, String> data = new HashMap<>();

        if (bufferTime != null) {

            data.put("AttendancePolicyForm[pre_post]", "1");
            data.put("AttendancePolicyForm[pre_time_hour]", bufferTime.getInTimeHrs());
            data.put("AttendancePolicyForm[pre_time_min]", bufferTime.getInTimeMins());
            data.put("AttendancePolicyForm[post_time_hour]", bufferTime.getOutTimeHrs() );
            data.put("AttendancePolicyForm[post_time_min]", bufferTime.getOutTimeMins());
        }

        return data;
    }

    public static boolean compareTo(BufferTime bufferTime, BufferTime bufferTime1) {

        if ( bufferTime != null ) {

            if ( bufferTime1 != null) {

                return bufferTime.getInTime() == bufferTime1.getInTime() &&
                        bufferTime.getOutTime() == bufferTime1.getOutTime();
            }

            return false;

        } else if ( bufferTime1 != null) {

            return false;

        } else {

            return true;

        }
    }
}

