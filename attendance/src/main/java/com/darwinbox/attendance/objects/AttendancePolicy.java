package com.darwinbox.attendance.objects;

import java.util.HashMap;
import java.util.List;

public class AttendancePolicy {

    private Absent absent;
    private MaxIn maxIn;
    private LateMark lateMark;
    private LateEarly lateEarly;
    private EarlyMark earlyMark;
    private WorkDuration workDuration;
    private BufferTime bufferTime;
    private AutoShift autoShift;
    private ShortLeave shortLeave;

    private MARKIN_POLICY markin_policy;
    private int graceTimeIn;
    private int graceTimeOut;

    private int max_optional_holiday;
    private boolean approveOptionalHolidays;

    private boolean allowWFH;
    private boolean allowOutDuty;
    private boolean allowBuffer;
    private boolean allowAutoShift;

    private boolean showTotalWorkDuration;
    private boolean showBreakDuration;
    private boolean showFinalDuration;
    private boolean showLateBy;
    private boolean showEarlyOut;
    private boolean showOverTime;

    private boolean req_weekoff;
    private boolean req_holiday;
    private boolean req_clockIn;
    private boolean req_outDuty;
    private boolean req_futureDates;
    private boolean req_shiftChange;
    private boolean req_shortLeave;

    private int edit_backdated;
    private int req_backdated;
    private int roster_backdated;
    private int autoApprove;
    private int abscondCount;

    private String policyName;
    private String policyID;
    private String policyDescription;
    private String companyID;

    public int getMax_optional_holiday() {
        return max_optional_holiday;
    }

    public void setMax_optional_holiday(int max_optional_holiday) {
        this.max_optional_holiday = max_optional_holiday;
    }

    public boolean isApproveOptionalHolidays() {
        return approveOptionalHolidays;
    }

    public void setApproveOptionalHolidays(boolean approveOptionalHolidays) {
        this.approveOptionalHolidays = approveOptionalHolidays;
    }

    public boolean isAllowWFH() {
        return allowWFH;
    }

    public void setAllowWFH(boolean allowWFH) {
        this.allowWFH = allowWFH;
    }

    public boolean isAllowOutDuty() {
        return allowOutDuty;
    }

    public void setAllowOutDuty(boolean allowOutDuty) {
        this.allowOutDuty = allowOutDuty;
    }

    public boolean isAllowBuffer() {
        return allowBuffer;
    }

    public void setAllowBuffer(boolean allowBuffer) {
        this.allowBuffer = allowBuffer;
    }

    public boolean isAllowAutoShift() {
        return allowAutoShift;
    }

    public void setAllowAutoShift(boolean allowAutoShift) {
        this.allowAutoShift = allowAutoShift;
    }

    public boolean isShowTotalWorkDuration() {
        return showTotalWorkDuration;
    }

    public void setShowTotalWorkDuration(boolean showTotalWorkDuration) {
        this.showTotalWorkDuration = showTotalWorkDuration;
    }

    public boolean isShowBreakDuration() {
        return showBreakDuration;
    }

    public void setShowBreakDuration(boolean showBreakDuration) {
        this.showBreakDuration = showBreakDuration;
    }

    public boolean isShowFinalDuration() {
        return showFinalDuration;
    }

    public void setShowFinalDuration(boolean showFinalDuration) {
        this.showFinalDuration = showFinalDuration;
    }

    public boolean isShowLateBy() {
        return showLateBy;
    }

    public void setShowLateBy(boolean showLateBy) {
        this.showLateBy = showLateBy;
    }

    public boolean isShowEarlyOut() {
        return showEarlyOut;
    }

    public void setShowEarlyOut(boolean showEarlyOut) {
        this.showEarlyOut = showEarlyOut;
    }

    public boolean isShowOverTime() {
        return showOverTime;
    }

    public void setShowOverTime(boolean showOverTime) {
        this.showOverTime = showOverTime;
    }

    public boolean isReq_weekoff() {
        return req_weekoff;
    }

    public void setReq_weekoff(boolean req_weekoff) {
        this.req_weekoff = req_weekoff;
    }

