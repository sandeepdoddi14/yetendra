package com.darwinbox.attendance.utils;

import com.darwinbox.attendance.objects.Attendance;
import com.darwinbox.attendance.objects.AttendancePunch;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.others.BufferTime;
import com.darwinbox.attendance.objects.policy.others.PolicyInfo;
import com.darwinbox.framework.utils.DatabaseUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendancePunchProcessor {

    public static void main(String[] args) throws Exception {


        float f1 = (float) 28.179; // total balance
        float f2 = (float)12.657; // carry fwd

        System.out.println(f1 - f2 );


        double dd1 = 28.179;
        double dd2 = 12.657;

        System.out.println(dd1 -dd2);

        BigDecimal bd1 = new BigDecimal("28.179");
        BigDecimal bd2 = new BigDecimal("12.657");

        System.out.println(bd1.subtract(bd2));

        System.exit(0);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date d = sdf.parse("2019-05-21"  );

        sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

        AttendancePunch d4 =  new AttendancePunch((sdf.parse("2019-05-21 11:35:22"  )), 1);
        AttendancePunch d2 =  new AttendancePunch((sdf.parse("2019-05-21 13:46:25"  )), 0);
        AttendancePunch d3 =  new AttendancePunch((sdf.parse("2019-05-21 18:45:12"  )), 0);
        AttendancePunch d1 =  new AttendancePunch((sdf.parse("2019-05-21 08:55:47"  )), 0);

        Shift shift = new Shift();
        shift.setStartTime("09:00");
        shift.setEndTime("18:00");

        shift.setOverNightShift(false);

        AttendancePolicy policy = new AttendancePolicy();
        policy.setPolicyInfo(new PolicyInfo());
        policy.getPolicyInfo().setGraceTimeIn("15");
        policy.getPolicyInfo().setGraceTimeOut("10");

        BufferTime buffer = new BufferTime();
        buffer.setInTimeHrs("2");
        buffer.setInTimeMins("5");
        buffer.setOutTimeMins("5");
        buffer.setOutTimeHrs("5");
        policy.setBufferTime(buffer);


        List<AttendancePunch> punches  = new ArrayList<>();
        punches.add(d1);
        punches.add(d2);
        punches.add(d3);   punches.add(d4);

        String machine_id = "DBOX16";
        String employeeId = "DBOX16";
        String query = "INSERT INTO `biometric`.`emp_att` (`machine_id`, `employee_id`, `timestamp`, `status`) VALUES ";

        String val_query = "";

        for ( AttendancePunch punch : punches) {

            val_query += ", (\""+machine_id+"\",\""+ employeeId+"\",\"" + punch.getTimestamp()+"\","+
                    ((true || false )? punch.getStatus() : 99 )+" )";
        }

        System.out.println(query + val_query);

        try {
            DatabaseUtils dbu = new DatabaseUtils();
            dbu.loadDriver("com.mysql.jdbc.Driver");
            dbu.getConnection("jdbc:mysql://biometric.qa.darwinbox.io:3306/biometric","darwinbox","darwinbox123");
            int n  = dbu.executeUpdate("select * from devices");

        }catch(Exception e){

            e.printStackTrace();
        }



        Attendance attobj = new Attendance();
        attobj.setShiftDate(d);
        attobj.setPunches(punches);
        attobj.setShift(shift);
        attobj.setPolicy(policy);

        attobj.setDirectionality(true);
        attobj.setEmcure(true);


        attobj.validateBuffer();
        attobj.validatePunches();

        attobj.calculateDurations();

        System.out.println( "First ClockIn : " + attobj.getRecorded_ClockIn());
        System.out.println( "First ClockOut : " + attobj.getRecorded_ClockOut());

        System.out.println(" Shift Date : " + attobj.getShiftDate());
        System.out.println(" Shift Start Time : " + attobj.getStartBufferTime());
        System.out.println(" Shift End Time : " + attobj.getEndBufferTime());
        System.out.println(" Punch In : " + attobj.getClockIn());
        System.out.println(" Punch Out : " + attobj.getClockOut());

        System.out.println(" Total Duration : "  + attobj.getTotalWorkDuration());
        System.out.println(" Final Duration : "  + attobj.getFinalWorkDuration());
        System.out.println(" Break Duration : "  + attobj.getBreakDuration());


    }


    private static void validatePunches(Attendance attendance) {



    }



}


