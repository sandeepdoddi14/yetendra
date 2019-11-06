package com.darwinbox.core.company.objects;

import java.util.HashMap;

public class JobDescription {
    private String id;

    private String jobDescriptionTitle;
    private String groupCompany;
    private String designation;
    private String officeLocation;
    private String positionDescription="Description";
    private String experienceFrom;
    private String experienceTo;
    private String salaryCurrency;
    private String salaryMin;
    private String salaryMax;
    private String primaryResponsibility;
    private String additionalResponsibility;

    private String reportingDesignation;

    private String educationalQualificationCategory;
    private String educationalQualificationSpecilization;
    private String educationalQualificationDegree;
    private String educationalQualificationAcademicScore;
    private String educationalQualificationInstitutionTier;


    private String requiredCertification;
    private String requiredTraining;

    private String workExpIndustry;
    private String workExpRole;
    private String workExpFrom;
    private String workExpTo;


    private String keyPerformanceIndicators;
    private String requiredCompetencies;
    private String requiredKnowledge;
    private String requiredSkills;
    private String requiredAbilitiesPhysical;
    private String requiredAbilitiesOthers;

    private String workEnvironmentDetails;
    private String travel;
    private String vehicle;
    private String workPermit;

    private String payRate;
    private String contractTypes;
    private String timeConstraits;
    private String complianceRelated;
    private String unionAffiliation;

    private String template="<p data-attr=\"modal\"><b>Group Company:</b> #*Group Company*#</p>\n" +
            "                    <p class=\"Designation\" data-attr=\"modal\"><b>Designation:</b> #*Designation*#</p>\n" +
            "                    <p class=\"Office Location\" data-attr=\"modal\"><b>Office Location:</b> #*Office Location*#</p>\n" +
            "                    <p class=\"YOE\" data-attr=\"modal\"><b>Years of experience:</b> #*YOE*#</p>\n" +
            "                    <p class=\"Salary Range\" data-attr=\"modal\"><b>Salary Range:</b> #*Salary Range*#</p>\n" +
            "                    <p class=\"Position description\"><b>Position description:</b> #*Position description*#</p>\n" +
            "                    <p class=\"Primary Responsibilities\"><b>Primary Responsibilities:</b> #*Primary Responsibilities*#</p>\n" +
            "                    <p class=\"Additional Responsibilities\"><b>Additional Responsibilities:</b> #*Additional Responsibilities*#</p>\n" +
            "                    <p class=\"Reporting Designation Group\"><b>Reporting Team</b></p>\n" +
            "                        <ul class=\"Reporting Designation Group\">\n" +
            "                            <li class=\"Reporting Designation\"><b>Reporting Designation:</b> #*Reporting Designation*#</li>\n" +
            "                            <li class=\"Reporting Department\"><b>Reporting Department:</b> #*Reporting Department*#</li>\n" +
            "                        </ul>\n" +
            "                    <p class=\"Category Group\"><b>Educational qualifications preferred</b></p>\n" +
            "                        <ul class=\"Category Group\">\n" +
            "                            <li class=\"Category\"><b>Category:</b> #*Category*#</li>\n" +
            "                            <li class=\"Field specialization\"><b>Field specialization:</b> #*Field specialization*#</li>\n" +
            "                            <li class=\"Degree\"><b>Degree:</b> #*Degree*#</li>\n" +
            "                            <li class=\"Academic score\"><b>Academic score:</b> #*Academic score*#</li>\n" +
            "                            <li class=\"Institution tier\"><b>Institution tier:</b> #*Institution tier*#</li>\n" +
            "                        </ul>\n" +
            "                    <p class=\"Required Certification/s\"><b>Required Certification/s:</b> #*Required Certification/s*#</p>\n" +
            "                    <p class=\"Required Training/s\"><b>Required Training/s:</b> #*Required Training/s*#</p>\n" +
            "                    <p class=\"Industry Group\"><b>Required work experience</b></p>\n" +
            "                        <ul class=\"Industry Group\">\n" +
            "                            <li class=\"Industry\"><b>Industry:</b> #*Industry*#</li>\n" +
            "                            <li class=\"Role\"><b>Role:</b> #*Role*#</li>\n" +
            "                            <li class=\"Years of experience\"><b>Years of experience:</b> #*Years of experience*#</li>\n" +
            "                        </ul>\n" +
            "                    <p class=\"Key Performance Indicators\"><b>Key Performance Indicators:</b> #*Key Performance Indicators*#</p>\n" +
            "                    <p class=\"Required Competencies\"><b>Required Competencies:</b> #*Required Competencies*#</p>\n" +
            "                    <p class=\"Required Knowledge\"><b>Required Knowledge:</b> #*Required Knowledge*#</p>\n" +
            "                    <p class=\"Required Skills\"><b>Required Skills:</b> #*Required Skills*#</p>\n" +
            "                    <p class=\"Physical Group\"><b>Required abilities</b></p>\n" +
            "                        <ul class=\"Physical Group\">\n" +
            "                            <li class=\"Physical\"><b>Physical:</b> #*Physical*#</li>\n" +
            "                            <li class=\"Other\"><b>Other:</b> #*Other*#</li>\n" +
            "                        </ul>\n" +
            "                    <p class=\"Work Environment Details\"><b>Work Environment Details:</b> #*Work Environment Details*#</p>\n" +
            "                    <p class=\"Travel Group\"><b>Specific requirements</b></p>\n" +
            "                        <ul class=\"Travel Group\">\n" +
            "                            <li class=\"Travel\"><b>Travel:</b> #*Travel*#</li>\n" +
            "                            <li class=\"Vehicle\"><b>Vehicle:</b> #*Vehicle*#</li>\n" +
            "                            <li class=\"Work Permit\"><b>Work Permit:</b> #*Work Permit*#</li>\n" +
            "                        </ul>\n" +
            "                    <p class=\"Pay Rate Group\"><b>Other details</b></p>\n" +
            "                        <ul class=\"Pay Rate Group\">\n" +
            "                            <li class=\"Pay Rate\"><b>Pay Rate:</b> #*Pay Rate*#</li>\n" +
            "                            <li class=\"Contract Types\"><b>Contract Types:</b> #*Contract Types*#</li>\n" +
            "                            <li class=\"Time Constraints\"><b>Time Constraints:</b> #*Time Constraints*#</li>\n" +
            "                            <li class=\"Compliance Related\"><b>Compliance Related:</b> #*Compliance Related*#</li>\n" +
            "                            <li class=\"Union Affiliation\"><b>Union Affiliation:</b> #*Union Affiliation*#</li>\n" +
            "                        </ul>";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobDescriptionTitle() {
        return jobDescriptionTitle;
    }

