package com.darwinbox.reimbursement.objects.ReimbCreation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReimbLimitsBody {

    private String band_grade_desig ;
    private String location ;
    private String uppercappu;
    private String uppercap;
    private String nooftimespermonth;
    private String maxamtpermonth;
    private String dayspostexpense;
    private String autoapprovallimit;
    private boolean autocalculate;
    private List<String> fieldValues = new ArrayList<>();

    public String getBand_grade_desig() {
        return band_grade_desig;
    }

    public void setBand_grade_desig(String band_grade_desig) {
        this.band_grade_desig = band_grade_desig;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUppercappu() {
        return uppercappu;
    }

    public void setUppercappu(String uppercappu) {
        this.uppercappu = uppercappu;
    }

    public String getUppercap() {
        return uppercap;
    }

    public void setUppercap(String uppercap) {
        this.uppercap = uppercap;
    }

    public String getNooftimespermonth() {
        return nooftimespermonth;
    }

    public void setNooftimespermonth(String nooftimespermonth) {
        this.nooftimespermonth = nooftimespermonth;
    }

    public String getMaxamtpermonth() {
        return maxamtpermonth;
    }

    public void setMaxamtpermonth(String maxamtpermonth) {
        this.maxamtpermonth = maxamtpermonth;
    }

    public String getDayspostexpense() {
        return dayspostexpense;
    }

    public void setDayspostexpense(String dayspostexpense) {
        this.dayspostexpense = dayspostexpense;
    }

    public String getAutoapprovallimit() {
        return autoapprovallimit;
    }

    public void setAutoapprovallimit(String autoapprovallimit) {
        this.autoapprovallimit = autoapprovallimit;
    }

    public boolean isAutocalculate() {
        return autocalculate;
    }

    public void setAutocalculate(boolean autocalculate) {
        this.autocalculate = autocalculate;
    }

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public  void toObject(Map<String,String> data)
    {
        setBand_grade_desig(data.get("Band_grade_desig"));
        setLocation(data.get("Location"));
        setUppercap(data.get("Uppercappu"));
    }

    public List<NameValuePair> toMap()
    {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("Reimb_set[0][designation][]",getBand_grade_desig()));
        body.add(new BasicNameValuePair("Reimb_set[0][location][]",getLocation()));
        body.add(new BasicNameValuePair("Reimb_set[0][rupees]",getUppercap()));
        body.add(new BasicNameValuePair("Reimb_set[0][upper_cap_unit]", getUppercappu()));

        return  body;
    }
}
