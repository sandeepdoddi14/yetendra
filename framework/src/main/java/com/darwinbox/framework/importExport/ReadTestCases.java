package com.darwinbox.framework.importExport;

import com.darwinbox.framework.beans.Suite;
import com.darwinbox.framework.beans.TestCase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReadTestCases {

    public List<TestCase> tcList = new ArrayList<TestCase>();
    public List<Integer> tcIds = new ArrayList<Integer>();
    private String fileName;
    private String query = "all";
    private int queryType = 0;

    public ReadTestCases(String fileName) {
        this.fileName = fileName;
    }

    public void readTestCases(String query) {

        ExcelReader readConfig = new ExcelReader( fileName);
        readConfig.setExcelFileObject();

        List<Map<String, String>> configMap = readConfig.getExcelData("TestCaseMaster");

        parseQuery(query.toLowerCase());
        Iterator tcIter = configMap.iterator();

        while (tcIter.hasNext()) {

            Map<String, String> tcMap = (Map<String, String>) tcIter.next();
            if(tcMap.get("TCID").isEmpty())
                continue;
            TestCase testcase = parseConfig(tcMap);
            tcList.add(testcase);
        }

        readConfig.close();

    }

    private TestCase parseConfig(Map<String, String> tcMap) {

        TestCase testCase = new TestCase();

        testCase.setTestcaseId(Integer.parseInt(tcMap.get("TCID")));
        testCase.setClassname(tcMap.get("ClassName"));
        testCase.setDescription(tcMap.get("TestCaseDescription"));
        testCase.setModule(tcMap.get("Module"));
        testCase.setScope(tcMap.get("Scope"));
        testCase.setPriority(tcMap.get("Priority"));

        return testCase;

    }

    private boolean checkTc(TestCase testcase) {

        switch (queryType) {

            case 1:
                if (testcase.getModule().equalsIgnoreCase(query))
                    return true;
                return false;
            case 2:
                if (testcase.getScope().equalsIgnoreCase(query))
                    return true;
                return false;
            case 3:
                if (testcase.getPriority().equalsIgnoreCase(query))
                    return true;
                return false;
            case 4:
                if (tcIds.contains((testcase.getTestcaseId())))
                    return true;
                return false;
            default:
                return true;
        }
    }

    private void parseQuery(String query) {

        if (query.startsWith("mod:")) {
            queryType = 1;
            this.query = query.substring(4);
        } else if (query.startsWith("scope:")) {
            queryType = 2;
            this.query = query.substring(6);
        } else if (query.startsWith("pr:")) {
            queryType = 3;
            this.query = query.substring(3);
        } else if (query.startsWith("all")) {
            queryType = 0;
            this.query = query;
        } else {
            queryType = 4;
            String[] tcidlist = query.split(",");

            for (String id : tcidlist) {

                if (id.contains("-")) {

                    String tcRange[] = id.split("-");

                    int low = Integer.parseInt(tcRange[0]);
                    int high = Integer.parseInt(tcRange[1]);

                    while (low <= high) {
                        tcIds.add(low++);
                    }

                    continue;
                }

                int idnum = Integer.parseInt(id);
                tcIds.add(idnum);
            }
        }
    }

    public void createTestngFile() {

        XmlMapper xmlMapper = new XmlMapper();
        Suite suite = new Suite("TestAutomation");
        try {

            for(TestCase testCase : tcList) {
                if(!checkTc(testCase))
                    continue;
                String testName = testCase.getDescription();
                String className = testCase.getClassName();
                suite.addTest(testName, className);
            }
            xmlMapper.writeValue(new File(System.getProperty("testngFile")),suite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }
}
