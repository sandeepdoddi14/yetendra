package com.darwinbox.customflows.objects.customflow;

import com.darwinbox.attendance.services.Services;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class CustomFlow extends Services {

    enum CustomFlowFor {
        DUMMY,
        EMPLOYEE,
        OFFER_LETTER,
        RAISE_REQUISITION,
        RECOGNITION
    }

    enum TriggerEvent {
        DUMMY,
        BUSINESS_FLOW,
        LOCATION_CHANGE,
        MANAGER_CHANGE,
        DESIGNATION_CHANGE,
        EMPLOYEE_MOVEMENT,
        EMPLOYEE_TRANSFER
    }


    enum EventType {
        DUMMY,
        NO_EVENT,
        DATE_OF_JOINING,
        DATE_OF_CONFIRMATION,
        WORK_ANNIVERSARY,
        ABSCONDING_TRIGGER
    }

    private String groupCompanyMongoId ="";
    private List<CustomFlowBody> customFlowBodyList = new ArrayList<>();

    private String companyName;
    private String name;
    private String description;
    private String isParent;
    private String id;
    private String workflowID;
    //can we make it boolean
    private String RestricCondition;

    private List<String> applicableToList;
    private List<String> emailToList;

    private CustomFlowFor cfFor;
    private TriggerEvent triggerEvent;
    private EventType eventType;

    public String getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(String workflowID) {
        this.workflowID = workflowID;
    }

    public String getRestricCondition() {
        return RestricCondition;
    }

    public void setRestricCondition(String restricCondition) {
        RestricCondition = restricCondition;
    }

    public List<String> getEmailToList() {
        return emailToList;
    }

    public void setEmailToList(List<String> emailToList) {
        this.emailToList = emailToList;
    }

    public CustomFlowFor getCfFor() {
        return cfFor;
    }

    public void setCfFor(CustomFlowFor cfFor) {
        this.cfFor = cfFor;
    }

    public TriggerEvent getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(TriggerEvent triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<String> getApplicableToList() {
        return applicableToList;
    }

    public void setApplicableToList(List<String> applicableToList) {
        this.applicableToList = applicableToList;
    }

    public void add(CustomFlowBody customFlowBody){
        customFlowBodyList.add(customFlowBody);
    }


    public void toObject(Map<String, String> data) {

        setName(data.get("Name"));
        setDescription(data.getOrDefault("Description", ""));
        setCfFor(CustomFlowFor.valueOf(data.get("For").toUpperCase().replace(" ", "_")));
        setWorkflowID(data.getOrDefault("WorkFlow", " "));
        setTriggerEvent(TriggerEvent.valueOf(data.get("Trigger Event").toUpperCase().replace(" ", "_")));
        setEventType(EventType.valueOf(data.getOrDefault("Event Type", "0").toUpperCase().replace(" ", "_")));
        setRestricCondition(data.get("Restrict Condition"));

        String comp  = data.getOrDefault("IsParent","all");

        if ( comp.equalsIgnoreCase("no")) {

            // TODO : how to get mangoid for that groupcompany read from inifile
            groupCompanyMongoId = getGroupCompanyIds().get(getIsParent().trim());
            setCompanyName("no");

        } if ( comp.equalsIgnoreCase("all")) {
            setCompanyName("all");
        } if ( comp.equalsIgnoreCase("yes")) {
            setCompanyName("");
        }

        String temp = data.get("Email To");
        if (temp.length() != 0) {

            String emailTo[] = temp.split(",");

            emailToList = new ArrayList<>();
            for (String value : emailTo) {
                emailToList.add(value);
            }

            setEmailToList(emailToList);
        }

        temp = data.get("Applicable To");
        if (temp.length() != 0) {

            String assignTo[] = temp.split(",");

            applicableToList = new ArrayList<>();
            for (String value : assignTo) {
                applicableToList.add(value);
            }

            setApplicableToList(applicableToList);
        }


    }

    public List<NameValuePair> toMap() {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("id", getId()));
        formData.add(new BasicNameValuePair("CustomFlow[for]", cfFor.ordinal()+""));
        formData.add(new BasicNameValuePair("CustomFlow[name]", getName()));
        formData.add(new BasicNameValuePair("CustomFlow[description]", getDescription()));

        if(getCompanyName().equalsIgnoreCase("ALL")){
            formData.add(new BasicNameValuePair("CustomFlow[parent_company_id]", "all"));
        }else if(getCompanyName().equalsIgnoreCase("")){
            formData.add(new BasicNameValuePair("CustomFlow[parent_company_id]", ""));
        }
        else{
            formData.add(new BasicNameValuePair("CustomFlow[parent_company_id]", groupCompanyMongoId));
        }

        formData.add(new BasicNameValuePair("CustomFlow[triggering_event]", triggerEvent.ordinal()+""));
        formData.add(new BasicNameValuePair("CustomFlow[event_type]", eventType.ordinal()+""));
        formData.add(new BasicNameValuePair("CustomFlow[intra_inter_company]", "0"));
        formData.add(new BasicNameValuePair("CustomFlow[or_and]", getRestricCondition().equalsIgnoreCase("OR") ? "0": "1"));
        formData.add(new BasicNameValuePair("CustomFlow[workflow_form]",""));
        formData.add(new BasicNameValuePair("CustomFlow[retirement_before_days]", ""));

        // get applicable category short names from Excel sheet
        List<String> applicabilityList = getApplicableToList();
        String[] ApplicableCategories = new String[applicabilityList.size()];
        if (applicabilityList.size() >= 1) {
            String applicabilityListBody = "";

            for (String value : applicabilityList) {
                applicabilityListBody = "," + value + applicabilityListBody;
            }

            if (applicabilityListBody.length() != 1) {
                ApplicableCategories = applicabilityListBody.split(",");
            }

        }

        String id = "";
        List<String> valuesList ;
        int randomIndex;

//        HashMap departments = getDepartments(groupCompanyMongoId);
//        HashMap locations = getOfficeLocations(groupCompanyMongoId);
//        JSONObject designations = getDesignations(groupCompanyMongoId);
        HashMap empTypes = getEmployeeTypes();
        HashMap grades = getGrades();
        HashMap bands = getBands();
        HashMap assignments = getUserAssignments();


        for (String applicable : ApplicableCategories) {
            switch (applicable) {
                case "ALL":

                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "ALL_0"));
                    break;

                case "TYP":
                    valuesList = new ArrayList<String>(empTypes.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "TYP_" + id));
                    break;
                case "BAND":
                    valuesList = new ArrayList<String>(bands.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "BAND_" + id));
                    break;
                case "GRAD":
                    valuesList = new ArrayList<String>(grades.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "GRADE_" + id));
                    break;
                /*case "DEPT":
                    valuesList = new ArrayList<String>(departments.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "DEPT_" + id));
                    break;
                case "LOC":
                    valuesList = new ArrayList<String>(locations.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "LOC_" + id));
                    break;
                case "DESG":
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "DESG_" + id));
                    break;*/
                case "assignment":
                    valuesList = new ArrayList<String>(assignments.values());
                    randomIndex = new Random().nextInt(valuesList.size());
                    id = valuesList.get(randomIndex);
                    formData.add(new BasicNameValuePair("CustomFlow[assign_to][]", "assignment_" + id));
                    break;
            }
        }


        if (getEmailToList().size() >= 1) {
            for (String emailUser : emailToList)
                formData.add(new BasicNameValuePair("CustomFlow[email_to][]", emailUser));
        }

        int count = 0;
        for (CustomFlowBody formBody : customFlowBodyList) {
            formData.addAll(formData.size(), (formBody.toMap(count)));
            count++;
        }

        return formData;
    }







}
