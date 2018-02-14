/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.Utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * @author balaji
 * @Creation_Date: 20 Nov 2017
 * @ClassName: ResouceHelper.java
 * @LastModified_Date: 20 Nov 2017
 */
public class ResourceHelper {
	public static String getResourcePath(String resource) {
		String path = getBaseResourcePath() + resource;
		return path;
	}

	public static String getBaseResourcePath() {
		String path = System.getProperty("user.dir");
		//System.out.println(path);
		return path;
	}

	public static InputStream getResourcePathInputStream(String path)
			throws FileNotFoundException {
		return new FileInputStream(ResourceHelper.getResourcePath(path));
	}

}
