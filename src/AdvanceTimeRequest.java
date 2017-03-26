/**
 * Filename: AdvanceTimeRequest
 * @author: Brendan Jones (bpj1651@rit.edu)
 * AdvanceTimeRequest is a concreteCommand for the LBMS.
 * This request can be invoked with either just days to move forward or days and hours
 */


/* imports */
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

        /* Add the amount of milliseconds of a day, for however many amount of days */
        long addedTime = (long) (this.numDays * 8.64e+7);


        if(!invalidDays) {
            //If request is also advancing hours, additional error check is required, along with advancing errors
            if (this.hasHours) {
                this.invalidHours = ((this.numHours < this.lbms.MIN_ADVANCE_HOURS ||
                        this.numHours > this.lbms.MAX_ADVANCE_HOURS));
                if (!invalidHours) {
                    //Add the amount of milliseconds of an hour, for however many amount of hours
                    addedTime += (long) (this.numHours * 3.6e+6);
                }
            }
        }


        /* if there aren't any errors with request, update time and perform checks for closing if necessary */
        if(!this.invalidHours && !this.invalidDays){
            Date oldTime = this.lbms.getTime();
            boolean wasOpen = this.lbms.isOpen();
            //Adavance the actual LBMS time
            this.lbms.setTime(addedTime + oldTime.getTime());
            boolean nowClosed = !this.lbms.isOpen();

            /* If the advance in time made the LBMS close, end all visits */
            if(wasOpen && nowClosed){
                //Let's say it's 15:00 and we want to advance 8 hours.
                //The new time is 23:00, but all visits ended at 19:00.
                //So we need to get a new Date object representing:
                // - the current date(day) + 19:00
                //So the math here is as follows:
                //OldTime.getTime - (oldTime.getTime() % 8.64e+7)
                //this get's just the Date at 00:00
                //then add the Library closing times minutes * milliseconds in a minute
                //Thus getting the time the visits end to be the right Date object
                Date timeVisitsEnd = new Date(oldTime.getTime() -
                        (long)(oldTime.getTime() % 8.64e+7) + (this.lbms.LIBRARY_CLOSED_TIME * 60000));

                this.close(timeVisitsEnd);
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

    /**
     * close is a helper method used to end all visits at the closing time.
     * @param timeClosed - the Date object representing the day the library closed, at it's closing time
     */
    private void close(Date timeClosed){
        for(Visitor v : this.lbms.getVisitors().values()){
            v.endVisit();
        }
        for(Visit v : this.lbms.getVisits()){
            v.endVisit(timeClosed);
        }
    }
}
