package com.darwinbox.core.company.objects;

import java.util.HashMap;
import java.util.List;

public class NoticePeriod {
    private String id;

    private String noticeName="";
    private String noOfDaysForNotice="";
    private String noOfDaysForNoticeInProbation="";
    private List<String> applicableForIDS;
    private Boolean managerOrReviewerWillBeAbleToEditTheRecoveryDays=false;
    private Boolean calculateNoticePeriodFromResignationDate=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoOfDaysForNotice() {
        return noOfDaysForNotice;
    }

    public void setNoOfDaysForNotice(String noOfDaysForNotice) {
        this.noOfDaysForNotice = noOfDaysForNotice;
    }

    public String getNoOfDaysForNoticeInProbation() {
        return noOfDaysForNoticeInProbation;
    }

    public void setNoOfDaysForNoticeInProbation(String noOfDaysForNoticeInProbation) {
        this.noOfDaysForNoticeInProbation = noOfDaysForNoticeInProbation;
    }

    public List<String> getApplicableFor() {
        return applicableForIDS;
    }

    public void setApplicableFor(List<String> applicableFor) {
        this.applicableForIDS = applicableFor;
    }

    public Boolean getManagerOrReviewerWillBeAbleToEditTheRecoveryDays() {
        return managerOrReviewerWillBeAbleToEditTheRecoveryDays;
    }

    public void setManagerOrReviewerWillBeAbleToEditTheRecoveryDays(Boolean managerOrReviewerWillBeAbleToEditTheRecoveryDays) {
        this.managerOrReviewerWillBeAbleToEditTheRecoveryDays = managerOrReviewerWillBeAbleToEditTheRecoveryDays;
    }

    public Boolean getCalculateNoticePeriodFromResignationDate() {
        return calculateNoticePeriodFromResignationDate;
    }

    public void setCalculateNoticePeriodFromResignationDate(Boolean calculateNoticePeriodFromResignationDate) {
        this.calculateNoticePeriodFromResignationDate = calculateNoticePeriodFromResignationDate;
    }


    public HashMap<String,String> toMap(){
        HashMap<String,String> map= new HashMap<>();

        map.put("NoticePeriod[notice_name]",getNoticeName());
        map.put("NoticePeriod[number_of_days]",getNoOfDaysForNotice());
        map.put("NoticePeriod[number_of_days_probation]",getNoOfDaysForNoticeInProbation());

        //PASSING MONGO IDS DIRECTLY TO THE PROPERTY
        for(String temp:applicableForIDS)
        {
            map.put("NoticePeriod[for][]",temp);
        }

        map.put("NoticePeriod[manager_edit]",getManagerOrReviewerWillBeAbleToEditTheRecoveryDays()?"1":"0");
        map.put("NoticePeriod[from_resignation_date]",getCalculateNoticePeriodFromResignationDate()?"1":"0");

        return map;
    }

}
