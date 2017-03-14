import java.sql.Time;
import java.util.Date;

/**
 * Visit represents a visit that a visitor has within the LBMS
 * Visits can be "not-complete" meaning the visitor is in the library
 */
public class Visit {

    /* Fields for a Visit */
    private Visitor visitor;
    private Date visitDate;
    private boolean isComplete;

    /**
     * Constructor for Visitor
     * @param visitor
     * @param visitDate
     */
    public Visit(Visitor visitor, Date visitDate){
        this.visitor = visitor;
        this.visitDate = visitDate;
        this.isComplete = false;
    }

}
