/*
 * Filename: CurrentTimeRequest.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * CurrentTimeRequest is a ConcreteCommand for the LBMS.
 * This request returns the current time
 */

import java.text.SimpleDateFormat;

public class CurrentTimeRequest implements Request {
    private LBMS lbms;
    private String time;
    private String date;

    /**
     * Constructor for CurrentTimeRequest
     *
     * @param lbms the lbms to get the current time from
     */
    public CurrentTimeRequest(LBMS lbms) {
        this.lbms = lbms;
        this.time = null;
        this.date = null;
    }

    @Override
    public void execute() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        this.time = formatter.format(this.lbms.getTime());

        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd");
        this.date = formatterDate.format(this.lbms.getTime());
    }

    @Override
    public String response() {
        return "datetime," + this.date + "," + this.time + ";";
    }
}
