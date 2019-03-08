package com.darwinbox.framework.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Java bean for test cases
 * Each and every test case is treated as an object of this class
 */

public class TestCase {

    private int testcaseId;
    private String description;
    private String classname;
    private String module;
    private String priority;
    private String scope;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(int testcaseId) {
        this.testcaseId = testcaseId;
    }

    public String getClassName() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname= classname;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
