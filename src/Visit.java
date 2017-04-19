/*
 * Filename: Visit.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * Purpose: visits are tracked for library statistics purpose
 */

/* imports */

import java.util.Date;

/**
 * Visit represents a visit that a visitor has within the LBMS
 * Visits can be "not-complete" meaning the visitor is in the library
 */
public class Visit implements java.io.Serializable {

    /* Fields for a Visit */
    private Visitor visitor;
    private Date visitStartDate;
    private Date visitEndDate;

    /**
     * Constructor for Visitor
     *
     * @param visitor        - the visitor that is visiting
     * @param visitStartDate - the starting date of the visit
     */
    public Visit(Visitor visitor, Date visitStartDate) {
        this.visitor = visitor;
        this.visitStartDate = visitStartDate;

        // initially the visit isn't complete
        this.visitEndDate = null;
    }

    /**
     * endVisit is called with a date object to represent when the visit ended
     *
     * @param visitEndDate - the date object that the visit ended on.
     */
    public void endVisit(Date visitEndDate) {
        this.visitEndDate = visitEndDate;
    }

    /**
     * getVisitDuration calculates the difference of start and end date's and returns an int version
     * of the duration
     *
     * @return long representation of the duration of the visit
     * @throws AssertionError - if the visit isn't complete, no time can be calculated.
     */
    public long getVisitDuration() {
        assert isComplete();

        return this.visitEndDate.getTime() - this.visitStartDate.getTime();
    }

    /**
     * isComplete tells whether the visit object has been completed.
     *
     * @return - boolean value if the visit is complete
     */
    public boolean isComplete() {
        return this.visitEndDate != null;
    }
}