    public boolean isReq_holiday() {
        return req_holiday;
    }

    public void setReq_holiday(boolean req_holiday) {
        this.req_holiday = req_holiday;
    }

    public boolean isReq_clockIn() {
        return req_clockIn;
    }

    public void setReq_clockIn(boolean req_clockIn) {
        this.req_clockIn = req_clockIn;
    }

    public boolean isReq_outDuty() {
        return req_outDuty;
    }

    public void setReq_outDuty(boolean req_outDuty) {
        this.req_outDuty = req_outDuty;
    }

    public boolean isReq_futureDates() {
        return req_futureDates;
    }

    public void setReq_futureDates(boolean req_futureDates) {
        this.req_futureDates = req_futureDates;
    }

    public boolean isReq_shiftChange() {
        return req_shiftChange;
    }

    public void setReq_shiftChange(boolean req_shiftChange) {
        this.req_shiftChange = req_shiftChange;
    }

    public boolean isReq_shortLeave() {
        return req_shortLeave;
    }

    public void setReq_shortLeave(boolean req_shortLeave) {
        this.req_shortLeave = req_shortLeave;
    }

    public int getEdit_backdated() {
        return edit_backdated;
    }

    public void setEdit_backdated(int edit_backdated) {
        this.edit_backdated = edit_backdated;
    }

    public int getReq_backdated() {
        return req_backdated;
    }

    public void setReq_backdated(int req_backdated) {
        this.req_backdated = req_backdated;
    }

    public int getRoster_backdated() {
        return roster_backdated;
    }

    public void setRoster_backdated(int roster_backdated) {
        this.roster_backdated = roster_backdated;
    }

    public int getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(int autoApprove) {
        this.autoApprove = autoApprove;
    }

    public int getAbscondCount() {
        return abscondCount;
    }

    public void setAbscondCount(int abscondCount) {
        this.abscondCount = abscondCount;
    }

    public int getGraceTimeIn() {
        return graceTimeIn;
    }

    public void setGraceTimeIn(int graceTimeIn) {
        this.graceTimeIn = graceTimeIn;
    }

    public int getGraceTimeOut() {
        return graceTimeOut;
    }

