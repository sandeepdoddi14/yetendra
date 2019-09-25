package com.darwinbox.customflows.objects.workflows;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlowBody;
import com.darwinbox.customflows.objects.forms.CFFormBody;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFWorkFLow extends Services {
  private String name;
  private String description;


  private boolean isSeriesSet;
  private String id = "";

  private List<CFWorkflowBody> cfWFBodyList = new ArrayList<>();


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



  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isSeriesSet() {
    return isSeriesSet;
  }

  public void setSeriesSet(boolean seriesSet) {
    isSeriesSet = seriesSet;
  }

  public void add(CFWorkflowBody cfWFBody){
    cfWFBodyList.add(cfWFBody);
  }
  public void toObject(Map<String, String> data) {

    setName(data.get("Name"));
    boolean series = (data.getOrDefault("Is Series","yes").equalsIgnoreCase("yes"));
    setSeriesSet(series);
    setDescription(data.get("Description").toString());

  }

  public List<NameValuePair> toMap(){

    List<NameValuePair> formData = new ArrayList<>();

    formData.add(new BasicNameValuePair("id", getId()));
    formData.add(new BasicNameValuePair("CustomWorkFlowForm[name]", getName()));
    formData.add(new BasicNameValuePair("CustomWorkFlowForm[description]", getDescription()));
    if(isSeriesSet)formData.add(new BasicNameValuePair("CustomWorkFlowForm[is_series]", (isSeriesSet() ? "1" : "0")));


    int count = 0;
    for (CFWorkflowBody formBody : cfWFBodyList) {
       formData.addAll(formData.size(),(formBody.toMap(count)));
       count ++;
    }

    return formData;
  }




}


