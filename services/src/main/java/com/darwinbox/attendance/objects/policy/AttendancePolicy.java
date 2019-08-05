package com.darwinbox.attendance.objects.policy;

import com.darwinbox.attendance.objects.policy.leavedeductions.*;
import com.darwinbox.attendance.objects.policy.others.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AttendancePolicy implements Serializable {

    private Absent absent;
    private LateDuration lateDuration;
    private LateMark lateMark;
    private EarlyMark earlyMark;
    private WorkDuration workDuration;
    private LateEarly lateEarly;
    private EarlyDuration earlyDuration;

    private PolicyInfo policyInfo;
    private DisplayFlags displayFlags;
    private RequestFlags reqFlags;
    private RequestDays reqDays;

    private BufferTime bufferTime;
    private AutoShift autoShift;
    private ShortLeave shortLeave;

    public String sortOder = "sortableitem_4,sortableitem_6,sortableitem_1,sortableitem_2,sortableitem_7,sortableitem_3,sortableitem_5";

    public String getSortOrder() {
        return sortOder;
    }

    public void setSortOrder(Object sortObj) {
        if ( sortObj != null )
            sortOder = sortObj.toString();
    }

     public Absent getAbsent() {
        return absent;
    }

    public void setAbsent(Absent absent) {
        this.absent = absent;
    }

    public LateDuration getLateDuration() {
        return lateDuration;
    }

    public void setLateDuration(LateDuration lateDuration) {
        this.lateDuration = lateDuration;
    }

    public LateMark getLateMark() {
        return lateMark;
    }

    public void setLateMark(LateMark lateMark) {
        this.lateMark = lateMark;
    }

    public EarlyMark getEarlyMark() {
        return earlyMark;
    }

    public void setEarlyMark(EarlyMark earlyMark) {
        this.earlyMark = earlyMark;
    }

    public WorkDuration getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(WorkDuration workDuration) {
        this.workDuration = workDuration;
    }

    public LateEarly getLateEarly() {
        return lateEarly;
    }

    public void setLateEarly(LateEarly lateEarly) {
        this.lateEarly = lateEarly;
    }

    public PolicyInfo getPolicyInfo() {
        return policyInfo;
    }

    public void setPolicyInfo(PolicyInfo policyInfo) {
        this.policyInfo = policyInfo;
    }

    public DisplayFlags getDisplayFlags() {
        return displayFlags;
    }

    public void setDisplayFlags(DisplayFlags displayFlags) {
        this.displayFlags = displayFlags;
    }

    public RequestFlags getReqFlags() {
        return reqFlags;
    }

    public void setReqFlags(RequestFlags reqFlags) {
        this.reqFlags = reqFlags;
    }

    public RequestDays getReqDays() {
        return reqDays;
    }

    public void setReqDays(RequestDays reqDays) {
        this.reqDays = reqDays;
    }

    public BufferTime getBufferTime() {
        return bufferTime;
    }

    public void setBufferTime(BufferTime bufferTime) {
        this.bufferTime = bufferTime;
    }

    public AutoShift getAutoShift() {
        return autoShift;
    }

    public void setAutoShift(AutoShift autoShift) {
        this.autoShift = autoShift;
    }

    public ShortLeave getShortLeave() {
        return shortLeave;
    }

    public void setShortLeave(ShortLeave shortLeave) {
        this.shortLeave = shortLeave;
    }

    public EarlyDuration getEarlyDuration() {
        return earlyDuration;
    }

    public void setEarlyDuration(EarlyDuration earlyDuration) {
        this.earlyDuration = earlyDuration;
    }

    public void jsonToObject(Map<String, Object> policyData) {

        policyInfo = PolicyInfo.jsonToObject(policyData);
        displayFlags = DisplayFlags.jsonToObject(policyData);
        reqFlags = RequestFlags.jsonToObject(policyData);
        reqDays = RequestDays.jsonToObject(policyData);

        shortLeave = ShortLeave.jsonToObject(policyData);
        bufferTime = BufferTime.jsonToObject(policyData);
        autoShift = AutoShift.jsonToObject(policyData);

        lateMark = LateMark.jsonToObject(policyData);
        earlyMark = EarlyMark.jsonToObject(policyData);
        lateEarly = LateEarly.jsonToObject(policyData);
        workDuration = WorkDuration.jsonToObject(policyData);
        lateDuration = LateDuration.jsonToObject(policyData);
        absent = Absent.jsonToObject(policyData);
        earlyDuration = EarlyDuration.jsonToObject(policyData);

        setSortOrder(policyData.get("sortable_order"));

    }

    public Map<String,String> getMap() {

        Map<String,String> body = new HashMap<>();

        body.putAll(policyInfo.getMap());
        body.putAll(displayFlags.getMap());
        body.putAll(reqFlags.getMap());
        body.putAll(reqDays.getMap());

        body.putAll(ShortLeave.getMap(shortLeave));
        body.putAll(BufferTime.getMap(bufferTime));
        body.putAll(AutoShift.getMap(autoShift));

        body.putAll(LateDuration.getMap(lateDuration));
        body.putAll(LateMark.getMap(lateMark));
        body.putAll(EarlyMark.getMap(earlyMark));
        body.putAll(LateEarly.getMap(lateEarly));
        body.putAll(WorkDuration.getMap(workDuration) );
        body.putAll(Absent.getMap(absent) );
        body.putAll(EarlyDuration.getMap(earlyDuration) );

        body.put("AttendancePolicyForm[sortable_order]",getSortOrder());

        return body;
    }


    public boolean compareTo(AttendancePolicy policy) {

        return  getPolicyInfo().compareTo(policy.getPolicyInfo()) &&
                getDisplayFlags().compareTo(policy.getDisplayFlags()) &&
                getReqFlags().compareTo(policy.getReqFlags()) &&
                getReqDays().compareTo(policy.getReqDays()) &&

                getSortOrder().equalsIgnoreCase(policy.getSortOrder()) &&

                ShortLeave.compareTo(getShortLeave(),policy.getShortLeave()) &&
                BufferTime.compareTo(getBufferTime(),policy.getBufferTime()) &&
                AutoShift.compareTo(getAutoShift(),policy.getAutoShift()) &&

                LateMark.compareTo(getLateMark(),policy.getLateMark()) &&
                Absent.compareTo(getAbsent(),policy.getAbsent()) &&
                EarlyMark.compareTo(getEarlyMark(),policy.getEarlyMark()) &&
                LateEarly.compareTo(getLateEarly(),policy.getLateEarly()) &&
                WorkDuration.compareTo(getWorkDuration(),policy.getWorkDuration()) &&
                EarlyDuration.compareTo(getEarlyDuration(),policy.getEarlyDuration()) &&
                LateDuration.compareTo(getLateDuration(),policy.getLateDuration());

    }
}
