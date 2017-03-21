/**
 * Filename: AdvanceTimeRequest
 * @author: Brendan Jones, bpj1651@rit.edu
 * AdvanceTimeRequest is a concreteCommand for the LBMS.
 * This request can be invoked with either just days to move forward or days and hours
 */


/* imports */
import java.util.ArrayList;
import java.util.Date;

public class AdvanceTimeRequest implements Request {
    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* all of the required information to AdvanceTime */
    private int numDays;
    private int numHours;

    /* boolean information associated with the concreteCommand to help with response */
    private boolean hasHours;
    private boolean invalidDays;
    private boolean invalidHours;

    /**
     * Constructor for concreteCommand. This constructor is used when the invoker just wants to advance days.
     * @param lbms - the LBMS itself.
     * @param numDays - the number of days to advance
     */
    public AdvanceTimeRequest(LBMS lbms, int numDays){
        this.numDays = numDays;
        this.lbms = lbms;
        //If this constructor is used, it can be assumed hours are not being advanced.
        this.hasHours = false;
        //Initially assume no errors. Execute will check for errors to help response.
        this.invalidDays = false;
        this.invalidHours = false;
    }

    /**
     * Constructor for concreteCommand. This constructor is used when the invoker wants to advance days and hours
     * @param lbms - the LBMS itself.
     * @param numDays - the number of days to advance
     * @param numHours - the number of hours to advance.
     */
    public AdvanceTimeRequest(LBMS lbms, int numDays, int numHours){
        this.numDays = numDays;
        this.lbms = lbms;
        this.numHours = numHours;
        //If this constructor is used, hours are being advanced.
        this.hasHours = true;
        //Initially assume no errors. Execute will check for errors to help response.
        this.invalidDays = false;
        this.invalidHours = false;
    }

    @Override
    public void execute(){
        /* check for an "invalid" number of days */
        this.invalidDays = (!(this.numDays >= this.lbms.MIN_ADVANCE_DAYS &&
                this.numDays <= this.lbms.MAX_ADVANCE_DAYS));

        if(!invalidDays) {
            //initially set an added time long (representing milliseconds to add to the LBMS time
            long addedTime = 0;
            long currentTime = this.lbms.getTime().getTime();

            //Add the amount of milliseconds of a day, for however many amount of days
            addedTime += (long) (this.numDays * 8.64e+7);

            //If request is also advancing hours, additional error check is required, along with advancing errors
            if (this.hasHours) {
                this.invalidHours = (!(this.numHours >= this.lbms.MIN_ADVANCE_HOURS &&
                        this.numHours <= this.lbms.MAX_ADVANCE_HOURS));
                if(!invalidHours) {
                    //Add the amount of milliseconds of an hour, for however many amount of hours
                    addedTime += (long) (this.numHours * 3.6e+6);
                    //update the lbms time
                    this.lbms.setTime(addedTime + currentTime);
                }
            }else {
                //update lbms time
                this.lbms.setTime(addedTime + currentTime);
            }
        }
    }

    @Override
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
