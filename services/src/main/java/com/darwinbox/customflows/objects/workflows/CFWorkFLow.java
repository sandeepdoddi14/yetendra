package com.darwinbox.customflows.objects.workflows;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.forms.CFFormBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFWorkFLow {
  private String name;
  private String description;
  private boolean isSeries;
  private String id = "";

  private List<CFWorkflowBody> wfFormBody = new ArrayList<>();

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

  public boolean isSeries() {
    return isSeries;
  }

  public void setSeries(boolean series) {
    isSeries = series;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public List<NameValuePair> toMapObject(){

    List<NameValuePair> formData = new ArrayList<>();

    formData.add(new BasicNameValuePair("id", getId()));
    formData.add(new BasicNameValuePair("CustomWorkFlowForm[name]", getName()));
    formData.add(new BasicNameValuePair("CustomWorkFlowForm[description]", getDescription()));
    //Need to add 'Is Series' checkbox value
    //formData.add(new BasicNameValuePair("CustomWorkFlowForm[is_series]", isSeries()));
    int count = 0;
    for (CFWorkflowBody formBody : wfFormBody) {
      count ++;
      //formData.addAll(formData.size(), Services.mapToFormData(formBody.toMap(count)));
    }

    return formData;
  }




}