    public void setGraceTimeOut(int graceTimeOut) {
        this.graceTimeOut = graceTimeOut;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public String getPolicyDescription() {
        return policyDescription;
    }

    public void setPolicyDescription(String policyDescription) {
        this.policyDescription = policyDescription;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Absent getAbsent() {
        return absent;
    }

    public void setAbsent(Absent absent) {
        this.absent = absent;
    }

    public MaxIn getMaxIn() {
        return maxIn;
    }

    public void setMaxIn(MaxIn maxIn) {
        this.maxIn = maxIn;
    }

    public LateMark getLateMark() {
        return lateMark;
    }

    public void setLateMark(LateMark lateMark) {
        this.lateMark = lateMark;
    }

    public LateEarly getLateEarly() {
        return lateEarly;
    }

    public void setLateEarly(LateEarly lateEarly) {
        this.lateEarly = lateEarly;
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

    public MARKIN_POLICY getMarkin_policy() {
        return markin_policy;
    }

    public void setMarkin_policy(MARKIN_POLICY markin_policy) {
        this.markin_policy = markin_policy;
    }

    public enum LEAVE_TYPE { HALFDAY, FULLDAY }
    public enum MARKIN_POLICY { NONE, INTIME, BOTH }

    public class BufferTime {
        private int minTime;
        private int maxTime;

        public int getMinTime() {
            return minTime;
        }

        public void setMinTime(int minTime) {
            this.minTime = minTime;
        }

        public int getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(int maxTime) {
            this.maxTime = maxTime;
        }
    }

    public class AutoShift {
        private List<Shift> shiftList;

        public List<Shift> getShiftList() {
            return shiftList;
        }

        public void setShiftList(List<Shift> shiftList) {
            this.shiftList = shiftList;
        }
    }

    public class ShortLeave {
        private int minTime;
        private int maxTime;
        private int shortleave_count;

        public int getMinTime() {
            return minTime;
        }

        public void setMinTime(int minTime) {
            this.minTime = minTime;
        }

        public int getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(int maxTime) {
            this.maxTime = maxTime;
        }

        public int getShortleave_count() {
            return shortleave_count;
        }

        public void setShortleave_count(int shortleave_count) {
            this.shortleave_count = shortleave_count;
        }
    }

    public class LateMark {
        private boolean isAutoApproved;
        private int count;
        private boolean isWeekoff;
        private boolean isHoliday;
        private boolean forALL;
        private String leaveId;
        private LEAVE_TYPE leaveType;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isWeekoff() {
            return isWeekoff;
        }

        public void setWeekoff(boolean weekoff) {
            isWeekoff = weekoff;
        }

        public boolean isHoliday() {
            return isHoliday;
        }

        public void setHoliday(boolean holiday) {
            isHoliday = holiday;
        }

        public boolean isForALL() {
            return forALL;
        }

        public void setForALL(boolean forALL) {
            this.forALL = forALL;
        }

        public String getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(String leaveId) {
            this.leaveId = leaveId;
        }

        public LEAVE_TYPE getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LEAVE_TYPE leaveType) {
            this.leaveType = leaveType;
        }

        public boolean isAutoApproved() {
            return isAutoApproved;
        }

        public void setAutoApproved(boolean autoApproved) {
            isAutoApproved = autoApproved;
        }
    }

    public class EarlyMark {
        private boolean isAutoApproved;
        private int count;
        private boolean isWeekoff;
        private boolean isHoliday;
        private String leaveId;
        private LEAVE_TYPE leaveType;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isWeekoff() {
            return isWeekoff;
        }

        public void setWeekoff(boolean weekoff) {
            isWeekoff = weekoff;
        }

        public boolean isHoliday() {
            return isHoliday;
        }

        public void setHoliday(boolean holiday) {
            isHoliday = holiday;
        }

        public String getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(String leaveId) {
            this.leaveId = leaveId;
        }

        public LEAVE_TYPE getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LEAVE_TYPE leaveType) {
            this.leaveType = leaveType;
        }

        public boolean isAutoApproved() {
            return isAutoApproved;
        }

        public void setAutoApproved(boolean autoApproved) {
            isAutoApproved = autoApproved;
        }
    }

    public class WorkDuration {
        private boolean isWeekoff;
        private boolean isHoliday;
        private int minTime;
        private int maxTime;
        private String leaveId;
        private LEAVE_TYPE leaveType;
        private boolean isAutoApproved;
        private boolean isFinal;

        public boolean isWeekoff() {
            return isWeekoff;
        }

        public void setWeekoff(boolean weekoff) {
            isWeekoff = weekoff;
        }

        public boolean isHoliday() {
            return isHoliday;
        }

        public void setHoliday(boolean holiday) {
            isHoliday = holiday;
        }

        public int getMinTime() {
            return minTime;
        }

        public void setMinTime(int minTime) {
            this.minTime = minTime;
        }

        public int getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(int maxTime) {
            this.maxTime = maxTime;
        }

        public String getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(String leaveId) {
            this.leaveId = leaveId;
        }

        public LEAVE_TYPE getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LEAVE_TYPE leaveType) {
            this.leaveType = leaveType;
        }

        public boolean isAutoApproved() {
            return isAutoApproved;
        }

        public void setAutoApproved(boolean autoApproved) {
            isAutoApproved = autoApproved;
        }

        public boolean isFinal() {
            return isFinal;
        }

        public void setFinal(boolean aFinal) {
            isFinal = aFinal;
        }
    }

    public class Absent {
        private boolean isWeekoff;
        private boolean isHoliday;
        private String leaveId;
        private LEAVE_TYPE leaveType;
        private boolean isAutoApproved;

        public boolean isWeekoff() {
            return isWeekoff;
        }

        public void setWeekoff(boolean weekoff) {
            isWeekoff = weekoff;
        }

        public boolean isHoliday() {
            return isHoliday;
        }

        public void setHoliday(boolean holiday) {
            isHoliday = holiday;
        }

        public String getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(String leaveId) {
            this.leaveId = leaveId;
        }

