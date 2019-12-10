package com.darwinbox.recruitment;

import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlow;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.HiringWorkFlow.HiringWorkFlow;
import com.darwinbox.recruitment.objects.HiringWorkFlow.HiringWorkFlowLineItems;
import com.darwinbox.recruitment.objects.jobsPages.HiringWFThirdPage;
import com.darwinbox.recruitment.objects.jobsPages.HiringWFThirdPageBody;
import com.darwinbox.recruitment.services.HiringWorkFlowService;
import com.darwinbox.recruitment.services.JobsPagesService;
import com.darwinbox.recruitment.services.RequisitionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecruitmentTestBase extends TestBase {

    private String fileName;
    private static HashMap<String, RecruitmentTestBase> obj;

    List<HiringWorkFlow> hiringWorkFlow = new ArrayList<>();
    List<HiringWorkFlowLineItems> hiringWorkFlowLineItems = new ArrayList<>();
    List<HiringWFThirdPage> hiringWFThirdPage = new ArrayList<>();
    List<HiringWFThirdPageBody> hiringWFThirdPageBody = new ArrayList<>();

    HiringWorkFlowService hiringWorkFlowService = new HiringWorkFlowService();
    JobsPagesService jobsPagesService = new JobsPagesService();

    private RecruitmentTestBase(String fileName) {
        this.fileName = fileName;
    }

    public static RecruitmentTestBase getObject(String baseFile) {

        if (obj == null)
            obj = new HashMap<>();

        RecruitmentTestBase baseData = obj.get(baseFile);

        if (baseData == null) {
            baseData = new RecruitmentTestBase(baseFile);
            //baseData.loadData();
            obj.put(baseFile, baseData);
        }
        return baseData;
    }

    private void loadData() {

        updateHiringWorkFlow();
        updateHiringWFThirdPage();
    }

    public void updateHiringWorkFlow(){

        List<Map<String, String>> hiringWorkFlowDataList = readDatafromSheet("HiringWF");
        List<Map<String, String>> hiringWorkFlowLineWiseDataList = readDatafromSheet("HiringWFLineWise");

        for (Map<String, String> data : hiringWorkFlowLineWiseDataList) {
            HiringWorkFlowLineItems lineItemsBody = new HiringWorkFlowLineItems();
            lineItemsBody.toObject(data);
            hiringWorkFlowLineItems.add(lineItemsBody);
        }

        for (Map<String, String> data : hiringWorkFlowDataList) {
            HiringWorkFlow hiringWorkFlowBody = new HiringWorkFlow();
            hiringWorkFlowBody.toObject(data);

            String bodyObjects = data.get("LineWiseBody");
            String[] objectTypes = bodyObjects.split(",");

            for (String value : objectTypes) {
                int index = Integer.parseInt(value) - 1;
                hiringWorkFlowBody.add(hiringWorkFlowLineItems.get(index));
            }

            hiringWorkFlowService.createSettings(hiringWorkFlowBody);

            hiringWorkFlow.add(hiringWorkFlowBody);

        }

    }

    public void updateHiringWFThirdPage(){

        List<Map<String, String>> HWF = readDatafromSheet("HiringWF");
        List<Map<String, String>> HWFbody = readDatafromSheet("HiringWFThirdPage");

        for (Map<String, String> data : HWFbody) {
            HiringWFThirdPageBody lineItemsBody = new HiringWFThirdPageBody();
            lineItemsBody.toObjectThirdPageBody(data);
            hiringWFThirdPageBody.add(lineItemsBody);
        }

        for(Map<String, String> data : HWF){
            HiringWFThirdPage hirWF = new HiringWFThirdPage();
            hirWF.toObjectThirdPage(data);

          String bodyObjects = data.get("HirWFThirdPageData");
          String[] objectTypes = bodyObjects.split(",");
          for (String value : objectTypes){

              int count = Integer.parseInt(value)-1;
              hirWF.add(hiringWFThirdPageBody.get(count));
          }

            String jobID=  new RequisitionService().searchActiveJobs();
            Reporter("Third page WorkFlow is updated for job ID :"+jobID,"PASS");

            jobsPagesService.hiringWorkFlow(hirWF,jobID);
            hiringWFThirdPage.add(hirWF);
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
