package com.darwinbox.attendance.shifts;

import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CRUDOperations extends TestBase {

    @BeforeClass
    public void beforeClass(){
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test
    public void testShiftsCreation (){

    }

}
