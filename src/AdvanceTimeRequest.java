/**
 * Filename: AdvanceTimeRequest
 * @author: Brendan Jones (bpj1651@rit.edu)
 * AdvanceTimeRequest is a concreteCommand for the LBMS.
 * This request can be invoked with either just days to move forward or days and hours
 */


/* imports */
import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;

import java.text.SimpleDateFormat;
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

    /**
     * execute checks for any invalid hours/days and advances time if appropriate
     */
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
            boolean nowClosed = !this.lbms.isOpen() || this.numDays > 0;
            /* If the advance in time made the LBMS close, end all visits */
            if(wasOpen && nowClosed){
                SimpleDateFormat formatMin = new SimpleDateFormat("mm");
                SimpleDateFormat formatSec = new SimpleDateFormat("ss");

                int currentNumHours = Integer.parseInt(String.format("%tH", oldTime));
                int currentNumMinutes = Integer.parseInt(formatMin.format(oldTime));
                int currentNumSec = Integer.parseInt(formatSec.format(oldTime));
                int currentNumMiliSec = Integer.parseInt(String.format("%tS", oldTime));

                long numMiliSecSinceDay = (long)currentNumMiliSec + (long)(currentNumSec * 1000) +
                        (long)(currentNumMinutes * 60000) + (long)(currentNumHours * 3.6e+6);
                oldTime = new Date(oldTime.getTime() - numMiliSecSinceDay);
                Date timeVisitsEnd = new Date(oldTime.getTime() + (this.lbms.getNumMinWhenClosed() * 60000));
                this.close(timeVisitsEnd);
            }
        }
    }

    /**
     * response returns the string whether there was an error or if it was successful
     * @return - String representation of what happened in execute.
     */
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
