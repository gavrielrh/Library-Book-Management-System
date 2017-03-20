import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Visit represents a visit that a visitor has within the LBMS
 * Visits can be "not-complete" meaning the visitor is in the library
 */
public class Visit {

    /* Fields for a Visit */
    private Visitor visitor;
    private String visitStartDate;
    private String visitEndDate;
    private boolean isComplete;
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**
     * Constructor for Visitor
     * @param visitor - the visitor that is visiting
     * @param visitStartDate - the starting date of the visit
     */
    public Visit(Visitor visitor, Date visitStartDate){
        this.visitor = visitor;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.visitStartDate = dateFormat.format(visitStartDate);
        // initially the visit isn't complete
        this.visitEndDate = null;
        this.isComplete = false;
    }

    /**
     *
     * @param visitEndDate
     */
    public void endVisit(Date visitEndDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.visitEndDate = dateFormat.format(visitEndDate);
        this.isComplete = true;
    }

    /**
     * getVisitDuration calculates the difference of start and end date's and returns an int version
     * of the duration
     * @return int representation of the duration of the visit
     */
    public int getVisitDuration() {
        assert this.isComplete;
        //Duration in minutes
        Date start = null;
        Date end = null;
        try {
            start = format.parse(this.visitStartDate);
            end = format.parse(this.visitEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int duration = (int)((end.getTime()-start.getTime())/1000*60);
        return duration;
    }

}
