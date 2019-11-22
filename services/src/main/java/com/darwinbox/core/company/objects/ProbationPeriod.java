package com.darwinbox.core.company.objects;

import java.util.HashMap;

public class ProbationPeriod {
    private String id;
    private String probationName;
    private String noOfDaysToCompleteProabtion;
    private Boolean showInProbationExtension;
    private Boolean extendConfirmation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProbationName() {
        return probationName;
    }

    public void setProbationName(String probationName) {
        this.probationName = probationName;
    }

    public String getNoOfDaysToCompleteProabtion() {
        return noOfDaysToCompleteProabtion;
    }

    public void setNoOfDaysToCompleteProabtion(String noOfDaysToCompleteProabtion) {
        this.noOfDaysToCompleteProabtion = noOfDaysToCompleteProabtion;
    }

    public Boolean getShowInProbationExtension() {
        return showInProbationExtension;
    }

    public void setShowInProbationExtension(Boolean showInProbationExtension) {
        this.showInProbationExtension = showInProbationExtension;
    }

    public Boolean getExtendConfirmation() {
        return extendConfirmation;
    }

    public void setExtendConfirmation(Boolean extendConfirmation) {
        this.extendConfirmation = extendConfirmation;
    }


    public HashMap<String,String> toMap(){
        HashMap<String,String> map= new HashMap<>();

        map.put("ProbationPeriod[probation_name]",getProbationName());
        map.put("ProbationPeriod[number_of_days]",getNoOfDaysToCompleteProabtion());
        map.put("ProbationPeriod[is_available_extension]",getShowInProbationExtension()?"1":"0");
        map.put("ProbationPeriod[autoconfirmation_extend]",getExtendConfirmation()?"1":"0");


        return map;
    }

}
