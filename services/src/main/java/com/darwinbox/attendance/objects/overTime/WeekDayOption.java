package com.darwinbox.attendance.objects.overTime;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;

import java.util.HashMap;
import java.util.Map;

public class WeekDayOption extends OverTimePolicyOptions {


    public Map<String,String> selectWeekDays() {

        Map<String, String> body = new HashMap<>();
      //
        body.put("Compoff_Weekdays[status]","0");
        body.put("Compoff_Weekdays[status]","4");
        body.put("Compoff_Weekdays[min_compoff_duration]",getMinDurationForOT());
        body.put("Compoff_Weekdays[compoff_approval_status]", LeaveDeductionsBase.parseToPHP(isRequiresApproval()));
        body.put("Compoff_Weekdays[approval_to_l1]",LeaveDeductionsBase.parseToPHP(isRequiresL1manager()));
        body.put("Compoff_Weekdays[approval_to_l2]",LeaveDeductionsBase.parseToPHP(isRequiresL2manager()));
        body.put("Compoff_Weekdays[approval_to_admin]",LeaveDeductionsBase.parseToPHP(isRequiresAdmin()));
        body.put("Compoff_Weekdays[pre_duration]",getMaxOTBeforeShift());
        body.put("Compoff_Weekdays[post_duration]",getMaxOTAfterShift());
        body.put("Compoff_Weekdays[per_day_ot_hours]",getMaxOTPerDay());
        body.put("Compoff_Weekdays[per_week_ot_hours]",getMaxOTPerWeek());
        body.put("Compoff_Weekdays[per_month_ot_hours]",getMaxOTPerMonth());
        body.put("Compoff_Weekdays[per_quater_ot_hours]",getMaxOTPerQuater());

        body.put("Compoff_Weekdays[cal_acc]",LeaveDeductionsBase.parseToPHP(isRestrictionOnAccrual()));
        body.put("Compoff_Weekdays[payout_status]",LeaveDeductionsBase.parseToPHP(isOTcompensatedVia()));

        if(isRestrictionOnAccrual()){
            body.put("Compoff_Weekdays[minimum_duration_required_to_credit_full_day]",getMinDurationToCreditForDay());
            body.put("Compoff_Weekdays[compoff_for_half_day]",LeaveDeductionsBase.parseToPHP(isCreditForHalfDay()));
            body.put("Compoff_Weekdays[min_compoff_duration_for_halfday]",getDurationToCreditForHalfDay());
            body.put("Compoff_Weekdays[compoff_onetime_extended_accural]",LeaveDeductionsBase.parseToPHP(isAllowCreateMoreThan1CompOff()));
        }
        else{
            body.put("Compoff_Weekdays[min_duration_required_for_acc]",getMinDurationForAccrue());
            body.put("Compoff_Weekdays[minimum_duration_required_to_credit_full_day]",getMinDurationToCreditForDay());
        }


        return body;
    }

    public void toObject(Map<String, String> body) {

        setMinDurationForOT(body.get("MinDuration"));
        setRequiresApproval(LeaveDeductionsBase.getFilter(body,"RequiresApproval","true"));
        setRequiresL1manager(LeaveDeductionsBase.getFilter(body,"L1ManagerApproval","true"));
        setRequiresL2manager(LeaveDeductionsBase.getFilter(body,"L2ManagerApproval","true"));
        setRequiresAdmin(LeaveDeductionsBase.getFilter(body,"AdminApproval","true"));
        setMaxOTBeforeShift(body.get("MaxBeforeShift"));
        setMaxOTAfterShift(body.get("MaxAfterShift"));
        setMaxOTPerDay(body.get("MaxPerDay"));
        setMaxOTPerWeek(body.get("MaxPerWeek"));
        setMaxOTPerMonth(body.get("MaxPerMonth"));
        setMaxOTPerQuater(body.get("MaxPerQuater"));
        
        setOTcompensatedVia(LeaveDeductionsBase.getFilter(body,"OTCompensatedVia","true"));
        setRestrictionOnAccrual(LeaveDeductionsBase.getFilter(body,"RestrictionOnAccrual","true"));
        setMinDurationToCreditForDay(body.get("minDurationToCreditForDay"));
        setMinDurationForAccrue(body.get("minDurationForAccrue"));
        setCreditForHalfDay(LeaveDeductionsBase.getFilter(body,"creditForHalfDay","true"));
        setDurationToCreditForHalfDay(body.get("durationToCreditForHalfDay"));
        setAllowCreateMoreThan1CompOff(LeaveDeductionsBase.getFilter(body,"allowMoreThan1compoff","true"));


    }
    }
