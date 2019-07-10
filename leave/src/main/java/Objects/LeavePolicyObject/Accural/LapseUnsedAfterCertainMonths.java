package Objects.LeavePolicyObject.Accural;

public class LapseUnsedAfterCertainMonths {
    private Boolean indicator=false;
    private int lapseMonths=0;

    public Boolean getIndicator() {
        return indicator;
    }

    public void setIndicator(Boolean indicator) {
        this.indicator = indicator;
        if(this.indicator==true)
            lapseMonths=1;
    }

    public int getLapseMonths() {
        return lapseMonths;
    }

    public void setLapseMonths(int lapseMonths) {
        this.lapseMonths = lapseMonths;
    }
}
