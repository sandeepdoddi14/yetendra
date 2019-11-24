package com.darwinbox.core.company.objects;

import java.util.HashMap;

public class JobLevel {
    private String id;

    private String  jobLevelName;
    private String joibLevelCode;
    private String gradeID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobLevelName() {
        return jobLevelName;
    }

    public void setJobLevelName(String jobLevelName) {
        this.jobLevelName = jobLevelName;
    }

    public String getJoibLevelCode() {
        return joibLevelCode;
    }

    public void setJoibLevelCode(String joibLevelCode) {
        this.joibLevelCode = joibLevelCode;
    }

    public String getGradeID() {
        return gradeID;
    }

    public void setGradeID(String gradeID) {
        this.gradeID = gradeID;
    }



    public HashMap<String,String> toMap(){
        HashMap map= new HashMap();

        map.put("TenantJobLevel[job_level_name]",getJobLevelName());
        map.put("TenantJobLevel[job_level_code]",getJoibLevelCode());
        map.put("TenantJobLevel[grade]",getGradeID());

        return  map;

    }
}
