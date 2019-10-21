package Service;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import org.openqa.selenium.By;


public class LeaveBalanceAPI extends TestBase {
    private String applicationUrl = data.get("@@url");
    public  String empId;
    public  String leaveType;
    private String requestUrl=applicationUrl+"/emailtemplate/UserLeaveBalance?id="+empId+"&leave="+leaveType;

    public LeaveBalanceAPI(String empId,String leaveType){
        this.empId=empId;
        this.leaveType=leaveType;
    }

    public leaveBalanceResponse leaveBalnce(){
        String requestUrl=applicationUrl+"/emailtemplate/UserLeaveBalance?id="+empId+"&leave="+leaveType;
        driver.get(requestUrl);
        String response=driver.findElement(By.xpath("/html/body")).getText().trim();
        Gson gson = new Gson();
        leaveBalanceResponse obj=gson.fromJson(response, leaveBalanceResponse.class);
        return obj;
    }

    public double getBalance(){
        return  leaveBalnce().balance;
    }

    public double getPendingLeaves(){return leaveBalnce().pending_leaves;}

    public double getApprovedLeaves(){return leaveBalnce().approved_leaves;}

    public double getCarryForwardBalance(){return leaveBalnce().prevBalance;}

    public double getTotalBalance(){return leaveBalnce().total;}

}

//mapper class to convert json to java object
//Leave Balance Response
final class leaveBalanceResponse {
    @JsonProperty("leave_name")
    public String leave_name;
    @JsonProperty("total")
    public double total;
    @JsonProperty("balance")
    public double balance;
    @JsonProperty("prevBalance")
    public double prevBalance;
    @JsonProperty("approved_leaves")
    public double approved_leaves;
    @JsonProperty("pending_leaves")
    public double pending_leaves;
    @JsonProperty("accured_till_now")
    public double accured_till_now;
}






