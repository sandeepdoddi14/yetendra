package com.darwinbox.customflows.objects.forms;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class CFFormBody {

    private String fieldName;
    private boolean isMandatory;
    private boolean isLineBreak;
    private FieldType fieldType;
    private List<String> fieldValues = new ArrayList<>();

    enum FieldType {
        TEXT_FIELD("textfield"),
        TEXT_AREA("textarea"),
        DATE("date"),
        CHECKBOX("checkbox"),
        SECTION_HEADING("section_heading"),
        DROPDOWN("select"),
        MULTI_SELECT_DROPDOWN("multiselect"),
        RADIO_BUTTONS("radio");

        private String type;

        public String getType() {
            return type;
        }

        private FieldType(String type) {
            this.type = type;
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public boolean isLineBreak() {
        return isLineBreak;
    }

    public void setLineBreak(boolean lineBreak) {
        isLineBreak = lineBreak;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }


    // from Excel to Java Object
    public void toObject(Map<String, String> data) {
        //

        setFieldName(data.get("FieldName"));
        setFieldType(FieldType.valueOf(data.get("FieldType").toUpperCase().replace(" ", "_")));

        String values[] = data.getOrDefault("Values","").split(",");
        for ( String value : values)
        fieldValues.add(value);

        setMandatory(data.getOrDefault("Mandatory","no").equalsIgnoreCase("no"));
        setLineBreak(data.getOrDefault("LineBreak","no").equalsIgnoreCase("no"));

    }




    public Map<String, String> toMap(int order) {

        Map<String, String> body = new HashMap<>();

        body.put("CustomForm_set[linebreak][]", (isLineBreak() ? order+"" : ""));
        body.put("CustomForm_set[required][]", (isMandatory() ? order+"" : ""));
        body.put("CustomForm_set[assessment][]", getFieldName());
        body.put("CustomForm_set[measure][]", getFieldType().getType());

        List<String> values = getFieldValues();
        String valueBody = "";

        for (String value : values ) {
            valueBody =  ","   + value + valueBody ;
        }

        if (valueBody.length() != 1)
            valueBody = valueBody.substring(1);

        body.put("CustomForm_set[options][]", valueBody);

        return body;
    }

    public static void main(String[] args) {

//        CFFormBody cfFormBody = new CFFormBody();
//
//        cfFormBody.setFieldValues(new ArrayList<>());
//
//        cfFormBody.getFieldValues();


    }
}
