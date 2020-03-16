package com.darwinbox.framework.utils.SAS;

import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.log4j.Logger;

import java.text.MessageFormat;

public class SelTestCase extends TestBase {


    ////*****Dummy Stuff To Support Selector Util*********//////

    private static String STARTING_THREAD = "<font color=#035d6d>>>> <u> Starting {0} </u> >>> </font>";
    private static String ENDING_THREAD = "<font color='green'><<< Ending {0} <<< </font>";
    public static String DEBUGGING_TEXT = "{0}";


    protected static Logger logs= Logger.getLogger("logs");

    protected static String getBrowserName() {
        //return browserName;
        return config.getBrowser();
    }



    protected static void getCurrentFunctionName(Boolean start)
    {
        if (start){
             logs.debug(MessageFormat.format(STARTING_THREAD, Thread.currentThread().getStackTrace()[2]));
        }
        else
        {
            logs.debug(MessageFormat.format(ENDING_THREAD, Thread.currentThread().getStackTrace()[2]));
        }

    }

    ////////////////////////////////////////////////////////////
}
