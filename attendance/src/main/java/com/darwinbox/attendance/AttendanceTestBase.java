package com.darwinbox.attendance;

import com.darwinbox.attendance.objects.*;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.*;
import com.darwinbox.attendance.objects.policy.others.*;
import com.darwinbox.attendance.services.AttendanceService;

import com.darwinbox.attendance.services.CronServices;
import com.darwinbox.attendance.services.RequestServices;

import com.darwinbox.attendance.services.apply.AttendanceImport;
import com.darwinbox.attendance.services.apply.AttendanceRequestServices;
import com.darwinbox.attendance.services.apply.LeaveApply;
import com.darwinbox.attendance.services.settings.AttendancePolicyService;
import com.darwinbox.attendance.services.settings.HolidayService;
import com.darwinbox.attendance.services.settings.LeaveSettings;
import com.darwinbox.attendance.services.settings.ShiftsServices;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.darwinbox.attendance.services.RequestServices.Act.APPROVE;
import static com.darwinbox.attendance.services.RequestServices.RequestType.ALL_REQUESTS;
import static com.darwinbox.attendance.services.RequestServices.RequestType.ATTENDANCE_REQUESTS;
import static com.darwinbox.attendance.services.RequestServices.RequestType.PENDING;

public class AttendanceTestBase {

    private static HashMap<String,AttendanceTestBase> obj;

    public String pending = "Pending Leave";
    public String approved = "On leave";
    public String approved_halfday = "Halfday Leave";
    public String pending_halfday = "Pending Halfday Leave";
    public String system = "Pending Halfday Leave";
    public String present = "Present";
    public String absent = "Absent";
    public String singlePunch = "Single Punch Absent";
    public String holiday = "Holiday";
    public String weekoff = "Weekly off";
    public String clockin_future = "cant apply for future time";
    public String clockin_weekOffs = "cant apply on weekoff";
    public String clockin_holiday = "cant apply on holiday";
    public String clockin_Success="";

    private List<Shift> shifts;
    private List<AttendancePolicy> policies;
    private Map<String, String> weekoffs;
    private Map<String, String> leaves;
    private DateTimeHelper helper;
    private String fileName;

    private AttendanceTestBase(String fileName) {
        this.fileName = fileName;
        helper = new DateTimeHelper();
        shifts = new ArrayList<>();
        policies = new ArrayList<>();
        weekoffs = new HashMap<>();
        leaves = new HashMap<>();
    }

    public static AttendanceTestBase getObject(String baseDateFile) {

        if (obj == null)
            obj = new HashMap<>();

        AttendanceTestBase atb = obj.get(baseDateFile);

        if (atb == null) {
            atb = new AttendanceTestBase(baseDateFile);
            atb.loadData();
            obj.put(baseDateFile, atb);
        }

        atb.deleteHolidays();

        return atb;
    }


    public static AttendanceTestBase getObject() {
        return getObject("LeaveDeductionPolicies.xlsx");
    }

    private void loadData() {

        createLeaves();
        createShifts();
        createAttendancePolicies();
        getWeeklyOffs();
        //createIPRestrictions();
        // createRegularizeReason();

    }

    private void deleteHolidays() {
        HolidayService srvc = new HolidayService();
        srvc.deleteHolidays();
    }

    private void createLeaves() {

        leaves = new HashMap<>();

        List<Map<String, String>> leaveData = readDatafromSheet("Leaves");

        LeaveSettings srvc = new LeaveSettings();
        String grpCompany = srvc.getGroupCompanyIds().get(TestBase.data.get("@@group"));

        for (Map<String, String> data : leaveData) {

            String companyId = data.get("isParent").equalsIgnoreCase("yes") ? "main" : grpCompany;
            String leave_id = srvc.createLeave(data.get("LeaveName"), companyId);
            leaves.put(data.get("LeaveName"), leave_id);
            srvc.log.info(" Leave : " + data.get("LeaveName") + " is available " );
        }
    }

