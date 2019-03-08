/**
 * 
 */
package com.darwinbox.framework.uiautomation.Utility;

import com.darwinbox.framework.uiautomation.base.TestBase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class ResourceHelper {
	public static String getResourcePath(String resource) {
		String path = TestBase.datapath + resource;
		return path;
	}

	public static String getBaseResourcePath() {
		String path = System.getProperty("user.dir");
		return path;
	}

	public static InputStream getResourcePathInputStream(String path)
			throws FileNotFoundException {
		return new FileInputStream(ResourceHelper.getResourcePath(path));
	}

}
