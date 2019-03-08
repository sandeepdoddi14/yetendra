package com.darwinbox.framework.importExport;

import com.darwinbox.framework.beans.Configuration;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReadTestConfiguration {

    public List<Configuration> configlist = new ArrayList<Configuration>();
    private String fileName;

    public ReadTestConfiguration(String fileName) {
        this.fileName = fileName;
    }

    public List<Configuration> readConfigs() {

        ExcelReader readConfig = new ExcelReader( fileName);
        readConfig.setExcelFileObject();
        List<Map<String, String>> configMap = readConfig.getExcelData("Config");

        Iterator configIter = configMap.iterator();

        while (configIter.hasNext()) {

            Map<String, String> config = (Map<String, String>) configIter.next();
            Configuration configObj  = parseConfig(config);
            if(configObj.isRunnable())
                configlist.add(configObj);
        }

        return configlist;

    }

    public Configuration parseConfig(Map<String, String> config) {
        Configuration configuration = new Configuration();
        configuration.setTestcases(config.get("TestCases"));
        configuration.setBrowser(config.get("browser"));
        configuration.setInstance(config.get("Instance"));
        configuration.setLoglevel(getLogLevel(config.get("Log Level")));
        configuration.setTestDataFile(config.get("TestDataFile"));

        String runMode = config.get("RunMode").toLowerCase();
        if (runMode.equalsIgnoreCase("no"))
            configuration.isRunnable(false);
        return configuration;
    }

    private Level getLogLevel(String config) {

        if (config.equalsIgnoreCase("debug"))
            return Level.DEBUG;
        else if (config.equalsIgnoreCase("off"))
            return Level.OFF;
        else if (config.equalsIgnoreCase("error"))
            return Level.ERROR;
        else return Level.INFO;
    }
}
