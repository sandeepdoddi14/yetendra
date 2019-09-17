package com.darwinbox.customflows.objects.approvalflows;

import com.darwinbox.customflows.objects.workflows.CFWorkflowBody;

import java.util.ArrayList;
import java.util.List;

public class CFApprovalFlowBody {

    List<String>  approverRoles;
    String action;
    String approvalContext;
    List<String>  visibilityRoles;

    String skipSettingName;
    String slaSettignName;

    private List<CFWorkflowBody> wfFormBody = new ArrayList<>();




}
