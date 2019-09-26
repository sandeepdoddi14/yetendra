package com;

import com.darwinbox.customflows.objects.CFSLASettings;
import com.darwinbox.customflows.objects.CFSkipSettings;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlow;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlowBody;
import com.darwinbox.customflows.objects.customflow.CustomFlow;
import com.darwinbox.customflows.objects.customflow.CustomFlowBody;
import com.darwinbox.customflows.objects.forms.CFForm;
import com.darwinbox.customflows.objects.forms.CFFormBody;
import com.darwinbox.customflows.objects.workflows.CFWorkFLow;
import com.darwinbox.customflows.objects.workflows.CFWorkflowBody;
import com.darwinbox.customflows.services.*;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;

import java.util.*;

public class CustomFlowTestBase {

    private static HashMap<String, CustomFlowTestBase> obj;
    private String fileName;

    private CustomFlowTestBase(String fileName) {
        this.fileName = fileName;
    }

    public static CustomFlowTestBase getObject(String baseCFFile) {

        if (obj == null)
            obj = new HashMap<>();

        CustomFlowTestBase cftb = obj.get(baseCFFile);

        if (cftb == null) {
            cftb = new CustomFlowTestBase(baseCFFile);
            cftb.loadData();
            obj.put(baseCFFile, cftb);
        }
        return cftb;
    }


    public static CustomFlowTestBase getObject() {
        return getObject("CustomFlowSettings.xlsx");
    }

    private void loadData() {

        //createCFForm();
        //createCFSkipSettings();
        //createCFSLASettings();
        //createCFApprovalFlow();
        // createCFWorkflow();
        createCustomFlow();
        // then create Cusotm flow using above ;

    }

    public void createCFForm() {

        List<Map<String, String>> cfFormDataList = readDatafromSheet("CFForm");
        List<Map<String, String>> cfFormBodyDataList = readDatafromSheet("CFFormBody");

        List<CFFormBody> cfFormBodyList = new ArrayList<>();
        List<CFForm> cfFormList = new ArrayList<>();

        //with all values in CFFormBody excel sheet create a java object
        for (Map<String, String> data : cfFormBodyDataList) {
            CFFormBody cfFormBody = new CFFormBody();
            cfFormBody.toObject(data);
            cfFormBodyList.add(cfFormBody);
        }
        //with all values in CFFormBody excel sheet create a java object
        for (Map<String, String> data : cfFormDataList) {
            CFForm cfForm = new CFForm();
            cfForm.toObject(data);

            //form body field types will be set here
            String bodyObjects = data.get("Body");
            String[] objectTypes = bodyObjects.split(",");

            for (String value : objectTypes) {
                int index = Integer.parseInt(value) - 1;
                cfForm.add(cfFormBodyList.get(index));
            }

            cfFormList.add(cfForm);

            CFFormService srvc = new CFFormService();
            srvc.createCFForm(cfForm);


        }


    }

    public void createCFSLASettings() {

        CFSLASettingsService srvc = new CFSLASettingsService();
        List<CFSLASettings> cfSLASettingsList = new ArrayList<>();
        List<Map<String, String>> cfSlaSettingsData = readDatafromSheet("SLASettings");
        //with all values in CFFormbody excel sheet create a java object
        for (Map<String, String> data : cfSlaSettingsData) {
            CFSLASettings cfSla = new CFSLASettings();
            cfSla.toObject(data);
            cfSLASettingsList.add(cfSla);
        }
        for (CFSLASettings cfSlaSetting : cfSLASettingsList) {
            srvc.createCFSLASettings(cfSlaSetting);
        }

    }

    public void createCFSkipSettings() {

        List<CFSkipSettings> cfSkipSettingsList = new ArrayList<>();
        CFSkipSettingsService srvc = new CFSkipSettingsService();
        List<Map<String, String>> cfSkipSettingsData = readDatafromSheet("SkipSettings");
        //with all values in CFFormbody excel sheet create a java object
        for (Map<String, String> data : cfSkipSettingsData) {
            CFSkipSettings cfSkip = new CFSkipSettings();
            cfSkip.toObject(data);
            cfSkipSettingsList.add(cfSkip);
        }

        for (CFSkipSettings cfSkipSetting : cfSkipSettingsList) {
            srvc.createCFSkipSettings(cfSkipSetting);
        }

    }

