package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbLimitsBody extends Services {

    private List<String> band_grade_desig = new ArrayList<>();
    private List<String> location = new ArrayList<>();

    private String uppercappu;
    private String uppercaponunits;
    private String nooftimespermonth;
    private String maxamtpermonth;
    private String dayspostexpense;
    private String autoapprovallimit;
    private String autocalculate;

    public String getUppercappu() {
        return uppercappu;
    }

    public void setUppercappu(String uppercappu) {
            this.uppercappu = uppercappu;
    }

    public String getUppercaponunits() {
        return uppercaponunits;
    }

    public void setUppercaponunits(String uppercaponunits) {
        this.uppercaponunits = uppercaponunits;
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

    public String getAutocalculate() {
        return autocalculate;
    }

    public void setAutocalculate(String autocalculate) {
        this.autocalculate = autocalculate;
    }

    public List<String> getBand_grade_desig() {
        return band_grade_desig;
    }

    public void setBand_grade_desig(List<String> band_grade_desig) {
        this.band_grade_desig = band_grade_desig;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public void toObject(Map<String, String> data) {
        setUppercappu(data.get("UpperCapPerUnit"));
        setUppercaponunits(data.get("UpperCap"));
        setNooftimespermonth(data.get("NoOfTimesPerMonth"));
        setMaxamtpermonth(data.get("MaxAmountPerPerson"));
        setDayspostexpense(data.get("DaysPostExpense"));
        setAutoapprovallimit(data.get("AutoApprovalLimit"));
        setAutocalculate(data.get("AutoCalculate"));
    }

    public List<NameValuePair> toMap(int count) {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("Reimb_set[" + count + "][rupees]", getUppercappu() + ""));
        body.add(new BasicNameValuePair("Reimb_set[" + count + "][upper_cap_unit]", getUppercappu() + ""));
        body.add(new BasicNameValuePair("Reimb_set[" + count + "][number_of_times]", getNooftimespermonth() + ""));
        body.add(new BasicNameValuePair("Reimb_set[" + count + "][max_in_month]", getMaxamtpermonth() + ""));
        body.add(new BasicNameValuePair("Reimb_set[" + count + "][in_how_many_days]", getDayspostexpense() + ""));
        body.add(new BasicNameValuePair("Reimb_set[" + count + "][auto_approval_limit]", getAutoapprovallimit() + ""));

        if (autocalculate.equalsIgnoreCase("yes"))
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][auto_cal_and_non_editable]", "1"));
        else
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][auto_cal_and_non_editable]", "0"));

        HashMap band = getBands();
        HashMap grade = getGrades();
        String gcName = getData("@@group");
        String  id = getGroupCompanyIds().get(gcName);
        JSONObject designation = getDesignations(id);

        for (String bandGradeDesig : getBand_grade_desig()) {
            if (bandGradeDesig.equalsIgnoreCase("all")) {
                body.add(new BasicNameValuePair("Reimb_set[designation][" + count + "][]", "ALL_0"));
            } else if (bandGradeDesig.equals("BAND")) {
                body.add(new BasicNameValuePair("Reimb_set[designation][" + count + "][]", "BAND_" + bandGradeDesig));
            } else if (bandGradeDesig.equals("GRADE")) {
                body.add(new BasicNameValuePair("Reimb_set[designation][" + count + "][]", "GRADE_" + bandGradeDesig));
            } else {
                body.add(new BasicNameValuePair("Reimb_set[designation][" + count + "][]", bandGradeDesig + ""));
            }
        }

        for (String location : getLocation()) {
            if (getLocation().equals("all"))
                body.add(new BasicNameValuePair("Reimb_set[" + count + "][location][]", "ALL_0"));

            else
                body.add(new BasicNameValuePair("Reimb_set[" + count + "][location][]", location));
        }
        return body;
    }

}