    public String getLeaveId(String name) {
        return leaves.get(name);
    }

    public AttendancePolicy getAttendancePolicy(String name) {
        return policies.stream().filter(policy -> policy.getPolicyInfo().getPolicyName().equals(name)).collect(Collectors.toList()).get(0);
    }


    public String getWeeklyOff(String name) {
        return weekoffs.get(name);
    }

    public Shift getShift(String name) {
        return shifts.stream().filter(shift -> shift.getShiftName().equals(name)).collect(Collectors.<Shift>toList()).get(0);
    }

    public int getData(String data, String substr) {

        String tmp = data.replace(substr + "#", "");
        if (tmp.equalsIgnoreCase("no")) {
            return -1;
        }

        return Integer.parseInt(tmp);
    }

    private List<Map<String, String>> readDatafromSheet(String sheetname) {

        Map<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "BaseData/"+fileName);
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }

    private void createShifts() {

        List<Map<String, String>> shiftData = readDatafromSheet("Shifts");

        ShiftsServices srvc = new ShiftsServices();
        String grpCompany = srvc.getGroupCompanyIds().get(TestBase.data.get("@@group"));

        for (Map<String, String> data : shiftData) {

            String companyId = data.get("isParent").equalsIgnoreCase("yes") ? "" : grpCompany;
            data.put("Group Company", companyId);
            Shift obj = new Shift();
            obj.toObject(data);

            Shift curr_shift = srvc.getShiftInfo(data.get("Shift Name"), companyId);

            srvc.log.info(obj.getShiftName() + " Shift is identified");

            if (curr_shift.getShiftName() == null) {
                srvc.createShift(obj.getShiftToMap());
                srvc.log.info(curr_shift.getShiftName() + " Shift is created");
            }

            if (!curr_shift.compareTo(obj)) {
                obj.setShiftID(curr_shift.getShiftID());
                srvc.updateShift(obj.getShiftToMap());
                srvc.log.info(curr_shift.getShiftName() + " Shift is updated");
            }

            curr_shift = srvc.getShiftInfo(data.get("Shift Name"), companyId);
            curr_shift.getWorkDuration();

            shifts.add(curr_shift);

        }

    }

    private void createAttendancePolicies() {

        List<Map<String, String>> policyData = readDatafromSheet("Policies");

        List<Map<String, String>> absent = readDatafromSheet("Absent");
        List<Map<String, String>> workduration = readDatafromSheet("WorkDuration");
        List<Map<String, String>> lateduration = readDatafromSheet("LateDuration");
        List<Map<String, String>> earlyduration = readDatafromSheet("EarlyDuration");
        List<Map<String, String>> latemark = readDatafromSheet("LateMark");
        List<Map<String, String>> earlymark = readDatafromSheet("EarlyMark");
        List<Map<String, String>> lateearly = readDatafromSheet("LatePlusEarly");
        List<Map<String, String>> common = readDatafromSheet("Common");
        List<Map<String, String>> display = readDatafromSheet("Display");
        List<Map<String, String>> reqflags = readDatafromSheet("RequestFlags");
        List<Map<String, String>> reqdays = readDatafromSheet("RequestDays");

        AttendancePolicyService srvc = new AttendancePolicyService();
        String grpCompanyId = srvc.getGroupCompanyIds().get(TestBase.data.get("@@group"));
        String parentCompany = TestBase.data.get("@@parent");
        String grpCompany = TestBase.data.get("@@group");

        for (Map<String, String> data : policyData) {

            boolean isParent = data.get("isParent").equalsIgnoreCase("yes");

            String companyName = isParent ? parentCompany : grpCompany;
            String companyId = isParent ? "" : grpCompanyId;

            data.put("Group Company", companyId);

            AttendancePolicy policy = new AttendancePolicy();

            int res = getData(data.get("Common"), "Common");
            Map<String, String> commonData = common.get(res - 1);

            PolicyInfo info = new PolicyInfo();
            commonData.putAll(data);
            info.toObject(commonData);
            policy.setPolicyInfo(info);

            srvc.log.info("Preparing data set for " + info.getPolicyName());

            BufferTime bufferTime = null;
            String buffTime = commonData.get("BufferTime");
            if (!buffTime.equalsIgnoreCase("no")) {
                bufferTime = new BufferTime();
                String pre_post[] = buffTime.split(",");

                String pre[] = pre_post[0].split(":");
                String post[] = pre_post[1].split(":");

                bufferTime.setInTimeHrs(pre[0]);
                bufferTime.setInTimeMins(pre[1]);
                bufferTime.setOutTimeHrs(post[0]);
                bufferTime.setOutTimeHrs(post[1]);
            }
            policy.setBufferTime(bufferTime);

            ShortLeave shortLeave = null;
            String shortLeaveData = commonData.get("ShortLeave");
            if (!shortLeaveData.equalsIgnoreCase("no")) {

                shortLeave = new ShortLeave();
                String sl[] = shortLeaveData.split(",");

                shortLeave.setMinTime(Integer.parseInt(sl[0]));
                shortLeave.setMaxTime(Integer.parseInt(sl[1]));
                shortLeave.setCount(Integer.parseInt(sl[2]));
            }
            policy.setShortLeave(shortLeave);

            res = getData(commonData.get("Display"), "Display");
            Map<String, String> displayData = display.get(res - 1);
            DisplayFlags displayFlags = new DisplayFlags();
            displayFlags.toObject(displayData);
            policy.setDisplayFlags(displayFlags);

            res = getData(commonData.get("RequestFlags"), "RequestFlags");
            Map<String, String> reqFlagsData = reqflags.get(res - 1);
            RequestFlags reqFlags = new RequestFlags();
            reqFlags.toObject(reqFlagsData);
            policy.setReqFlags(reqFlags);

            res = getData(commonData.get("RequestDays"), "RequestDays");
            Map<String, String> reqDaysData = reqdays.get(res - 1);
            RequestDays reqDays = new RequestDays();
            reqDays.toObject(reqDaysData);
            policy.setReqDays(reqDays);

            res = getData(data.get("Absent"), "Absent");
            Absent absentObj = null;
            if (res != -1) {
                Map<String, String> absentData = absent.get(res - 1);
                String leave_id = leaves.get(absentData.get("LeaveToDeduct"));
                absentData.put("LeaveToDeductId", leave_id);
                absentObj = new Absent();
                absentObj.toObject(absentData);
            }
            policy.setAbsent(absentObj);

            res = getData(data.get("LateDuration"), "LateDuration");
            LateDuration lateDuration = null;
            if (res != -1) {
                Map<String, String> lateDurData = lateduration.get(res - 1);
                String leave_id = leaves.get(lateDurData.get("LeaveToDeduct"));
                lateDurData.put("LeaveToDeductId", leave_id);
                lateDuration = new LateDuration();
                lateDuration.toObject(lateDurData);
            }
            policy.setLateDuration(lateDuration);

            res = getData(data.get("EarlyDuration"), "EarlyDuration");
            EarlyDuration earlyDuration = null;
            if (res != -1) {
                Map<String, String> earlyDurData = earlyduration.get(res - 1);
                String leave_id = leaves.get(earlyDurData.get("LeaveToDeduct"));
                earlyDurData.put("LeaveToDeductId", leave_id);
                earlyDuration = new EarlyDuration();
                earlyDuration.toObject(earlyDurData);
            }
            policy.setEarlyDuration(earlyDuration);

            res = getData(data.get("LateMark"), "LateMark");
            LateMark lateMark = null;
            if (res != -1) {
                Map<String, String> lateMarkData = latemark.get(res - 1);
                String leave_id = leaves.get(lateMarkData.get("LeaveToDeduct"));
                lateMarkData.put("LeaveToDeductId", leave_id);
                lateMark = new LateMark();
                lateMark.toObject(lateMarkData);
            }
            policy.setLateMark(lateMark);

            res = getData(data.get("EarlyMark"), "EarlyMark");
            EarlyMark earlyMark = null;
            if (res != -1) {
                Map<String, String> earlyMarkData = earlymark.get(res - 1);
                String leave_id = leaves.get(earlyMarkData.get("LeaveToDeduct"));
                earlyMarkData.put("LeaveToDeductId", leave_id);
                earlyMark = new EarlyMark();
                earlyMark.toObject(earlyMarkData);
            }
            policy.setEarlyMark(earlyMark);

            res = getData(data.get("LatePlusEarly"), "LatePlusEarly");
            LatePlusEarly lateplusearly = null;
            if (res != -1) {
                Map<String, String> lateearlyData = lateearly.get(res - 1);
                String leave_id = leaves.get(lateearlyData.get("LeaveToDeduct"));
                lateearlyData.put("LeaveToDeductId", leave_id);
                lateplusearly = new LatePlusEarly();
                lateplusearly.toObject(lateearlyData);
            }
            policy.setLateEarly(lateplusearly);

            res = getData(data.get("WorkDuration"), "WorkDuration");
            WorkDuration workdurObj = null;
            if (res != -1) {
                Map<String, String> workdurData = workduration.get(res - 1);
                String leave_id = leaves.get(workdurData.get("LeaveToDeduct"));
                workdurData.put("LeaveToDeductId", leave_id);
                workdurObj = new WorkDuration();
                workdurObj.toObject(workdurData);
            }
            policy.setWorkDuration(workdurObj);

            try {
                String policy_id = srvc.getAttendancePolicyId(data.get("Policy Name"), companyName);

                if (policy_id == null) {
                    srvc.createAttendancePolicy(policy);
                    srvc.log.info(data.get("Policy Name") + " is created ");
                } else {

                    AttendancePolicy policylive = srvc.getAttendancePolicy(policy_id);
                    policy.getPolicyInfo().setPolicyID(policy_id);

                    if (!policylive.compareTo(policy)) {
                        srvc.log.info(data.get("Policy Name") + " is updated");
                        srvc.updateAttendancePolicy(policy);
                        policy_id = null;
                    }
                }

                if (policy_id == null) {
                    policy_id = srvc.getAttendancePolicyId(data.get("Policy Name"), companyName);
                    policy = srvc.getAttendancePolicy(policy_id);
                }

                policies.add(policy);
            } catch (Exception e) {
                srvc.log.info(" Exception while processing data ");
                srvc.log.error(e.getMessage());
            }

        }

    }

    private void writeDatatoFile() {

        try {
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("testdata") + "attendance");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for ( AttendancePolicy policy : policies)
                out.writeObject(policy);
            for ( Shift shift : shifts)
                out.writeObject(shift);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readDatafromFile() {

        policies = new ArrayList<>();
        shifts = new ArrayList<>();

        try {

            FileInputStream fileIn = new FileInputStream(System.getProperty("testdata") + "attendance");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj;

            while ( true ) {

                obj = in.readObject();
                if ( obj == null)
                    break;
                if (obj instanceof AttendancePolicy) {
                    AttendancePolicy policy = (AttendancePolicy)obj;
                    policies.add(policy);
                }
                if (obj instanceof Shift) {
                    shifts.add((Shift)obj);
                }
            }
            return;
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void getWeeklyOffs() {

        List<Map<String, String>> weekOffData = readDatafromSheet("WeeklyOff");

        weekoffs = new HashMap<>();

        for (Map<String, String> data : weekOffData) {

            String name = data.get("WeeklyOffName");
            String id = data.get("id");

            weekoffs.put(name, id);

        }
    }

    public void updateLeave(String leaveName, String leaveId, boolean cntWeeklyOff, boolean cntHoliday, String companyID) {

        Map<String, String> body = new HashMap<>();
        body.put("Leaves[name]", leaveName);
        body.put("Leaves[id]", leaveId);
        body.put("Leaves[parent_company_id]", companyID);
        body.put("LeavePolicy_InterveningHolidays[status]", LeaveDeductionsBase.parseToPHP(cntHoliday || cntWeeklyOff));
        body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]", LeaveDeductionsBase.parseToPHP(cntHoliday));
        body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]", LeaveDeductionsBase.parseToPHP(cntWeeklyOff));

        LeaveSettings srvc = new LeaveSettings();
        srvc.updateLeave(body);
    }

    public void applyLeave(Date date, Employee employee, String leaveId, boolean isFirst, boolean isSecond, boolean isApproved) {

        String formattedDate = helper.formatDateTo(date, "yyyy-MM-dd");

        Map headers = new HashMap();
        Map body = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("user_id", employee.getMongoID());
        body.put("from_date", formattedDate);
        body.put("to_date", formattedDate);
        body.put("leave_change", leaveId);
        body.put("is_half_day", LeaveDeductionsBase.parseToPHP(isFirst || isSecond));
        body.put("is_firsthalf_secondhalf", (isFirst ? "1" : "2"));

        body.put("UserLeavesOther", "0");

        if (isApproved) {
            body.put("UserLeavesOther", "1");
        } else {

            headers.put("Cookie", employee.getPhpSessid());
        }

        new LeaveApply().applyLeave(headers, body);

    }

    public void createHoliday(Date date) {
        String formattedDate = helper.formatDateTo(date, "yyyy-MM-dd");

        Holiday holiday = new Holiday();
        holiday.setName(" Holiday " + formattedDate);
        holiday.setDate(formattedDate);

        new HolidayService().createHoliday(holiday);
    }

    public void importBackdated(Map<String, String> body) {
        AttendanceImport srvc = new AttendanceImport();
        srvc.importBackdated(body);
    }

    public void assignPolicyAndShift(String userID, String date) {

        AttendanceImport srvc = new AttendanceImport();

        String shiftId = getShift("Default").getShiftID();
        String policyId = getAttendancePolicy("Default").getPolicyInfo().getPolicyID();
        String weekoffId = getWeeklyOff("None");

        srvc.assignPolicyAndShift(userID, shiftId, policyId, weekoffId, date);

    }

    public void assignPolicyAndShift(String userID, String date, Shift shift, AttendancePolicy policy,String weekoffId) {

        AttendanceImport srvc = new AttendanceImport();
        String shiftId = shift.getShiftID();
        String policyId = policy.getPolicyInfo().getPolicyID();
        srvc.assignPolicyAndShift(userID, shiftId, policyId, weekoffId, date);

    }

    public String getAttendanceStatus(String mongoID, Date date) {

        AttendanceService srvc = new AttendanceService();
        String month = helper.formatDateTo(date, "yyyy-MM");
        String day = helper.formatDateTo(date, "dd MMM yyyy, E");

        return srvc.getAttendanceLogbyDate(mongoID, month, day);

    }

    public Map<String,String> getAttendanceLog(String mongoID, Date date) {

        AttendanceService srvc = new AttendanceService();
        String month = helper.formatDateTo(date, "yyyy-MM");
        String day = helper.formatDateTo(date, "dd MMM yyyy, E");

        return srvc.getCompleteAttendanceLog(mongoID, month, day);

    }


    public JSONObject applyClockin(Map<String,String> headers, int timeInMins, int location, String date ) {
        AttendanceRequestServices srvc = new AttendanceRequestServices();
        return new JSONObject(srvc.applyClockIn(headers, timeInMins, location, date));
    }

    public String getUserAttendanceId(Employee employee) {
        String userId = employee.getUserID();
        RequestServices srvc = new RequestServices();
        List<String> str = srvc.getRequestIds(userId, PENDING, ATTENDANCE_REQUESTS,ALL_REQUESTS );

        return str.get(0);
    }

    public void validateHoliday(boolean isHoliday, String status, TestBase testBase) {
        if (isHoliday && !status.contains(holiday))
              testBase.Reporter(" Holiday is not marked ", "FAIL");
    }

    public void validateWeekoff(boolean isWeekOff, String status, TestBase testBase) {
        if (isWeekOff && !status.contains(weekoff))
                testBase.Reporter(" Weekoff is not marked ", "FAIL");
    }

    public void validateLeave(boolean isApproved, boolean isHalf, String status, String leave, TestBase testBase) {

        if (isApproved) {
            if (isHalf) {
                if (status.contains(approved_halfday + "(" + leave + ")")) {
                    testBase.Reporter(" Approved Half day leave is present ", "PASS");
                } else {
                    testBase.Reporter(" Approved Half day is leave not present ", "FAIL");
                }
            } else {
                if (status.contains(approved+ "(" + leave + ")")) {
                    testBase.Reporter(" Approved Full day leave is present ", "PASS");
                } else {
                    testBase.Reporter(" Approved Full day leave is not present ", "FAIL");
                }
            }
        } else {
            if (isHalf) {
                if (status.contains(pending_halfday + "(" + leave + ")")) {
                    testBase.Reporter(" Pending Half day leave is present ", "PASS");
                } else {
                    testBase.Reporter(" Pending Half day is leave not present ", "FAIL");
                }
            } else {
                if (status.contains(pending+ "(" + leave + ")")) {
                    testBase.Reporter(" Pending Full day leave is present ", "PASS");
                } else {
                    testBase.Reporter(" Pending Full day leave is not present ", "FAIL");
                }
            }
        }
    }

    public void validateNoLeave(String status, String leave, TestBase testBase) {
        if (status.contains( "(" + leave + ")")) {
            testBase.Reporter(" Leave is present which is not expected", "FAIL");
        } else {
            testBase.Reporter(" Leave is not present which is as expected", "PASS");
        }
    }

    public void approveAttendance(String id) {

        RequestServices srvc = new RequestServices();
        srvc.processRequest(id, APPROVE );

    }

    public void validatePolicy(AttendancePolicy policy, Shift shift,Date date, List<AttendancePunch> punches) {

        String status = "";

        punches.sort(Comparator.comparing(AttendancePunch::getTimestamp));

        int totalPunches = punches.size();
        AttendancePunch firstPunch = punches.get(0);
        AttendancePunch lastPunch = punches.get(totalPunches-1);

        BufferTime bfr = policy.getBufferTime() == null ? new BufferTime(): policy.getBufferTime();

        int startTime = shift.getStartTime() - bfr.getInTime();
        int endTime = shift.getEndTime() + bfr.getOutTime();

        Date startDate = helper.setTime(date, startTime);
        Date endDate = helper.setTime(date, endTime);

        //validatePunchList();


    }

    public void changeDate(Date date) {
        CronServices srvc = new CronServices();
        srvc.changeDate(date);
    }

    public void runClockInreminder() {
        CronServices srvc = new CronServices();
        srvc.runClockInReminder();
    }

    public Shift updateShift(Shift shift) {

        ShiftsServices srvc = new ShiftsServices();
        srvc.updateShift(shift.getShiftToMap());
        shift = srvc.getShiftInfo(shift.getShiftName(), shift.getGroupCompany());
        return shift;
    }

    public void runUpdatePolicyContinuous() {
        CronServices srvc = new CronServices();
        srvc.runUpdatePolicyContinuous();
    }

    public void runCron(Attendance attobj, Employee employee) {

        Map<String,String> importBody = new Absent().getAbsent(employee.getEmployeeID(), attobj.getPolicy().getPolicyInfo().getPolicyName(), attobj.getShift().getShiftName(),attobj.getShiftDate() , false);
        importBackdated(importBody);

    }

    public void runBiometricCron() {
        CronServices srvc = new CronServices();
        srvc.runBiometricCron();
    }
}