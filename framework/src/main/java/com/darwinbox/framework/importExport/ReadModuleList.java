package com.darwinbox.framework.importExport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReadModuleList {

    public Map<String, String> moduleMap = new HashMap<String, String>();
    private String fileName;

    public ReadModuleList(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> readModules() {

        ExcelReader readConfig = new ExcelReader(System.getProperty("testdata") + fileName);
        readConfig.setExcelFileObject();

        List<Map<String, String>> moduleList = readConfig.getExcelData("modules");

        Iterator configIter = moduleList.iterator();

        while (configIter.hasNext()) {

            Map<String, String> config = (Map<String, String>) configIter.next();

            String method = config.get("Function Name");
            String cname = config.get("Class Name");
            String mname = config.get("Method Name");

            moduleMap.put(method + ".class", cname);
            moduleMap.put(method, mname);
        }

        return moduleMap;

    }

}
