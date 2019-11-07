package com.darwinbox.recruitment.objects.jobsPages;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HiringWFThirdPage {


    private boolean isInterviewsParallel;
    private boolean isPreOffer;

    private List<HiringWFThirdPageBody> hiringWFThirdPageBody = new ArrayList<>();


    public boolean isInterviewsParallel() {
        return isInterviewsParallel;
    }

    public void setInterviewsParallel(boolean interviewsParallel) {
        isInterviewsParallel = interviewsParallel;
    }

    public boolean isPreOffer() {
        return isPreOffer;
    }

    public void setPreOffer(boolean preOffer) {
        isPreOffer = preOffer;
    }

    public List<NameValuePair> toMapThirdPage() {

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("yt1","SAVE & CONTINUE"));
        list.add(new BasicNameValuePair("edit","1"));
        list.add(new BasicNameValuePair("RaiseRequisition[series_parallel]", LeaveDeductionsBase.parseToPHP(isInterviewsParallel()))); //boolean
        list.add(new BasicNameValuePair("RaiseRequisition[pre_offer]",LeaveDeductionsBase.parseToPHP(isPreOffer()))); //boolean

        //first-step checkbox, takes input as "on" or ""
        list.add(new BasicNameValuePair("shortlist_status","on"));

        for (HiringWFThirdPageBody HWbody : hiringWFThirdPageBody) {

            list.addAll(list.size(), (HWbody.toMapThirdPageBody()));
        }

        return list;
    }
    public void toObjectThirdPage(Map<String, String> body) {

        setInterviewsParallel(LeaveDeductionsBase.getFilter(body,"","true"));
        setPreOffer(LeaveDeductionsBase.getFilter(body,"","true"));



    }


}