    public void createCFApprovalFlow() {

        List<Map<String, String>> cfApprovalFlowDataList = readDatafromSheet("CFApprovalFlow");
        List<Map<String, String>> cfApprovalFlowBodyDataList = readDatafromSheet("CFApprovalFlowBody");

        List<CFApprovalFlowBody> cfApprovalFlowBodyList = new ArrayList<>();
        List<CFApprovalFlow> cfApprovalFlowList = new ArrayList<>();

        //with all values in CFFormBody excel sheet create a java object
        for (Map<String, String> data : cfApprovalFlowBodyDataList) {
            CFApprovalFlowBody cfAFBody = new CFApprovalFlowBody();
            cfAFBody.toObject(data);
            cfApprovalFlowBodyList.add(cfAFBody);
        }

        //with all values in CFApprovalFlowBody excel sheet create a java object
        for (Map<String, String> data : cfApprovalFlowDataList) {
            CFApprovalFlow cfApprovalFlow = new CFApprovalFlow();
            cfApprovalFlow.toObject(data);

            //form body field types will be set here
            String bodyObjects = data.get("AFBody");
            String[] objectTypes = bodyObjects.split(",");

            for (String value : objectTypes) {
                int index = Integer.parseInt(value) - 1;
                cfApprovalFlow.add(cfApprovalFlowBodyList.get(index));
            }

            cfApprovalFlowList.add(cfApprovalFlow);
            CFApprovalFlowService cfAfSrv = new CFApprovalFlowService();
            cfAfSrv.createCFApprovalFlow(cfApprovalFlow);

        }
    }


    public void createCFWorkflow() {

        List<Map<String, String>> cfWorkflowDataList = readDatafromSheet("CFWorkFlow");
        List<Map<String, String>> cfWorkFlowBodyDataList = readDatafromSheet("CFWorkFlowBody");

        List<CFWorkflowBody> cfWorkflowBodyList = new ArrayList<>();
        List<CFWorkFLow> cfWorkflowList = new ArrayList<>();

        //with all values in CFWorkflowBody excel sheet create a java object
        for (Map<String, String> data : cfWorkFlowBodyDataList) {
            CFWorkflowBody cfWFBody = new CFWorkflowBody();
            cfWFBody.toObject(data);
            cfWorkflowBodyList.add(cfWFBody);
        }

        //with all values in CFWorkFlow excel sheet create a java object
        for (Map<String, String> data : cfWorkflowDataList) {
            CFWorkFLow cfWorkFlow = new CFWorkFLow();
            cfWorkFlow.toObject(data);

            //form body field types will be set here
            String bodyObjects = data.get("WF Body");
            String[] objectTypes = bodyObjects.split(",");

            for (String value : objectTypes) {
                int index = Integer.parseInt(value) - 1;
                cfWorkFlow.add(cfWorkflowBodyList.get(index));
            }

            cfWorkflowList.add(cfWorkFlow);
            CFWorkFLowService cfWFSrv = new CFWorkFLowService();
            cfWFSrv.createCFWorkflow(cfWorkFlow);

        }
    }


    public void createCustomFlow() {

        List<Map<String, String>> customflowDataList = readDatafromSheet("CustomFlow");
        List<Map<String, String>> customflowBodyDataList = readDatafromSheet("CustomFlowBody");

        List<CustomFlowBody> customflowBodyList = new ArrayList<>();
        List<CustomFlow> customflowList = new ArrayList<>();


        //with all values in CFWorkflowBody excel sheet create a java object
        for (Map<String, String> data : customflowBodyDataList) {
            CustomFlowBody cfBody = new CustomFlowBody();
            cfBody.toObject(data);
            customflowBodyList.add(cfBody);
        }

        //with all values in CFWorkFlow excel sheet create a java object
        for (Map<String, String> data : customflowDataList) {
            CustomFlow customFlow = new CustomFlow();
            customFlow.toObject(data);

            //form body field types will be set here
            String bodyObjects = data.get("CF Body");
            String[] objectTypes = bodyObjects.split(",");

            for (String value : objectTypes) {
                int index = Integer.parseInt(value) - 1;
                customFlow.add(customflowBodyList.get(index));
            }

            customflowList.add(customFlow);
            CustomFlowService customFlowSrv = new CustomFlowService();
            customFlowSrv.createCustomFlow(customFlow);

        }

    }



    private List<Map<String, String>> readDatafromSheet(String sheetname) {

        Map<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", fileName);
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }
}