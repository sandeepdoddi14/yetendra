/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.Utility;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author balaji
 * @Creation_Date:  1 Dec 2017 
 * @ClassName: UtilityHelper.java
 * @LastModified_Date:  1 Dec 2017 
 */
public class UtilityHelper {
	
	/**
	 * This method create a directory if it does not exists
	 * @param DirectoryName
	 */
	public static void CreateADirectory(String DirectoryName) {
		
		String workingDirectory = ResourceHelper.getBaseResourcePath();
		String dir = workingDirectory + File.separator + DirectoryName;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdir();
		} 
	}
	
	public static String getCurrentDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
		Calendar cal = Calendar.getInstance();
		String time = "" + dateFormat.format(cal.getTime());
		return time;
	}

	public static String getCurrentDate() {
		return getCurrentDateTime().substring(0, 11);
	}

}
