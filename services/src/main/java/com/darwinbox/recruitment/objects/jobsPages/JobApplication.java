package com.darwinbox.recruitment.objects.jobsPages;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class JobApplication extends Services {

//enum for pan_card and aadar_card and pass string in visibility and edibility
//getid() put common?

    private String fieldType;
    private String mandatory;

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    private List<String> visibilityRoles;
    public List<String> getVisibilityRoles() {
        return visibilityRoles;
    }
    public void setVisibilityRoles(List<String> visibilityRoles) {
        this.visibilityRoles = visibilityRoles;
    }
    private List<String> edibilityRoles;
    public List<String> getEdibilityRoles() {
        return edibilityRoles;
    }

    public void setEdibilityRoles(List<String> edibilityRoles) {
        this.edibilityRoles = edibilityRoles;
    }


    public enum field{
        PANCARD("pan_card"),
        COVERLETTER("cover"),
        RESUME("resume"),
        ADDRESS("address"),
        REFERENCENAME("reference"),
        RECRUITERNAME("recruiter_name"),
        RECRUTEREMAILID("recruiter_email"),
        LANGUAGES("languages_spoke"),
        HIGHESTEDUCATION("high_edu"),
        COLLEGEORUNIVERSITY("college_university"),
        UNIQUENESS("unique"),
        DESIREDSALARY("desired_salary"),
        DATEAVAILABLE("data_avail");
        //work_for_company
        //perticular_profile
        public final String s;
        field(String s) {
            this.s = s;
        }
    }
    public static field field;
    public static JobApplication.field getField() {
        return field;
    }

    public static void setField(JobApplication.field field) {
        JobApplication.field = field;
    }

    /*public enum visibility{
        ALL,
        INTERVIEWERS,
        HIRINGGROUP,
        SCHEDULINGTEAM,
        SCREENINGTEAM,
        SHORTLISTINGTEAM,
        RECRUITER;
    }
    public  static visibility visibility;
    public static JobApplication.visibility getVisibility() {
        return visibility;
    }

    public static void setVisibility(JobApplication.visibility visibility) {
        JobApplication.visibility = visibility;
    }
*/
    public List<NameValuePair> toMapSecondPage() {

        Map<String, String> body = new HashMap<>();
        List<NameValuePair> formData = new ArrayList<>();

        //pan_card value in s variable
        body.put("input["+getField().s+"]",getFieldType());
        body.put("validate["+getField().s+"]",getMandatory());

        if (getVisibilityRoles().size() >= 1) {
            for (String visiblityUser : visibilityRoles)
                formData.add(new BasicNameValuePair("visibility["+getField().s+"][]", visiblityUser));
        }
        if(getEdibilityRoles().size() >=1){
            for(String edibilityUser : edibilityRoles){
                formData.add(new BasicNameValuePair("editiblity["+getField().s+"][]",edibilityUser));
            }
        }

        body.putAll(defaultForSecondPage());
        formData.addAll(mapToFormData(body));
        return formData;
    }

    public void toObjectSecondPage(Map<String, String> body){

        setField(JobApplication.field.valueOf(body.get("fields")));
        setFieldType(body.get("fieldType"));
        setMandatory(body.get("mandatory"));

        String temp = body.get("VisibilityRoles");
        if (temp.length() != 0) {

            String VisibilityUsers[] = temp.split(",");

            visibilityRoles = new ArrayList<>();
            for (String value : VisibilityUsers) {
                visibilityRoles.add(value);
            }
            setVisibilityRoles(visibilityRoles);
        }

        String role = body.get("EdibilityRoles");
        if (role.length() != 0) {

            String EdibilityUsers[] = role.split(",");

            edibilityRoles = new ArrayList<>();
            for (String value : EdibilityUsers) {
                edibilityRoles.add(value);
            }
            setEdibilityRoles(edibilityRoles);
        }
    }

    public Map<String, String> defaultForSecondPage(){

        Map<String, String> body = new HashMap<>();

        body.put("edit","1");
        body.put("yt1","SAVE & CONTINUE");

        body.put("input[firstname]","textfield");
        body.put("validate[firstname]","on");
        body.put("visibility[firstname][]","1");
        body.put("input[lastname]","textfield");
        body.put("validate[lastname]","on");
        body.put("visibility[lastname][]","1");
        body.put("input[email]","textfield");
        body.put("validate[email]","on");
        body.put("visibility[email][]","1");
        body.put("input[phone]","textfield");
        body.put("validate[phone]","on");
        body.put("visibility[phone][]","1");
        body.put("input[aadhar_card]","textfield");
        body.put("validate[aadhar_card]","on");
        body.put("visibility[aadhar_card][]","1");

        return body;
    }

}
