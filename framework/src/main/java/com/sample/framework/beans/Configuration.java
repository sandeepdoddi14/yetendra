package com.sample.framework.beans;

import org.apache.log4j.Level;

import java.io.Serializable;

/**
 * Bean for test configuration which is defined in Config.xlsx file
 * Config.xlsx is present in src/main/resources
 */

public class Configuration implements Serializable {

    private String testcases;
    private String browser;
    private String instance;
    private Level loglevel;
    private String testDataFile = "TestCases.xlsx";
    private boolean isRunnable = true;

    public String getTestcases() {
        return testcases;
    }

    public void setTestcases(String testcases) {
        this.testcases = testcases;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public boolean isRunnable() {
        return isRunnable;
    }

    public void isRunnable(boolean isRunnable) {
        this.isRunnable = isRunnable;
    }

    public Level getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(Level loglevel) {
        this.loglevel = loglevel;
    }

    public String getTestDataFile() {
        return testDataFile;
    }

    public void setTestDataFile(String testDataFile) {
        this.testDataFile = System.getProperty("testdata") +"src/main/resources/"+ ((testDataFile == null) ? this.testDataFile : testDataFile);
    }

}
