package Objects;

import org.json.JSONObject;
import java.util.List;

public class Holiday {

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public JSONObject toService(){

        JSONObject obj = new JSONObject();
        obj.put("holiday_name",name);
        obj.put("holiday_date",date);
        obj.put("holiday_optional",isOptional ? 1 : 0);
        obj.put("holiday_location",location);
        obj.put("errors",errors);

        return obj;
    }

    String name;
    String date;
    boolean isOptional;
    boolean repeat;
    List<String> location,errors;

}
