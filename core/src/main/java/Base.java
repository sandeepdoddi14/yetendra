/*
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.services.ServiceTests;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Base  extends TestBase {

    protected static List<Band> getBandsFromExcelSheet() {
        List<Map<String, String>> excelData = ExcelUtils.readDatafromSheet("create");
        List<Band> band= new ArrayList<>();

        for (Map<String, String> data : excelData) {

            Band bandObject = new Band();
            bandObject.setBandName(data.get(""));
            bandObject.setDescription(data.get(""));

            if(new ServiceTests().getBands().get(bandObject.getBandName())!=null)
            {
                //cannoot update as it is creating duplicates

            }
            else
            {
                new ServiceTests().createBand(bandObject);
            }

            bands.add(policyObject);
        }

        //return leavePolicies.stream().filter(x -> x.getLeave_Type().contains(policy)).findFirst().get();
        return leavePolicies;
    }
}
*/
