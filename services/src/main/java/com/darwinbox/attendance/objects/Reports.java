package com.darwinbox.attendance.objects;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reports {

    public String module;
    public String reportType;
    public String reportFilter;
    public String subCriteria;
    public String monthAndYear;
    public String fromDate;
    public String toDate;



    public enum employeeTypes {

        activeEmployees("1"), inactiveEmployees("2"), pendingEmployees("3"), allEmployees("0");

        public final String value;
        employeeTypes(String value) {
            this.value = value;
        }
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportFilter() {
        return reportFilter;
    }

    public void setReportFilter(String reportFilter) {
        this.reportFilter = reportFilter;
    }

    public String getSubCriteria() {
        return subCriteria;
    }

    public void setSubCriteria(String subCriteria) {
        this.subCriteria = subCriteria;
    }

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }




    public Map<String, String> toMap(String monthOrCustom) {

        Map<String, String> body = new HashMap<>();

        body.put("roster_id",getReportType());
        body.put("main_roster",getModule());
        body.put("filter_id[]",getReportFilter());
        body.put("active_inactive",getSubCriteria());

        if(monthOrCustom.equalsIgnoreCase("month"))
            body.put("month_year",getMonthAndYear());
        if(monthOrCustom.equalsIgnoreCase("custom")){
            body.put("fromDate",getFromDate());
            body.put("toDate",getToDate());
        }

        return body;
    }
    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public List<String> filter;

    public List<NameValuePair> todoMap(String monthOrCustom) {

        List<NameValuePair> list = new ArrayList<>();

        list.add(new BasicNameValuePair("roster_id",getReportType()));
        list.add(new BasicNameValuePair("main_roster",getModule()));
        for (String getFilters  : getFilter()) {
            list.add(new BasicNameValuePair("filter_id[]",getFilters));
        }
        list.add(new BasicNameValuePair("active_inactive",getSubCriteria()));


        if(monthOrCustom.equalsIgnoreCase("month"))
            list.add(new BasicNameValuePair("month_year",getMonthAndYear()));
        if(monthOrCustom.equalsIgnoreCase("custom")){
            list.add(new BasicNameValuePair("fromDate",getFromDate()));
            list.add(new BasicNameValuePair("toDate",getToDate()));
        }

        return list;
    }
}
