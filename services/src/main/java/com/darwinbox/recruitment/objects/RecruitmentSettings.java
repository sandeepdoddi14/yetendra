package com.darwinbox.recruitment.objects;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.services.Services;
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

        private String calenderService; //enum
        private String startTAT; //enum
        private String endTAT;   //enum

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

        public String getCalenderService() {
                return calenderService;
        }

        public void setCalenderService(String calenderService) {
                this.calenderService = calenderService;
        }

        public String getStartTAT() {
                return startTAT;
        }

        public void setStartTAT(String startTAT) {
                this.startTAT = startTAT;
        }

        public String getEndTAT() {
                return endTAT;
        }

        public void setEndTAT(String endTAT) {
                this.endTAT = endTAT;
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

                body.put("RecruitmentAddingUser[recruitment_calendar_service]",""); //enum?
                body.put("RecruitmentAddingUser[tat_start]","");
                body.put("RecruitmentAddingUser[tat_end]","");

                list.addAll(mapToFormData(body));
                return list;
        }


        }
