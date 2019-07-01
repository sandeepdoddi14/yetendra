package Objects.LeavePolicyObject.Fields;

public class PrefixSuffixSetting {
    public Boolean indicator=false;
    public WeeklyOffPrefix weeklyOffPrefix=new WeeklyOffPrefix();
    public WeeklyOffSuffix weeklyOffSuffix=new WeeklyOffSuffix();
    public HolidayPrefix holidayPrefix=new HolidayPrefix();
    public HolidaySuffix holidaySuffix= new HolidaySuffix();
}