package DivisionServices;

import com.darwinbox.core.company.objects.CostCentre;
import com.darwinbox.core.company.objects.Division;
import com.darwinbox.core.employee.objects.BusinessUnit;
import com.darwinbox.core.employee.objects.Grade;
import com.darwinbox.core.services.*;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRUD extends TestBase {

    DivisionServices divisionServices = new DivisionServices();

    Division division = null;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void createDivision(HashMap<String, String> testData) throws  Exception{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();


        division = new Division();

        String name=new String("testDivision"+Math.random()).substring(0,16).replace(".","");
        division.setDivisionName(name);
        division.setCostCentreID(new CostCenterServices().getCostCenters().entrySet().iterator().next().getValue());

        String bu1=new BusinessUnitServices().getBusinessUnits().keySet().toArray()[0].toString();
        String bu2=new BusinessUnitServices().getBusinessUnits().keySet().toArray()[1].toString();

        List<String> bus= new ArrayList<>();
        bus.add(new BusinessUnitServices().getBusinessUnits().get(bu1));
        bus.add(new BusinessUnitServices().getBusinessUnits().get(bu2));

        division.setSelectBussinessUnitsID(bus);

        divisionServices.createDivision(division);


        if(divisionServices.getAllDivisions().get(division.getDivisionName())!=null ||
                divisionServices.getAllDivisions().get(division.getDivisionName())!=""){
            Reporter("Divison Created SuccessFully "+ division.getDivisionName(),"Info");
            division.setId(divisionServices.getAllDivisions().get(division.getDivisionName()));
        }
        else{
            Reporter("ERROR IN CREATING DIVISION"+ division.getDivisionName(),"Info");
        }



        division.setDivisionName("updated"+name);


        divisionServices.updateDivision(division);


        if(divisionServices.getAllDivisions().get(division.getDivisionName())!=null ||
                divisionServices.getAllDivisions().get(division.getDivisionName())!=""){
            Reporter("Divison Updated SuccessFully "+ division.getDivisionName(),"Info");
            division.setId(divisionServices.getAllDivisions().get(division.getDivisionName()));
        }
        else{
            Reporter("ERROR IN UPDATED DIVISION"+ division.getDivisionName(),"Info");
        }




        divisionServices.deleteDivision(division);




        if(divisionServices.getAllDivisions().get(division.getDivisionName())!=null ||
                divisionServices.getAllDivisions().get(division.getDivisionName())!=""){
            Reporter("ERROR IN DELETING DIVISION"+ division.getDivisionName(),"Info");

            // division.setId(divisionServices.getAllDivisions().get(division.getDivisionName()));
        }
        else{
            Reporter("Divison DELETED SuccessFully "+ division.getDivisionName(),"Info");
        }





    }
}


