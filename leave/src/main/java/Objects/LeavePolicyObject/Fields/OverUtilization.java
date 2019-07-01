package Objects.LeavePolicyObject.Fields;

 public class OverUtilization {
    public  boolean  indicator = false;
    public  boolean countExcessAsPaid = false;
    public  boolean countExcessAsUnPaid = false;
    public  boolean utilizeFrom = false;
    public  boolean dontAllowMoreThanYearlyAllocation = false;
    public  boolean dontAllowMoreThanYearlyAccural = false;
    public  int fixedOverUtilization = 0;
    public  String utlizeFromDropDown = null;//should maintain policy object
}