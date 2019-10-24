package com.darwinbox.leaves.Objects.LeavePolicyObject.Accural;

public class Credit_On_Accural_Basis {
    private Boolean indicator=false;
    //Accural time frame
    private Boolean month=false;
    private Boolean beginOfMonth=false;
    private Boolean endOfMonth=false;
    private Boolean quarter=false;
    private Boolean beginOfQuarter=false;
    private Boolean endOfQuarter=false;
    private Boolean biAnnual=false;

    public ConsiderWorkingDays getConsiderWorkingDays() {
        return considerWorkingDays;
    }

    public void setConsiderWorkingDays(ConsiderWorkingDays considerWorkingDays) {
        this.considerWorkingDays = considerWorkingDays;
    }

    private ConsiderWorkingDays considerWorkingDays=new ConsiderWorkingDays();



    public LapseUnsedAfterCertainMonths getLapseUnsedAfterCertainMonths() {
        return lapseUnsedAfterCertainMonths;
    }

    public void setLapseUnsedAfterCertainMonths(LapseUnsedAfterCertainMonths lapseUnsedAfterCertainMonths) {
        this.lapseUnsedAfterCertainMonths = lapseUnsedAfterCertainMonths;
    }

    private LapseUnsedAfterCertainMonths lapseUnsedAfterCertainMonths=null;


    public Boolean getIndicator() {
        return indicator;
    }

    public void setIndicator(Boolean indicator) {
        this.indicator = indicator;
        /*if(this.indicator=true){
            this.month=true;
            this.beginOfMonth=true;
        }*/
    }

    public Boolean getMonth() {
        return month;
    }



    public Boolean getBeginOfMonth() {
        return beginOfMonth;
    }


    public Boolean getEndOfMonth() {
        return endOfMonth;
    }


    public Boolean getQuarter() {
        return quarter;
    }


    public Boolean getBeginOfQuarter() {
        return beginOfQuarter;
    }

       public Boolean getEndOfQuarter() {
        return endOfQuarter;
    }



    public Boolean getBiAnnual() {
        return biAnnual;
    }

    public void setBiAnnual(Boolean biAnnual) {
        this.biAnnual = biAnnual;
    }


    public void setMonthlyAccuralSetting(Boolean month,Boolean beginOfMonth,Boolean endOfMonth)
    {
        if(this.indicator==true) {
            this.month=month;
            this.beginOfMonth=beginOfMonth;
            this.endOfMonth=endOfMonth;
        }
        else
            throw new RuntimeException("Please set Indicator First"+getClass().getName());
    }

    public void setQuarterlyAccural(Boolean quarter, Boolean beginOfQuarter, Boolean endOfQuarter){
        if(this.indicator==true) {
            this.quarter=quarter;
            this.beginOfQuarter=beginOfQuarter;
            this.endOfQuarter=endOfQuarter;
        }
        else
            throw new RuntimeException("Please set Indicator First"+getClass().getName());
    }
}
