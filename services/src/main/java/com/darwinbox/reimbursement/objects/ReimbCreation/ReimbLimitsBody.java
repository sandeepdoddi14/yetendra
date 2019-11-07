package com.darwinbox.reimbursement.objects.ReimbCreation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReimbLimitsBody {

    private List<String> band_grade_desig = new ArrayList<>() ;
    private List<String> location = new ArrayList<>();

    private float uppercappu;
    private int uppercaponunits;
    private int nooftimespermonth;
    private float maxamtpermonth;
    private int dayspostexpense;
    private float autoapprovallimit;
    private boolean autocalculate;

    public float getUppercappu() {
        return uppercappu;
    }

    public void setUppercappu(float uppercappu) {
        this.uppercappu = uppercappu;
    }

    public int getUppercaponunits() {
        return uppercaponunits;
    }

    public void setUppercaponunits(int uppercaponunits) {
        this.uppercaponunits = uppercaponunits;
    }

    public int getNooftimespermonth() {
        return nooftimespermonth;
    }

    public void setNooftimespermonth(int nooftimespermonth) {
        this.nooftimespermonth = nooftimespermonth;
    }

    public float getMaxamtpermonth() {
        return maxamtpermonth;
    }

    public void setMaxamtpermonth(float maxamtpermonth) {
        this.maxamtpermonth = maxamtpermonth;
    }

    public int getDayspostexpense() {
        return dayspostexpense;
    }

    public void setDayspostexpense(int dayspostexpense) {
        this.dayspostexpense = dayspostexpense;
    }

    public float getAutoapprovallimit() {
        return autoapprovallimit;
    }

    public void setAutoapprovallimit(float autoapprovallimit) {
        this.autoapprovallimit = autoapprovallimit;
    }

    public boolean isAutocalculate() {
        return autocalculate;
    }

    public void setAutocalculate(boolean autocalculate) {
        this.autocalculate = autocalculate;
    }

    public List<String> getBand_grade_desig() { return band_grade_desig;}

    public void setBand_grade_desig(List<String> band_grade_desig) {this.band_grade_desig = band_grade_desig; }

    public List<String> getLocation() { return location; }

    public void setLocation(List<String> location) { this.location = location; }

    public  void toObject(Map<String,String> data)
    {
        setUppercappu(Float.parseFloat(data.get("UpperCapPerUnit")));
        setUppercaponunits(Integer.parseInt(data.get("UpperCap")));
        setNooftimespermonth(Integer.parseInt(data.get("NoOfTimesPerMonth")));
        setMaxamtpermonth(Float.parseFloat("MaxAmountPerPerson"));
        setDayspostexpense(Integer.parseInt("DaysPostExpense"));
        setAutoapprovallimit(Float.parseFloat("AutoApprovalLimit"));
        setAutocalculate(Boolean.parseBoolean("AutoCalculate"));
    }

    public List<NameValuePair> toMap(int count)
    {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("Reimb_set["+count+"][rupees]",getUppercappu()+""));
        body.add(new BasicNameValuePair("Reimb_set["+count+"][upper_cap_unit]", getUppercappu()+""));
        body.add(new BasicNameValuePair("Reimb_set["+count+"][number_of_times]",getNooftimespermonth()+""));
        body.add(new BasicNameValuePair("Reimb_set["+count+"][max_in_month]",getMaxamtpermonth()+""));
        body.add(new BasicNameValuePair("Reimb_set["+count+"][in_how_many_days]",getDayspostexpense()+""));
        body.add(new BasicNameValuePair("Reimb_set["+count+"][auto_cal_and_non_editable]",isAutocalculate()+""));
        body.add(new BasicNameValuePair("Reimb_set["+count+"][auto_approval_limit]",getAutoapprovallimit()+""));

        for(String bandGradeDesig : getBand_grade_desig()) {
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][designation][]", bandGradeDesig));
        }

        for(String location : getLocation()) {
            body.add(new BasicNameValuePair("Reimb_set["+count+"][location][]", location));
        }
        return  body;
    }

}
