package com.darwinbox.customflows.objects.customflow;

import com.darwinbox.customflows.objects.forms.CFFormBody;
import com.darwinbox.customflows.services.CFApprovalFlowService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomFlowBody {

    enum TriggerType{
        DUMMY,
        MANUAL,
        AUTOMATIC
    }

    private String initiator;
    private String approvalFlow;
    private TriggerType triggerType;

    //for below fields we set default data as these are not
    // applicable for raise requistion
    private String applicationForm;
    private boolean Exception;
    private String action;
    CFFormBody.FieldType fieldType;
    private List<String> fieldValues = new ArrayList<>();


    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getApprovalFlow() {
        return approvalFlow;
    }

    public void setApprovalFlow(String approvalFlow) {
        this.approvalFlow = approvalFlow;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void toObject(Map<String, String> data) {

        String cfAFName = data.get("Approval Flow_Version");
        String cfAFID = "";
        if(cfAFName!="") {
            String version = cfAFName.split("#")[1];
            cfAFName = cfAFName.split("#")[0];
            CFApprovalFlowService cfAFsrv = new CFApprovalFlowService();
            cfAFID = cfAFsrv.getcfApprovalFlowByName(cfAFName, version);
        }
        setApprovalFlow(cfAFID);

        setInitiator(data.get("Initiator"));
        setTriggerType(TriggerType.valueOf(data.get("Trigger Type").replace(" ","_").toUpperCase()));


    }

    public List<NameValuePair> toMap(int stageOrder) {

        List<NameValuePair> formData = new ArrayList<>();

        formData.add(new BasicNameValuePair("CustomFlowset[trigger_type][]", triggerType.ordinal()+""));
        formData.add(new BasicNameValuePair("CustomFlowset[role][]",getInitiator()));
        formData.add(new BasicNameValuePair("CustomFlowset[approval_flow][]",getApprovalFlow()));

        formData.add(new BasicNameValuePair("CustomFlowset[title][]",""));
        formData.add(new BasicNameValuePair("CustomFlowset[action][]",""));
        formData.add(new BasicNameValuePair("CustomFlowset[options][]", ""));

        return formData;
    }

}