        public LEAVE_TYPE getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LEAVE_TYPE leaveType) {
            this.leaveType = leaveType;
        }

        public boolean isAutoApproved() {
            return isAutoApproved;
        }

        public void setAutoApproved(boolean autoApproved) {
            isAutoApproved = autoApproved;
        }
    }

    public class LateEarly {
        private int count;
        private boolean isWeekoff;
        private boolean isHoliday;
        private int minInTime;
        private int maxOutTime;
        private String leaveId;
        private LEAVE_TYPE leaveType;
        private boolean isAutoApproved;
        private boolean count_as_2;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isWeekoff() {
            return isWeekoff;
        }

        public void setWeekoff(boolean weekoff) {
            isWeekoff = weekoff;
        }

        public boolean isHoliday() {
            return isHoliday;
        }

        public void setHoliday(boolean holiday) {
            isHoliday = holiday;
        }

        public int getMinInTime() {
            return minInTime;
        }

        public void setMinInTime(int minInTime) {
            this.minInTime = minInTime;
        }

        public int getMaxOutTime() {
            return maxOutTime;
        }

        public void setMaxOutTime(int maxOutTime) {
            this.maxOutTime = maxOutTime;
        }

        public String getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(String leaveId) {
            this.leaveId = leaveId;
        }

        public LEAVE_TYPE getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LEAVE_TYPE leaveType) {
            this.leaveType = leaveType;
        }

        public boolean isAutoApproved() {
            return isAutoApproved;
        }

        public void setAutoApproved(boolean autoApproved) {
            isAutoApproved = autoApproved;
        }

        public boolean isCount_as_2() {
            return count_as_2;
        }

        public void setCount_as_2(boolean count_as_2) {
            this.count_as_2 = count_as_2;
        }

    }

    public class MaxIn {
        private boolean isWeekoff;
        private boolean isHoliday;
        private int minInTime;
        private int maxInTime;
        private String leaveId;
        private LEAVE_TYPE leaveType;
        private boolean isAutoApproved;

        public boolean isWeekoff() {
            return isWeekoff;
        }

        public void setWeekoff(boolean weekoff) {
            isWeekoff = weekoff;
        }

        public boolean isHoliday() {
            return isHoliday;
        }

        public void setHoliday(boolean holiday) {
            isHoliday = holiday;
        }

        public int getMinInTime() {
            return minInTime;
        }

        public void setMinInTime(int minInTime) {
            this.minInTime = minInTime;
        }

        public int getMaxInTime() {
            return maxInTime;
        }

        public void setMaxInTime(int maxInTime) {
            this.maxInTime = maxInTime;
        }

        public String getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(String leaveId) {
            this.leaveId = leaveId;
        }

        public boolean isAutoApproved() {
            return isAutoApproved;
        }

        public void setAutoApproved(boolean autoApproved) {
            isAutoApproved = autoApproved;
        }
    }

    public String parseToPHP(boolean flag){
      return flag ? "1" : "0";
    }

