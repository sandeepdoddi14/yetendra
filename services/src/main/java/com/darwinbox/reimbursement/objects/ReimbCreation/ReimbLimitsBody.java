package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import com.darwinbox.framework.uiautomation.base.TestBase;
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

    public void addBandGradeDesig(String bgd) {
        band_grade_desig.add(bgd);
    }

    public void addLocation(String loc) {
        location.add(loc);
    }

    public void toObject(Map<String, String> data) {
        setUppercappu(data.get("UpperCapPerUnit"));
        setUppercaponunits(data.get("UpperCap"));
        setNooftimespermonth(data.get("NoOfTimesPerMonth"));
        setMaxamtpermonth(data.get("MaxAmountPerPerson"));
        setDayspostexpense(data.get("DaysPostExpense"));
        setAutoapprovallimit(data.get("AutoApprovalLimit"));
        setAutocalculate(data.get("AutoCalculate"));

        String temp_bgd = data.get("Band_Grade_Designation");
        if (temp_bgd.length() != 0) {
            for (String bgd : temp_bgd.split(",")) {
                addBandGradeDesig(bgd);
            }
        }
        String temp_loc = data.get("Location");
        if (temp_loc.length() != 0) {
            for (String loc : temp_loc.split(",")) {
                addLocation(loc);
            }
        }
    }

    public List<NameValuePair> toMap(int count, String mode) {
        List<NameValuePair> body = new ArrayList<>();
        ReimbForm reimbForm = new ReimbForm();

        if (mode == "create") {
            body.add(new BasicNameValuePair("Reimb_set[rupees][]", getUppercappu() + ""));
            body.add(new BasicNameValuePair("Reimb_set[upper_cap_unit][]", getUppercappu() + ""));
            body.add(new BasicNameValuePair("Reimb_set[number_of_times][]", getNooftimespermonth() + ""));
            body.add(new BasicNameValuePair("Reimb_set[max_in_month][]", getMaxamtpermonth() + ""));
            body.add(new BasicNameValuePair("Reimb_set[in_how_many_days][]", getDayspostexpense() + ""));
            body.add(new BasicNameValuePair("Reimb_set[auto_approval_limit][]", getAutoapprovallimit() + ""));

            String keyBgd = "Reimb_set[designation][" + count + "][]";
          /*  List<NameValuePair> nvp_bgd = chooseApplicableTo(band_grade_desig, reimbForm.companyId, keyBgd);
            for (NameValuePair nv : nvp_bgd) {
                body.add(nv);
            }
          */  String keyloc = "Reimb_set[location][" + count + "][]";
          /*  List<NameValuePair> nvp_loc = chooseApplicableTo(location, reimbForm.companyId, keyloc);
            for (NameValuePair nv : nvp_loc) {
                body.add(nv);
            }
          */  if (autocalculate.equalsIgnoreCase("yes"))
                body.add(new BasicNameValuePair("Reimb_set[auto_cal_and_non_editable][]", "1"));
            else
                body.add(new BasicNameValuePair("Reimb_set[auto_cal_and_non_editable][]", "0"));
        }
        if (mode == "edit") {
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][rupees]", getUppercappu() + ""));
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][upper_cap_unit]", getUppercappu() + ""));
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][number_of_times]", getNooftimespermonth() + ""));
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][max_in_month]", getMaxamtpermonth() + ""));
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][in_how_many_days]", getDayspostexpense() + ""));
            body.add(new BasicNameValuePair("Reimb_set[" + count + "][auto_approval_limit]", getAutoapprovallimit() + ""));

            String keyBgd = "Reimb_set[" + count + "][designation][]";
         /*   List<NameValuePair> nvp_bgd = chooseApplicableTo(band_grade_desig, reimbForm.companyId, keyBgd);
            for (NameValuePair nv : nvp_bgd) {
                body.add(nv);
            }
         */   String keyloc = "Reimb_set[" + count + "][location][]";
           /* List<NameValuePair> nvp_loc = chooseApplicableTo(location, reimbForm.companyId, keyloc);
            for (NameValuePair nv : nvp_loc) {
                body.add(nv);
            }
*/
            if (autocalculate.equalsIgnoreCase("yes"))
                body.add(new BasicNameValuePair("Reimb_set[" + count + "][auto_cal_and_non_editable][]", "1"));
            else
                body.add(new BasicNameValuePair("Reimb_set[" + count + "][auto_cal_and_non_editable][]", "0"));

        }
              /* for (String location : getLocation()) {
                if (getLocation().equals("all"))
                    body.add(new BasicNameValuePair("Reimb_set[" + count + "][location][]", "ALL_0"));
                else
                    body.add(new BasicNameValuePair("Reimb_set[" + count + "][location][]", location));
            }
            for (String bgd : getBand_grade_desig()) {
                if (getLocation().equals("all"))
                    body.add(new BasicNameValuePair("Reimb_set[" + count + "][]", "ALL_0"));

                else
                    body.add(new BasicNameValuePair("Reimb_set[" + count + "][designation][]", bgd));
            }
*/
        return body;
    }
}

