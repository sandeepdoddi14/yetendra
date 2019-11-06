package com.darwinbox.core.company.objects;

import java.util.HashMap;

public class GroupCompany {
    private String id;

    private String groupCompanyName="";
    private String groupCompanyCode="";
    private String country="";
    private String state="";
    private String city="";
    private String url="";
    private String headOfDepartment="";
    private String companyLogo="binary";


    //Comapny Deatials
    private String companyType="public";
    private String dateOfCorporation="";
    private String panNo="";
    private String tanNo="";
    private String vatRegistration="";
    private String gstRegistartion="";
    private String cstRegistration="";
    private String pfRegistration="";
    private String esiRegistration="";

    public String getGroupCompanyName() {
        return groupCompanyName;
    }

    public void setGroupCompanyName(String groupCompanyName) {
        this.groupCompanyName = groupCompanyName;
    }

    public String getGroupCompanyCode() {
        return groupCompanyCode;
    }

    public void setGroupCompanyCode(String groupCompanyCode) {
        this.groupCompanyCode = groupCompanyCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getDateOfCorporation() {
        return dateOfCorporation;
    }

    public void setDateOfCorporation(String dateOfCorporation) {
        this.dateOfCorporation = dateOfCorporation;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getTanNo() {
        return tanNo;
    }

    public void setTanNo(String tanNo) {
        this.tanNo = tanNo;
    }

    public String getVatRegistration() {
        return vatRegistration;
    }

    public void setVatRegistration(String vatRegistration) {
        this.vatRegistration = vatRegistration;
    }

    public String getGstRegistartion() {
        return gstRegistartion;
    }

    public void setGstRegistartion(String gstRegistartion) {
        this.gstRegistartion = gstRegistartion;
    }

    public String getCstRegistration() {
        return cstRegistration;
    }

    public void setCstRegistration(String cstRegistration) {
        this.cstRegistration = cstRegistration;
    }

    public String getPfRegistration() {
        return pfRegistration;
    }

    public void setPfRegistration(String pfRegistration) {
        this.pfRegistration = pfRegistration;
    }

    public String getEsiRegistration() {
        return esiRegistration;
    }

    public void setEsiRegistration(String esiRegistration) {
        this.esiRegistration = esiRegistration;
    }



    public HashMap<String,String> toMap(){
        HashMap<String,String> map=new HashMap<>();

        map.put("SubTenantProfile[tenant_name]",getGroupCompanyName());

        //if(getGroupCompanyCode()!=null || getGroupCompanyCode()!="")
            map.put("SubTenantProfile[tenant_code]",getGroupCompanyCode());
        //else
          //  map.put("SubTenantProfile[tenant_code]","");

        map.put("SubTenantProfile[country]",getCountry());
        map.put("SubTenantProfile[state]",getState());
        map.put("SubTenantProfile[city]",getCity());

        //if(getUrl()!=null || getUrl()!="")
            map.put("SubTenantProfile[site_url]",getUrl());
        //else
          //  map.put("SubTenantProfile[site_url]","");

        //if(getHeadOfDepartment()!=null || getHeadOfDepartment()!="")
            map.put("SubTenantProfile[head_lable_dept]",getHeadOfDepartment());
        //else
            //map.put("SubTenantProfile[head_lable_dept]","");

       // if(getCompanyLogo()!=null || getCompanyLogo()!="")
        map.put("company_logo:",getCompanyLogo());
       // else
         //   map.put("company_logo:","(binary)");

        //if(getCompanyType()!=null || getCompanyType()!="")
        map.put("SubTenantProfile[company_type]",getCompanyType());
        //else
          //  map.put("SubTenantProfile[company_type]","");


        map.put("SubTenantProfile[date_of_incorporation]",getDateOfCorporation());
            //if(getPanNo()!=null || getPanNo()!="")
        map.put("SubTenantProfile[pan_no]",getPanNo());
       // else
          //  map.put("SubTenantProfile[pan_no]","");

        map.put("SubTenantProfile[tan_no]",getTanNo());
        map.put("SubTenantProfile[vat_registration]",getVatRegistration());
        map.put("SubTenantProfile[gst_registration]",getGstRegistartion());
        map.put("SubTenantProfile[cst_registration]",getCstRegistration());
        map.put("SubTenantProfile[pf_registration]",getPfRegistration());
        map.put("SubTenantProfile[esi_registration]",getEsiRegistration());



        return  map;
    }
}
