package com.darwinbox.recruitment.objects;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecruitmentSettings extends Services {

        private List<String> hiringLead;

        private boolean aadharCard;
        private boolean panCard;
        private boolean email;
        private boolean contactNumber;

        private String daysBeforeReapplication;
        private boolean allowdaysBeforeReapplicaton;
        private String daysBeforeReapplicationIJP;
        private boolean allowdaysBeforeReapplicatonIJP;

        private String websiteLink;
        private List<String> requisitionRole;

        private boolean showRecuitmentStartDate;
        private boolean showExperienceRange;
        private boolean showSalaryRange;
        private boolean showCommentsOrInstructions;
        private boolean showAssetRequirements;

        private boolean blockIJPtab;
        private boolean blockRefertab;

        public enum  startTAT{
               DUMMY,
               REQUISITIONCREATIONDATE,
               REQUISITIONAPPROVALDATE,
               HIRINGLEADASSIGNMENTDATE,
               CANDIDATEAPPLICATIONDATE;
       }
        public static startTAT startTAT;
        public RecruitmentSettings.startTAT getStartTAT() {
                return startTAT;
        }

        public void setStartTAT(RecruitmentSettings.startTAT startTAT) {
                this.startTAT = startTAT;
        }

        public enum endTAT{
                DUMMY,
                OFFERDATE,
                OFFERACCEPTANCEDATE,
                ADDEDASPENDINGEMPLOYEE,
                EMPLOYEEACTIVATION;
        }
        public static endTAT endTAT;
        public RecruitmentSettings.endTAT getEndTAT() {
                return endTAT;
        }

        public void setEndTAT(RecruitmentSettings.endTAT endTAT) {
                this.endTAT = endTAT;
        }

        public enum calenderService{
                GOOGLE("g"),
                OFFICE365("o");
                public final String s;
                calenderService(String s) {
                 this.s = s;
                }
        }
        public static calenderService calenderService;

        public RecruitmentSettings.calenderService getCalenderService() {
                return calenderService;
        }

        public void setCalenderService(RecruitmentSettings.calenderService calenderService) {
                this.calenderService = calenderService;
        }

        public List<String> getHiringLead() {
                return hiringLead;
        }

        public void setHiringLead(List<String> hiringLead) {
                this.hiringLead = hiringLead;
        }

        public boolean isAadharCard() {
                return aadharCard;
        }

        public void setAadharCard(boolean aadharCard) {
                this.aadharCard = aadharCard;
        }

        public boolean isPanCard() {
                return panCard;
        }

        public void setPanCard(boolean panCard) {
                this.panCard = panCard;
        }

        public boolean isEmail() {
                return email;
        }

        public void setEmail(boolean email) {
                this.email = email;
        }

        public boolean isContactNumber() {
                return contactNumber;
        }

        public void setContactNumber(boolean contactNumber) {
                this.contactNumber = contactNumber;
        }



        public boolean isAllowdaysBeforeReapplicaton() {
                return allowdaysBeforeReapplicaton;
        }

        public void setAllowdaysBeforeReapplicaton(boolean allowdaysBeforeReapplicaton) {
                this.allowdaysBeforeReapplicaton = allowdaysBeforeReapplicaton;
        }

        public String getDaysBeforeReapplication() {
                return daysBeforeReapplication;
        }

        public void setDaysBeforeReapplication(String daysBeforeReapplication) {
                this.daysBeforeReapplication = daysBeforeReapplication;
        }

        public String getDaysBeforeReapplicationIJP() {
                return daysBeforeReapplicationIJP;
        }

        public void setDaysBeforeReapplicationIJP(String daysBeforeReapplicationIJP) {
                this.daysBeforeReapplicationIJP = daysBeforeReapplicationIJP;
        }


        public boolean isAllowdaysBeforeReapplicatonIJP() {
                return allowdaysBeforeReapplicatonIJP;
        }

        public void setAllowdaysBeforeReapplicatonIJP(boolean allowdaysBeforeReapplicatonIJP) {
                this.allowdaysBeforeReapplicatonIJP = allowdaysBeforeReapplicatonIJP;
        }

        public String getWebsiteLink() {
                return websiteLink;
        }

        public void setWebsiteLink(String websiteLink) {
                this.websiteLink = websiteLink;
        }

        public List<String> getRequisitionRole() {
                return requisitionRole;
        }

        public void setRequisitionRole(List<String> requisitionRole) {
                this.requisitionRole = requisitionRole;
        }

        public boolean isShowRecuitmentStartDate() {
                return showRecuitmentStartDate;
        }

        public void setShowRecuitmentStartDate(boolean showRecuitmentStartDate) {
                this.showRecuitmentStartDate = showRecuitmentStartDate;
        }

        public boolean isShowExperienceRange() {
                return showExperienceRange;
        }

        public void setShowExperienceRange(boolean showExperienceRange) {
                this.showExperienceRange = showExperienceRange;
        }

        public boolean isShowSalaryRange() {
                return showSalaryRange;
        }

        public void setShowSalaryRange(boolean showSalaryRange) {
                this.showSalaryRange = showSalaryRange;
        }

        public boolean isShowCommentsOrInstructions() {
                return showCommentsOrInstructions;
        }

        public void setShowCommentsOrInstructions(boolean showCommentsOrInstructions) {
                this.showCommentsOrInstructions = showCommentsOrInstructions;
        }

        public boolean isShowAssetRequirements() {
                return showAssetRequirements;
        }

        public void setShowAssetRequirements(boolean showAssetRequirements) {
                this.showAssetRequirements = showAssetRequirements;
        }

        public boolean isBlockIJPtab() {
                return blockIJPtab;
        }

        public void setBlockIJPtab(boolean blockIJPtab) {
                this.blockIJPtab = blockIJPtab;
        }

        public boolean isBlockRefertab() {
                return blockRefertab;
        }

        public void setBlockRefertab(boolean blockRefertab) {
                this.blockRefertab = blockRefertab;
        }


        public List<NameValuePair> toMap() {

                Map<String, String> body = new HashMap<>();
                List<NameValuePair> list = new ArrayList<>();

                body.put("yt0","SAVE");

                for (String getLeads  : getHiringLead()) {
                        list.add(new BasicNameValuePair("recruitment_access_users",getLeads));
                }
                body.put("RecruitmentAddingUser[aadhar_check]", LeaveDeductionsBase.parseToPHP(isAadharCard()));
                body.put("RecruitmentAddingUser[pan_check]",LeaveDeductionsBase.parseToPHP(isPanCard()));
                body.put("RecruitmentAddingUser[email_check]",LeaveDeductionsBase.parseToPHP(isEmail()));
                body.put("RecruitmentAddingUser[phone_check]",LeaveDeductionsBase.parseToPHP(isContactNumber()));

                body.put("RecruitmentAddingUser[no_of_days]",getDaysBeforeReapplication());
                body.put("RecruitmentAddingUser[allow_diff_position]",LeaveDeductionsBase.parseToPHP(isAllowdaysBeforeReapplicaton()));
                body.put("RecruitmentAddingUser[ijp_no_of_days]",getDaysBeforeReapplicationIJP());
                body.put("RecruitmentAddingUser[allow_diff_position_ijp]",LeaveDeductionsBase.parseToPHP(isAllowdaysBeforeReapplicatonIJP()));

                body.put("RecruitmentAddingUser[back_to_website_link]",getWebsiteLink());
                for (String getRequisition  : getRequisitionRole()) {
                        list.add(new BasicNameValuePair("RecruitmentAddingUser[raise_requisition_access][]",getRequisition));
                }

                body.put("RecruitmentAddingUser[requisition_recruitment_start_date]",LeaveDeductionsBase.parseToPHP(isShowRecuitmentStartDate()));
                body.put("RecruitmentAddingUser[requisition_exp_range]",LeaveDeductionsBase.parseToPHP(isShowExperienceRange()));
                body.put("RecruitmentAddingUser[requisition_sal_range]",LeaveDeductionsBase.parseToPHP(isShowSalaryRange()));
                body.put("RecruitmentAddingUser[requisition_comments]",LeaveDeductionsBase.parseToPHP(isShowCommentsOrInstructions()));
                body.put("RecruitmentAddingUser[requisition_asset]",LeaveDeductionsBase.parseToPHP(isShowAssetRequirements()));

                body.put("RecruitmentAddingUser[block_ijp]",LeaveDeductionsBase.parseToPHP(isBlockIJPtab()));
                body.put("RecruitmentAddingUser[block_refer]",LeaveDeductionsBase.parseToPHP(isBlockRefertab()));

                body.put("RecruitmentAddingUser[recruitment_calendar_service]",""+getCalenderService().s);
                body.put("RecruitmentAddingUser[tat_start]",""+getStartTAT().ordinal());
                body.put("RecruitmentAddingUser[tat_end]",""+getEndTAT().ordinal());

                list.addAll(mapToFormData(body));
                return list;
        }

        public void toObject(Map<String,String> body){

                List<String> data = new ArrayList<>();
                data.add(body.get("HiringLeadID"));
                setHiringLead(data);

                setAadharCard(LeaveDeductionsBase.getFilter(body,"AadharCard","true"));
                setPanCard(LeaveDeductionsBase.getFilter(body,"PanCard","true"));
                setEmail(LeaveDeductionsBase.getFilter(body,"Email","true"));
                setContactNumber(LeaveDeductionsBase.getFilter(body,"ContactNumber","true"));

                setDaysBeforeReapplication(body.get("DaysBeforeReapplication"));
                setAllowdaysBeforeReapplicaton(LeaveDeductionsBase.getFilter(body,"IsDaysBeforeReapplicationAllowed","true"));
                setDaysBeforeReapplicationIJP(body.get("IJPDaysBeforeReapplication"));
                setAllowdaysBeforeReapplicatonIJP(LeaveDeductionsBase.getFilter(body,"IJPDaysBeforeReapplicationAllowed","true"));

                setWebsiteLink(body.get("websiteLink"));

                List<String> data1 = new ArrayList<>();
                data1.add(body.get("requisitionrole"));
               // data1.add("5b89185a19546");
                setRequisitionRole(data1);

                setShowRecuitmentStartDate(LeaveDeductionsBase.getFilter(body,"IsRecStartDate","true"));
                setShowExperienceRange(LeaveDeductionsBase.getFilter(body,"IsExperienceRange","true"));
                setShowSalaryRange(LeaveDeductionsBase.getFilter(body,"IsSalaryRange","true"));
                setShowCommentsOrInstructions(LeaveDeductionsBase.getFilter(body,"IsComments","true"));
                setShowAssetRequirements(LeaveDeductionsBase.getFilter(body,"IsAssetRequirements","true"));

                setBlockIJPtab(LeaveDeductionsBase.getFilter(body,"IsblockIJP","true"));
                setBlockRefertab(LeaveDeductionsBase.getFilter(body,"IsBlockRefererTab","true"));

                setCalenderService(calenderService.valueOf(body.get("CalenderSettings")));
                setStartTAT(RecruitmentSettings.startTAT.valueOf(body.get("startTAT")));
                setEndTAT(RecruitmentSettings.endTAT.valueOf(body.get("endTAT")));
        }

    public void toMapDefaultBody(Employee employee,Map<String,String> data) {

        String url = getData("@@url") + "/settings/recruitment/user";

        Map<String, String> body = new HashMap<>();

        body.put("yt0", "SAVE");

        body.put("recruitment_access_users",data.get("HiringLead"));

        // use getters and setters here
        body.put("RecruitmentAddingUser[aadhar_check]","0");
        body.put("RecruitmentAddingUser[pan_check]","0");
        body.put("RecruitmentAddingUser[email_check]","0");
        body.put("RecruitmentAddingUser[phone_check]","0");
        body.put("RecruitmentAddingUser[no_of_days]","0");
        body.put("RecruitmentAddingUser[allow_diff_position]","0");
        body.put("RecruitmentAddingUser[ijp_no_of_days]","");
        body.put("RecruitmentAddingUser[allow_diff_position_ijp]","0");
        body.put("RecruitmentAddingUser[back_to_website_link]","");
        body.put("RecruitmentAddingUser[raise_requisition_access][]",employee.getDesignationID()); //
        body.put("RecruitmentAddingUser[requisition_recruitment_start_date]","0");
        body.put("RecruitmentAddingUser[requisition_exp_range]","0");
        body.put("RecruitmentAddingUser[requisition_sal_range]","0");
        body.put("RecruitmentAddingUser[requisition_comments]","0");
        body.put("RecruitmentAddingUser[requisition_asset]","0");
        body.put("RecruitmentAddingUser[block_ijp]","0");
        body.put("RecruitmentAddingUser[block_refer]","0");
        body.put("RecruitmentAddingUser[recruitment_calendar_service]","g");
        body.put("RecruitmentAddingUser[recruitment_calendar_service]","g");
        body.put("RecruitmentAddingUser[tat_start]","1");
        body.put("RecruitmentAddingUser[tat_end]","1");

        doPost(url, null,mapToFormData(body));

    }

    }
