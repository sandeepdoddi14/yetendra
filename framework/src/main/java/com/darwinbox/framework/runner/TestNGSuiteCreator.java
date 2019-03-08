package com.darwinbox.framework.runner;

import com.darwinbox.framework.beans.Configuration;
import com.darwinbox.framework.importExport.ReadTestCases;
import com.darwinbox.framework.importExport.ReadTestConfiguration;
import com.darwinbox.framework.utils.FileUtilsSerialized;
import com.darwinbox.framework.utils.QueryExcel;

public class TestNGSuiteCreator {

	public static void main(String[] args) {

		String fileName = System.getProperty("configFile");
		String configFile = System.getProperty("testdata") + "config";

		ReadTestConfiguration configuration = new ReadTestConfiguration(fileName);
		Configuration config = configuration.readConfigs().get(0);

		FileUtilsSerialized.writeToFile(config, configFile);

		ReadTestCases rdtcs = new ReadTestCases(config.getTestDataFile());
		rdtcs.readTestCases(config.getTestcases());
		rdtcs.createTestngFile();
	}

}
