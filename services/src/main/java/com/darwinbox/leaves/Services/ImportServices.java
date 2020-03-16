package com.darwinbox.leaves.Services;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportServices  extends Services {


   public String importLeaveAdjustmentBalance(String emailIDoremplID,String leaveType,String leaveBalance,String year){

        String url= data.get("@@url") + "/import/processUserLeaveAdjustmentBalance";

        List<NameValuePair> deafultBody= new ArrayList<NameValuePair>();

       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalanceHeader[]","user_email"));
       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalanceHeader[]","leave_name"));
       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalanceHeader[]","user_leave_balance"));
       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalanceHeader[]","year"));




       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalance[0][user_email]",emailIDoremplID));
       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalance[0][leave_name]",leaveType));
       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalance[0][user_leave_balance]",leaveBalance));
       deafultBody.add(new BasicNameValuePair("ImportUserLeaveAdjustmentBalance[0][year]",year));


       deafultBody.add(new BasicNameValuePair("secondstep","2"));
       deafultBody.add(new BasicNameValuePair("resolve","Resolved errors, try again"));

       HashMap<String, String> headers = new HashMap<>();
       headers.put("X-Requested-With", "XMLHttpRequest");

       return doPost(url, headers, deafultBody);


    }


}
