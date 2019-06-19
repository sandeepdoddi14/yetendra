package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class LateOrEarlyBase extends LeaveDeductionsBase implements Serializable {

    private int count;
    private boolean isHalfDay;
    private boolean isForEvery = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isHalfDay() {
        return isHalfDay;
    }

    public void setHalfDay(boolean halfDay) {
        isHalfDay = halfDay;
    }

    public boolean isForEvery() {
        return isForEvery;
    }

    public void setForEvery(boolean forEvery) {
        isForEvery = forEvery;
    }

    public static boolean compareToSuper(LateOrEarlyBase ld, LateOrEarlyBase ld1) {

        boolean status = ld.getCount() == ld1.getCount() &&
                ld.isHalfDay() == ld1.isHalfDay() &&
                ld.isForEvery() == ld1.isForEvery();

        return status && LeaveDeductionsBase.compareToSuper(ld,ld1 );

    }

    public List<Date> getDatesforDeduction(Date date) {

        DateTimeHelper helper = new DateTimeHelper();

        List<Date> dates = new ArrayList<>();
        for (int i = 0; i < count; i++ ) {
            dates.add(date);
            date = helper.getNextDate(date);
        }

        return dates;
    }



}
