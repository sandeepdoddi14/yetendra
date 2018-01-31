///**
// * 
// */
//package com.darwinbox.test.hrms.uiautomation.helper.Logger;
//
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
//import com.darwinbox.test.hrms.uiautomation.Utility.ResourceHelper;
//
//
//
///**
// * @author balaji
// * @Creation_Date:  20 Nov 2017 
// * @ClassName: LoggerHelper.java
// * @LastModified_Date:  20 Nov 2017 
// */
//public class LoggerHelper {
//	private static boolean root = false;
//
//	public static Logger getLogger(Class clas){
//		if (root) {
//			return Logger.getLogger(clas);
//		}
//		PropertyConfigurator.configure(ResourceHelper.getResourcePath("/src/main/resources/configfile/log4j.properties"));
//		root = true;
//		return Logger.getLogger(clas);
//	}
//
//}