    public void setJobDescriptionTitle(String jobDescriptionTitle) {
        this.jobDescriptionTitle = jobDescriptionTitle;
    }

    public String getExperienceTo() {
        return experienceTo;
    }

    public void setExperienceTo(String experienceTo) {
        this.experienceTo = experienceTo;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public String getExperienceFrom() {
        return experienceFrom;
    }

    public void setExperienceFrom(String experienceFrom) {
        this.experienceFrom = experienceFrom;
    }



    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public String getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(String salaryMin) {
        this.salaryMin = salaryMin;
    }

    public String getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(String salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getPrimaryResponsibility() {
        return primaryResponsibility;
    }

    public void setPrimaryResponsibility(String primaryResponsibility) {
        this.primaryResponsibility = primaryResponsibility;
    }

    public String getAdditionalResponsibility() {
        return additionalResponsibility;
    }

    public void setAdditionalResponsibility(String additionalResponsibility) {
        this.additionalResponsibility = additionalResponsibility;
    }

    public String getReportingDesignation() {
        return reportingDesignation;
    }

    public void setReportingDesignation(String reportingDesignation) {
        this.reportingDesignation = reportingDesignation;
    }

    public String getEducationalQualificationCategory() {
        return educationalQualificationCategory;
    }

    public void setEducationalQualificationCategory(String educationalQualificationCategory) {
        this.educationalQualificationCategory = educationalQualificationCategory;
    }

    public String getEducationalQualificationSpecilization() {
        return educationalQualificationSpecilization;
    }

    public void setEducationalQualificationSpecilization(String educationalQualificationSpecilization) {
        this.educationalQualificationSpecilization = educationalQualificationSpecilization;
    }

    public String getEducationalQualificationDegree() {
        return educationalQualificationDegree;
    }

    public void setEducationalQualificationDegree(String educationalQualificationDegree) {
        this.educationalQualificationDegree = educationalQualificationDegree;
    }

    public String getEducationalQualificationAcademicScore() {
        return educationalQualificationAcademicScore;
    }

    public void setEducationalQualificationAcademicScore(String educationalQualificationAcademicScore) {
        this.educationalQualificationAcademicScore = educationalQualificationAcademicScore;
    }

    public String getEducationalQualificationInstitutionTier() {
        return educationalQualificationInstitutionTier;
    }

    public void setEducationalQualificationInstitutionTier(String educationalQualificationInstitutionTier) {
        this.educationalQualificationInstitutionTier = educationalQualificationInstitutionTier;
    }

    public String getRequiredCertification() {
        return requiredCertification;
    }

    public void setRequiredCertification(String requiredCertification) {
        this.requiredCertification = requiredCertification;
    }

    public String getRequiredTraining() {
        return requiredTraining;
    }

    public void setRequiredTraining(String requiredTraining) {
        this.requiredTraining = requiredTraining;
    }

    public String getWorkExpIndustry() {
        return workExpIndustry;
    }

    public void setWorkExpIndustry(String workExpIndustry) {
        this.workExpIndustry = workExpIndustry;
    }

    public String getWorkExpRole() {
        return workExpRole;
    }

    public void setWorkExpRole(String workExpRole) {
        this.workExpRole = workExpRole;
    }

    public String getWorkExpFrom() {
        return workExpFrom;
    }

    public void setWorkExpFrom(String workExpFrom) {
        this.workExpFrom = workExpFrom;
    }

    public String getWorkExpTo() {
        return workExpTo;
    }

    public void setWorkExpTo(String workExpTo) {
        this.workExpTo = workExpTo;
    }

    public String getKeyPerformanceIndicators() {
        return keyPerformanceIndicators;
    }

    public void setKeyPerformanceIndicators(String keyPerformanceIndicators) {
        this.keyPerformanceIndicators = keyPerformanceIndicators;
    }

    public String getRequiredCompetencies() {
        return requiredCompetencies;
    }

    public void setRequiredCompetencies(String requiredCompetencies) {
        this.requiredCompetencies = requiredCompetencies;
    }

    public String getRequiredKnowledge() {
        return requiredKnowledge;
    }

    public void setRequiredKnowledge(String requiredKnowledge) {
        this.requiredKnowledge = requiredKnowledge;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getRequiredAbilitiesPhysical() {
        return requiredAbilitiesPhysical;
    }

    public void setRequiredAbilitiesPhysical(String requiredAbilitiesPhysical) {
        this.requiredAbilitiesPhysical = requiredAbilitiesPhysical;
    }

    public String getRequiredAbilitiesOthers() {
        return requiredAbilitiesOthers;
    }

    public void setRequiredAbilitiesOthers(String requiredAbilitiesOthers) {
        this.requiredAbilitiesOthers = requiredAbilitiesOthers;
    }

    public String getWorkEnvironmentDetails() {
        return workEnvironmentDetails;
    }

    public void setWorkEnvironmentDetails(String workEnvironmentDetails) {
        this.workEnvironmentDetails = workEnvironmentDetails;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(String workPermit) {
        this.workPermit = workPermit;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public String getContractTypes() {
        return contractTypes;
    }

    public void setContractTypes(String contractTypes) {
        this.contractTypes = contractTypes;
    }

    public String getTimeConstraits() {
        return timeConstraits;
    }

    public void setTimeConstraits(String timeConstraits) {
        this.timeConstraits = timeConstraits;
    }

    public String getComplianceRelated() {
        return complianceRelated;
    }

    public void setComplianceRelated(String complianceRelated) {
        this.complianceRelated = complianceRelated;
    }

    public String getUnionAffiliation() {
        return unionAffiliation;
    }

    public void setUnionAffiliation(String unionAffiliation) {
        this.unionAffiliation = unionAffiliation;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public HashMap<String,String> toMap(){
        HashMap<String,String> map= new HashMap<>();

        map.put("JobDescription[id]",getId());
        map.put("JobDescription[jd_title]",getJobDescriptionTitle());
        map.put("JobDescription[parent_company_id]",getGroupCompany());
        map.put("JobDescription[pos_description]",getPositionDescription());
        map.put("JobDescription[experience_from]",getExperienceFrom());
        map.put("JobDescription[experience_to]",getExperienceTo());
        map.put("JobDescription[salary_currency]",getSalaryCurrency());
        map.put("JobDescription[salary_min]",getSalaryMin());
        map.put("JobDescription[salary_max] ",getSalaryMax());
        map.put("JobDescription[primary_responsibility][]",getPrimaryResponsibility());
        map.put("JobDescription[additional_responsibility][]",getAdditionalResponsibility());
        map.put("JobDescription[reporting_designation]",getReportingDesignation());
        map.put("JobDescription[edu_category_other]",getEducationalQualificationCategory());
        map.put("JobDescription[edu_field_specialisation_other]",getEducationalQualificationSpecilization());
        map.put("JobDescription[edu_degree_other]",getEducationalQualificationDegree());
        map.put("JobDescription[edu_academic_score]",getEducationalQualificationAcademicScore());
        map.put("JobDescription[edu_institution_tier]",getEducationalQualificationInstitutionTier());
        map.put("JobDescription[required_certification]",getRequiredCertification());
        map.put("JobDescription[required_training]",getRequiredTraining());
        map.put("JobDescription[work_industry]",getWorkExpIndustry());
        map.put("JobDescription[work_role]",getWorkExpRole());
        map.put("JobDescription[work_exp_from]",getWorkExpFrom());
        map.put("JobDescription[work_exp_to]",getWorkExpTo());
        map.put("JobDescription[key_performance_indicators]",getKeyPerformanceIndicators());
        map.put("JobDescription[required_competencies]",getRequiredCompetencies());
        map.put("JobDescription[required_knowledge]",getRequiredKnowledge());
        map.put("JobDescription[required_skills]",getRequiredSkills());
        map.put("JobDescription[required_abilities_physical]",getRequiredAbilitiesPhysical());
        map.put("JobDescription[required_abilities_other]",getRequiredAbilitiesOthers());
        map.put("JobDescription[work_environment_details]",getWorkEnvironmentDetails());
        map.put("JobDescription[travel]",getTravel());
        map.put("JobDescription[work_permit]",getWorkPermit());
        map.put("JobDescription[pay_rate]",getPayRate());
        map.put("JobDescription[contract_types]",getContractTypes());
        map.put("JobDescription[time_constraints]",getTimeConstraits());
        map.put("JobDescription[compliance_related]",getComplianceRelated());
        map.put("JobDescription[union_affiliation]",getUnionAffiliation());
        map.put("JobDescription[jd_template]",getTemplate());


        return map;



    }
}
