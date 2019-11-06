package LocationsServices;

import com.darwinbox.HelperServices;
import com.darwinbox.Services;
import com.darwinbox.core.company.objects.JobDescription;
import com.darwinbox.core.location.objects.Locations;
import com.darwinbox.core.services.JobDescriptionServices;
import com.darwinbox.core.services.location.LocationsServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Random;

public class CRUD extends Services {

    LocationsServices locationServices = new LocationsServices();

    Locations locations = null;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void crudLocation(HashMap<String, String> testData) throws  Exception{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();


        locations = new Locations();


        locations.setForCompanies(getCurrentInstanceGroupCompany());
        locations.setCityType("test");
        locations.setOfficeAddress("test office address");
        locations.setOfficeArea("test office area");

        HashMap<Object,Object> countries=new HelperServices().getCountries();
        String country=countries.keySet().toArray()[new Random().nextInt(countries.keySet().toArray().length)].toString();

        Reporter("Selected Country is..."+country,"Info");
        locations.setCountry(country);

        HashMap<String,String> states= new HelperServices().getStates(country);
        String state=states.keySet().toArray()[new Random().nextInt(states.keySet().toArray().length)].toString();

        Reporter("Selected State is..."+state,"Info");
        locations.setState(state);

        HashMap<String,String> cities=new HelperServices().getCities(states.get(state));
        String city=cities.keySet().toArray()[new Random().nextInt(cities.keySet().toArray().length)].toString();

        Reporter("Selected City is..."+city,"Info");


        locations.setOfficeCity(city);

        locations.setPinCode("500081");
        locations.setOfficeEmail("test@email.com");
        locations.setMobileNo("9545777000");
        locations.setTelNo("8887770007");
        locations.setRegiteredOffice(true);
        locations.setCostCenter("test");
        locations.setWorkAreaCode("54685");
        locations.setLocationArea("dabagardens");


        String createLocationResponse=locationServices.createLocationType(locations);
         if(createLocationResponse!="")
         {
             Reporter("Location Created Successfully","Info");
         }
         else
         {
             Reporter("ERROR In creating Location","Error");
         }


         locationServices.getLocations();









    }
}


