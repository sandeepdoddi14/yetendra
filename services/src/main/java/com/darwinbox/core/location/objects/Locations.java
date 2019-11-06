package com.darwinbox.core.location.objects;

import com.darwinbox.HelperServices;
import com.darwinbox.Services;
import com.darwinbox.core.services.CostCenterServices;
import com.darwinbox.core.services.location.CityTypeServices;

import java.util.HashMap;

public class Locations {
    private String id;

    private String forCompanies="";
    private String cityType="";
    private String officeAddress="";
    private String officeArea="";
    private String country="";
    private String state="";
    private String officeCity="";
    private String pinCode="";
    private String officeEmail="";
    private String mobileNo="";
    private String telNo="";
    private Boolean isRegiteredOffice=false;
    private String costCenter="";
    private String workAreaCode="";
    private String locationArea="";

   public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForCompanies() {
        return forCompanies;
    }

    public void setForCompanies(String forCompanies) {
        this.forCompanies = forCompanies;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOfficeArea() {
        return officeArea;
    }

    public void setOfficeArea(String officeArea) {
        this.officeArea = officeArea;
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

    public void setState(String state) {
        this.state = state;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public Boolean getRegiteredOffice() {
        return isRegiteredOffice;
    }

    public void setRegiteredOffice(Boolean regiteredOffice) {
        isRegiteredOffice = regiteredOffice;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getWorkAreaCode() {
        return workAreaCode;
    }

    public void setWorkAreaCode(String workAreaCode) {
        this.workAreaCode = workAreaCode;
    }

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }


    public HashMap<String,String> toMap(){
       HashMap<String,String> map= new HashMap<>();

       map.put("TenantOffices[parent_company_array][]",new Services().getGroupCompanyIds().get(getForCompanies()));
       map.put("TenantOffices[city_type]",new CityTypeServices().getCityTypes().get(getCityType()));
       map.put("TenantOffices[address]",getOfficeAddress());
       map.put("TenantOffices[area]",getOfficeArea());

       String countryID=new HelperServices().getCountries().get(getCountry())+"";
       map.put("TenantOffices[country]",countryID);

       String stateID=new HelperServices().getStates(getCountry()).get(getState());
       map.put("TenantOffices[state]",stateID);

       map.put("TenantOffices[city]",new  HelperServices().getCities(stateID).get(getOfficeCity()));

       map.put("TenantOffices[pin_code]",getPinCode());
       map.put("TenantOffices[email]",getOfficeEmail());
       map.put("TenantOffices[mobile_no]",getMobileNo());
       map.put("TenantOffices[tel_no]",getTelNo());
       map.put("TenantOffices[registered_office]",getRegiteredOffice()?"1":"0");
       map.put("TenantOffices[cost_center]",new CostCenterServices().getCostCenters().get(getCostCenter()));
       map.put("TenantOffices[work_area_code]",getWorkAreaCode());
       map.put("TenantOffices[location_area]",getLocationArea());

       return  map;
    }
}
