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
    private Date visitStartDate;
    private Date visitEndDate;
    private boolean isComplete;
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**
     * Constructor for Visitor
     * @param visitor - the visitor that is visiting
     * @param visitStartDate - the starting date of the visit
     */
    public Visit(Visitor visitor, Date visitStartDate){
        this.visitor = visitor;
        this.visitStartDate = visitStartDate;
        // initially the visit isn't complete
        this.visitEndDate = null;
        this.isComplete = false;
    }

    public Visit(Visitor visitor, Date visitStartDate, Date VisitEndDate){
        this.visitor = visitor;
        this.visitStartDate = visitStartDate;
        this.visitEndDate = visitEndDate;
    }

    /**
     *
     * @param visitEndDate
     */
    public void endVisit(Date visitEndDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.visitEndDate = visitEndDate;
        this.isComplete = true;
    }

    /**
     * getVisitDuration calculates the difference of start and end date's and returns an int version
     * of the duration
     * @return long representation of the duration of the visit
     */
    public long getVisitDuration() {
        assert this.isComplete;
        return this.visitEndDate.getTime() - this.visitStartDate.getTime();
    }

    public boolean isComplete(){
        return this.isComplete;
    }

    public Visitor getVisitor(){
        return this.visitor;
    }

    public long getVisitStartTime(){
        return this.visitStartDate.getTime();
    }

    public long getVisitEndTime(){
        assert this.isComplete();
        return this.visitEndDate.getTime();
    }
}
