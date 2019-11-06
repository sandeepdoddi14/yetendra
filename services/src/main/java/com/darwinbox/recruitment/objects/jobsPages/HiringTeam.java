package com.darwinbox.recruitment.objects.jobsPages;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiringTeam {

    private String hiringLead;
   private List<String> screeningTeam;
   private List<String> schedulingTeam;
   private List<String> hiringGroup;
   private boolean isInterviewer;
    public String getHiringLead() {
        return hiringLead;
    }

    public void setHiringLead(String hiringLead) {
        this.hiringLead = hiringLead;
    }


    public List<String> getScreeningTeam() {
        return screeningTeam;
    }

    public void setScreeningTeam(List<String> screeningTeam) {
        this.screeningTeam = screeningTeam;
    }

    public List<String> getSchedulingTeam() {
        return schedulingTeam;
    }

    public void setSchedulingTeam(List<String> schedulingTeam) {
        this.schedulingTeam = schedulingTeam;
    }

    public List<String> getHiringGroup() {
        return hiringGroup;
    }

    public void setHiringGroup(List<String> hiringGroup) {
        this.hiringGroup = hiringGroup;
    }

    public boolean isInterviewer() {
        return isInterviewer;
    }

    public void setInterviewer(boolean interviewer) {
        isInterviewer = interviewer;
    }

    public List<NameValuePair> toMapFourthPage() {

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("edit","1"));
        list.add(new BasicNameValuePair("RaiseRequisition[hiring_lead]",getHiringLead()));

        if (getScreeningTeam().size() >= 1) {
            for (String screeningteam : screeningTeam)
                list.add(new BasicNameValuePair("RaiseRequisition[screening_team]", screeningteam));
        }
        if (getSchedulingTeam().size() >= 1) {
            for (String schedulingteam : schedulingTeam)
                list.add(new BasicNameValuePair("RaiseRequisition[scheduling_team]", schedulingteam));
        }
        if (getHiringGroup().size() >= 1) {
            for (String hiringgroup : hiringGroup)
                list.add(new BasicNameValuePair("RaiseRequisition[hiring_group]", hiringgroup));
        }
        list.add(new BasicNameValuePair("RaiseRequisition[cc_email_offer_letter]",""));
        list.add(new BasicNameValuePair("RaiseRequisition[view_rights_interviewer]", LeaveDeductionsBase.parseToPHP(isInterviewer())));

        return list;
    }

    public void toObjectFourthPage(Map<String, String> body) {

        setHiringLead(body.get(""));
        setInterviewer(LeaveDeductionsBase.getFilter(body,"","true"));

        String temp = body.get("");
        if (temp.length() != 0) {

            String screening[] = temp.split(",");

            screeningTeam = new ArrayList<>();
            for (String value : screening) {
                screeningTeam.add(value);
            }
            setScreeningTeam(screeningTeam);
        }
         temp = body.get("");
        if (temp.length() != 0) {

            String scheduling[] = temp.split(",");

            schedulingTeam = new ArrayList<>();
            for (String value : scheduling) {
                schedulingTeam.add(value);
            }
            setSchedulingTeam(schedulingTeam);
        }
         temp = body.get("");
        if (temp.length() != 0) {

            String hiring[] = temp.split(",");

            hiringGroup = new ArrayList<>();
            for (String value : hiring) {
                hiringGroup.add(value);
            }
            setHiringGroup(hiringGroup);
        }

    }
    }
