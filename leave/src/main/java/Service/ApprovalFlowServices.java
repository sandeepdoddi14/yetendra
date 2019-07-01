package Service;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApprovalFlowServices extends Service {


    public Map<String, String> getAllApprovalFlows() {

        Map<String, String> data = new HashMap<>();

        String url = getData("@@url") + "/settings/getApprovalFlows";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject jsonres = new JSONObject(response);
        JSONArray arr = jsonres.getJSONArray("aaData");

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;
            String name = objarr.getString(0);
            String id = objarr.getString(1);

            Pattern p = Pattern.compile("id=\"\\w+\"");
            Matcher m = p.matcher(id);


            if (m.find())
                data.put(name, (StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]));

        }
        return data;
    }


    public ApprovalFlow getApprovalFlowByname(String name) {

        Map<String, String> data = getAllApprovalFlows();

        if (!data.containsKey(name))
            return null;

        String url = getData("@@url") + "/settings/getApprovalFlow";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        doGet(url, headers);
        // JSONObject response= new JSONObject(doGet(url, headers));
        ApprovalFlow approvalFlow = new ApprovalFlow();
        approvalFlow.getMap();
        //approvalFlow.jsonToObject(response.toMap());

        return approvalFlow;

    }

    public void createApprovalFlow(ApprovalFlow flow) {


        String url = getData("@@url") + "/settings/company/approvalflow";




            List<NameValuePair> body = new ArrayList<>();
                body.addAll(flow.getMap());
           // body.add(new BasicNameValuePair("user[name]", ""));
           // body.add(new BasicNameValuePair("TenanatApprovalFlows[type]", ""));
           // body.add(new BasicNameValuePair("user[level1][]", ""));
            body.add(new BasicNameValuePair("yt0", "SAVE"));

            doPost(url, null, body);

    }



    public void updateApprovalFlow(ApprovalFlow flow) {

        List<NameValuePair> body = new ArrayList<>();
        body.addAll(flow.getMap());
        body.add(new BasicNameValuePair("TenanatApprovalFlows[id]",getAllApprovalFlows().get(flow.getName())));
        String url = getData("@@url") + "/settings/editApprovalFLow";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        doPost(url, headers,body);
        }


    public void deleteApprovalFlow(ApprovalFlow flow){

        deleteApprovalFlow(flow.getName());

    }

    public void deleteApprovalFlow(String name) {

        Map<String, String> data = getAllApprovalFlows();

        String url = getData("@@url") + "/settings/editApprovalFLow";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("resource", data.get(name));
        body.put("mode", "delete");

        doPost(url, headers, mapToFormData(body));

    }


}

