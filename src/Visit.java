import java.sql.Time;
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

    /**
     *
     * @param visitEndDate
     */
    public void endVisit(Date visitEndDate){
        this.visitEndDate = visitEndDate;
        this.isComplete = true;
    }

    /**
     * getVisitDuration calculates the difference of start and end date's and returns a string version
     * of the duration
     * @return String representation of the duration of the visit
     */
    public String getVisitDuration(){
        assert this.isComplete;
        //TODO: Replace with the visit's duration
        return "HH-MM-SS";
    }

}
