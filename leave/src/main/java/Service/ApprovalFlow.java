package Service;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ApprovalFlow {

    private String name;
    private String type;
    private String id;
    private ApprovalLevels level1 = new ApprovalLevels(); //for obj not being null, if in excel data value is null, this does not throw null pointer exception
    private ApprovalLevels level2 = new ApprovalLevels();
    private ApprovalLevels level3= new ApprovalLevels();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ApprovalLevels getLevel1() {
        return level1;
    }

    public void setLevel1(ApprovalLevels level1) {
        this.level1 = level1;
    }

    public ApprovalLevels getLevel2() {
        return level2;
    }

    public void setLevel2(ApprovalLevels level2) {
        this.level2 = level2;
    }

    public ApprovalLevels getLevel3() {
        return level3;
    }

    public void setLevel3(ApprovalLevels level3) {
        this.level3 = level3;
    }

    public List<NameValuePair> getMap() {

        List<NameValuePair> body = new ArrayList<>();

        body.add(new BasicNameValuePair("user[name]",getName()));
        body.add(new BasicNameValuePair("TenanatApprovalFlows[type]",getType()));

        body.addAll(level1.getMap("1"));
        body.addAll(level2.getMap("2"));
        body.addAll(level3.getMap("3"));

        return body;
    }
}
