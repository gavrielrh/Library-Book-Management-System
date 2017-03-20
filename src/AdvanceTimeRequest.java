import java.util.ArrayList;
import java.util.Date;

/**
 * Created by brendanjones44 on 3/20/17.
 */
public class AdvanceTimeRequest implements Request {
    private LBMS lbms;
    private int numDays;
    private int numHours;
    private boolean hasHours;
    private boolean invalidDays;
    private boolean invalidHours;

    public AdvanceTimeRequest(LBMS lbms, int numDays){
        this.numDays = numDays;
        this.lbms = lbms;
        this.hasHours = false;
    }

    public AdvanceTimeRequest(LBMS lbms, int numDays, int numHours){
        this.numDays = numDays;
        this.lbms = lbms;
        this.numHours = numHours;
        this.hasHours = true;
    }

    public void execute(){
        this.invalidDays = (!(this.numDays >= 0 && this.numDays <= 7));
        if(!invalidDays) {
            long addedTime = 0;
            long currentTime = this.lbms.getTime().getTime();
            addedTime += (long) (this.numDays * 8.64e+7);
            if (this.hasHours) {
                this.invalidHours = (!(this.numHours >= 0 && this.numHours <= 23));
                if(!invalidHours) {
                    addedTime += (long) (this.numHours * 3.6e+6);
                    this.lbms.setTime(addedTime + currentTime);
                }
            }else {
                this.lbms.setTime(addedTime + currentTime);
            }
        }
    }

    public String response(){
        if(this.invalidDays){
            return "advance,invalid-number-of-days," + Integer.toString(numDays) + ";";
        }else if(this.invalidHours){
            return "advance,invalid-number-of-hours," + Integer.toString(numHours) + ";";
        }else{
            return "advance,success";
        }
    }
}