//    public HashMap<String,String> getAttendanceObjectFormData(){
//
//        HashMap<String,String> formData = new HashMap<>();
//
//        formData.put("AttendancePolicyForm[policy_name]",policyName);
//        formData.put("AttendancePolicyForm[policy_description]",policyDescription);
//        formData.put("AttendancePolicyForm[parent_company_id]",policyID);
//
//        formData.put("AttendancePolicyForm[grace_time]",graceTimeIn+"");
//        formData.put("AttendancePolicyForm[grace_time_early]",graceTimeOut+"");
//
//        formData.put("AttendancePolicyForm[max_optional_holiday_policy]",max_optional_holiday+"");
//        formData.put("AttendancePolicyForm[auto_approve_optional_holidays]",parseToPHP(approveOptionalHolidays));
//
//        formData.put("AttendancePolicyForm[work_from_home]",parseToPHP(allowWFH));
//        formData.put("AttendancePolicyForm[out_duty]",parseToPHP(allowOutDuty));
//
//        formData.put("AttendancePolicyForm[disable_work_duration]",parseToPHP(showTotalWorkDuration));
//        formData.put("AttendancePolicyForm[disable_final_work_duration]",parseToPHP(showFinalDuration));
//        formData.put("AttendancePolicyForm[disable_break_duration]",parseToPHP(showBreakDuration));
//        formData.put("AttendancePolicyForm[disable_late_mark]", parseToPHP(showLateBy));
//        formData.put("AttendancePolicyForm[disable_early_out]", parseToPHP(showEarlyOut));
//        formData.put("AttendancePolicyForm[disable_overtime]",parseToPHP(showOverTime));
//
//        formData.put("AttendancePolicyForm[edit_back_days]",edit_backdated+"");
//        formData.put("AttendancePolicyForm[apply_back_days_employee]", req_backdated+"");
//        formData.put("AttendancePolicyForm[roster_back_days]", roster_backdated+"");
//        formData.put("AttendancePolicyForm[auto_approve_days]", autoApprove+"");
//        formData.put("AttendancePolicyForm[absconding_days]", abscondCount+"");
//
//        formData.put("AttendancePolicyForm[allow_attrequest_weekoff]", parseToPHP(req_weekoff));
//        formData.put("AttendancePolicyForm[allow_attrequest_holiday]",  parseToPHP(req_holiday));
//        formData.put("AttendancePolicyForm[clockin_allowed_attendance_regularise]",  parseToPHP(req_clockIn));
//        formData.put("AttendancePolicyForm[od_allowed_attendance_regularise]", parseToPHP(req_outDuty));
//        formData.put("AttendancePolicyForm[allow_attendance_request_future]",  parseToPHP(req_futureDates));
//        formData.put("AttendancePolicyForm[allow_shift_change_request]", parseToPHP(req_shiftChange));
//
//        if (!req_shortLeave) {
//            shortLeave = new ShortLeave();
//        }
//        formData.put("AttendancePolicyForm[short_leave_allowed_attendance_regularise]", parseToPHP(req_shortLeave));
//        formData.put("AttendancePolicyForm[short_leave_allowed_days]",shortLeave.getShortleave_count()+"");
//        formData.put("AttendancePolicyForm[short_leave_min_mins]", shortLeave.getMinTime()+"");
//        formData.put("AttendancePolicyForm[short_leave_max_mins]", shortLeave.getMaxTime()+"");
//
//        formData.put("AttendancePolicyForm[markin_policy]", "2");
//        formData.put("AttendancePolicyForm[allow_shift_change_request]", "1");
//        formData.put("AttendancePolicyForm[allow_auto_shift_assignment]", "0");
//        formData.put("AttendancePolicyForm[allow_auto_shift_assignment]", "1");
//        formData.put("AttendancePolicyForm[allowed_shift_auto_assignment][]", "5bec73d9e9a28");
//        formData.put("AttendancePolicyForm[allowed_shift_auto_assignment][]", "5bed253d4c578");
//        formData.put("AttendancePolicyForm[allowed_shift_auto_assignment][]", "5bed25ea22d0a");
//        formData.put("AttendancePolicyForm[allowed_shift_auto_assignment][]", "5bf3c6e6082b7");
//        formData.put("AttendancePolicyForm[nearest]", "2");
//        formData.put("AttendancePolicyForm[nearest]", "1");
//        formData.put("AttendancePolicyForm[nearest]", "1");
////        formData.put("AttendancePolicyForm[pre_post]", "0");
////        formData.put("AttendancePolicyForm[pre_time_hour]", "0");
////        formData.put("AttendancePolicyForm[pre_time_min]", "0");
////        formData.put("AttendancePolicyForm[post_time_hour]", "0");
////        formData.put("AttendancePolicyForm[post_time_min]", "0");
////        formData.put("AttendancePolicyForm[leave_deduction_policy]", "0");
////        formData.put("AttendancePolicyForm[leave_deduction]","");
////        formData.put("AttendancePolicyForm[leave_deduction_deduct_after_approval]", "0");
////        formData.put("AttendancePolicyForm[leave_deduction_on_holiday]", "0");
////        formData.put("AttendancePolicyForm[leave_deduction_on_holiday]", "1");
////        formData.put("AttendancePolicyForm[leave_deduction_on_weeklyoff]", "0");
////        formData.put("AttendancePolicyForm[leave_deduction_on_weeklyoff]", "1");
////        formData.put("AttendancePolicyForm[work_duration_policy]", "0");
////        formData.put("AttendancePolicyForm[clock_in_hrs_half_day]", 0
////        formData.put("AttendancePolicyForm[clock_in_min_half_day]", 0
////        formData.put("AttendancePolicyForm[work_duration_leave_type]",
////        formData.put("AttendancePolicyForm[clock_in_hrs_full_day]", 0
////        formData.put("AttendancePolicyForm[clock_in_min_full_day]", 0
////        formData.put("AttendancePolicyForm[work_duration_deduct_after_approval]", 0
////        formData.put("AttendancePolicyForm[work_duration_deduction_on_holiday]", 0
////        formData.put("AttendancePolicyForm[work_duration_deduction_on_holiday]", 1
////        formData.put("AttendancePolicyForm[work_duration_deduction_on_weeklyoff]", 0
////        formData.put("AttendancePolicyForm[work_duration_deduction_on_weeklyoff]", 1
////        formData.put("AttendancePolicyForm[calculate_on_net_work_duration]", 0
////        formData.put("AttendancePolicyForm[lateplusearlymark_policy]", 0
////        formData.put("AttendancePolicyForm[lateplusearlymark_leave_deduction]",
////        formData.put("AttendancePolicyForm[lateplusearlymark_leave_type]",
////        formData.put("AttendancePolicyForm[number_of_lateplusearlymarks]", 1
////        formData.put("AttendancePolicyForm[every_next_lateplusearlymark]", 0
////        formData.put("AttendancePolicyForm[lateplusearlymark_deduct_after_approval]", 0
////        formData.put("AttendancePolicyForm[lateplusearlymark_deduction_on_holiday]", 0
////        formData.put("AttendancePolicyForm[lateplusearlymark_deduction_on_holiday]", 1
////        formData.put("AttendancePolicyForm[lateplusearlymark_deduction_on_weeklyoff]", 0
////        formData.put("AttendancePolicyForm[lateplusearlymark_deduction_on_weeklyoff]", 1
////        formData.put("AttendancePolicyForm[lateplusearlymark_both_on_sameday_count]", 2
////
////        formData.put("AttendancePolicyForm[latemark_policy]", 0
////        formData.put("AttendancePolicyForm[latemark_leave_deduction]",
////        formData.put("AttendancePolicyForm[latemark_leave_type]",
////        formData.put("AttendancePolicyForm[number_of_latemarks]", 1
////        formData.put("AttendancePolicyForm[every_next_latemark]", 0
////        formData.put("AttendancePolicyForm[latemark_deduct_after_approval]", 0
////        formData.put("AttendancePolicyForm[latemark_deduction_on_holiday]", 0
////        formData.put("AttendancePolicyForm[latemark_deduction_on_holiday]", 1
////        formData.put("AttendancePolicyForm[latemark_deduction_on_weeklyoff]", 0
////        formData.put("AttendancePolicyForm[latemark_deduction_on_weeklyoff]", 1
////        formData.put("AttendancePolicyForm[earlymark_policy]", 1
////        formData.put("AttendancePolicyForm[earlymark_leave_deduction]", 1
////        formData.put("AttendancePolicyForm[earlymark_leave_type]", 5a8d4f8b6c4a3
////        formData.put("AttendancePolicyForm[number_of_earlymarks]", 3
////        formData.put("AttendancePolicyForm[earlymark_deduct_after_approval]", 0
////        formData.put("AttendancePolicyForm[earlymark_deduction_on_holiday]", 0
////        formData.put("AttendancePolicyForm[earlymark_deduction_on_weeklyoff]", 0
////        formData.put("AttendancePolicyForm[earlymark_deduction_on_weeklyoff]", 1
////        formData.put("AttendancePolicyForm[change_date]", 0
////        formData.put("AttendancePolicyForm[id]", 5bfcf999f05a5
//    }

}
