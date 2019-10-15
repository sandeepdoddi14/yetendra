package com.darwinbox.reimbursement.objects.ReimbCreation;

import java.util.ArrayList;
import java.util.List;

public class ReimbFormBody {

    private List<String> band_grade_desig = new ArrayList<>();
    private List<String> location = new ArrayList<>();
    private String uppercappu;
    private String uppercap;
    private String nooftimespermonth;
    private String maxamtpermonth;
    private String dayspostexpense;
    private String autoapprovallimit;
    private boolean autocalculate;
    private List<String> fieldValues = new ArrayList<>();

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




}
