/**
 * Filename: BeginVisitRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * BeginVisitRequest represents a ConcreteCommand within the Command Design pattern.
 * Invoking the command begins the visit of the given visitor within the LBMS.
 */

/* imports */
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeginVisitRequest implements Request, UndoableCommand {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* All the required information to begin a visit */
    private Visitor visitor;
    private String visitorId;
    private Date visitDate;
    private Visit visit;

    /* boolean information associated with the ConcreteCommand to help response */
    private boolean isDuplicate;
    private boolean isInvalidId;
    private boolean libraryOpen;

    private boolean undoSuccess;


    /**
     * Constructor for the BeginVisitRequest
     * @param lbms - the system itself. This is so execute can call lbms commands
     * @param visitorId - the Id of the visitor beginning a request.
     */
    public BeginVisitRequest(LBMS lbms, String visitorId){
        this.lbms = lbms;
        this.visitorId = visitorId;
        /* check if the libaray is open */
        this.libraryOpen = this.lbms.isOpen();
        /* initially set potential errors to false */
        this.isDuplicate = false;
        this.isInvalidId = false;
        /* initially no visitor or Date */
        this.visitor = null;
        this.visitDate = null;
    }

    @Override
    public void execute() {
        if (this.libraryOpen) {
            if (this.lbms.hasVisitor(this.visitorId)) {
                //If the lbms has the visitor, assign this commands visitor to be the visitor
                this.visitor = this.lbms.getVisitors().get(this.visitorId);
                if (this.visitor.isVisiting()) {
                    //If the visitor is already visiting, the error is a duplicate
                    this.isDuplicate = true;
                } else {
                    //If a visit is valid, get the lbms time for the visit
                    this.visitDate = lbms.getTime();
                    Visit startingVisit = new Visit(this.visitor, this.visitDate);
                    this.visitor.setCurrentVisit(startingVisit);
                    this.lbms.addVisit(startingVisit);
                    this.visit = startingVisit;
                }
            } else {
                //If lbms doesn't have the visitor, it is an inValidId error
                this.isInvalidId = true;
            }
        }
    }

    @Override
    public String response(){
        if(!this.libraryOpen){
            return "arrive,cannot-arrive-library-closed;";
        }
        else if(this.isDuplicate){
            return "arrive,duplicate;";
        } else if (this.isInvalidId) {
            return "arrive,invalid-id;";
        }else{
            return "arrive," + this.visitorId + "," +
                    this.getVisitDate() + "," +
                    this.getVisitTime() + ";";
        }
    }

    /**
     * getVisitTime is a helper method to get the string of the visit Time
     * @return - String representation of the visit Time
     * @throws AssertionError if the visit wasn't valid, meaning it didn't have a date
     */
    public String getVisitTime(){
        assert this.visitDate != null;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(this.visitDate);
    }

    /**
     * getVisitDate is a helper method to get the string of the visit Date
     * @return - String representaton of the visit Date
     * @throws AssertionError if the visit wasn't valid, meaning it didn't have a date
     */
    public String getVisitDate(){
        assert this.visitDate != null;
        return LBMS.dateFormatter.format(this.visitDate);
    }

    public boolean undo(){
        if(!this.libraryOpen || this.isInvalidId || this.isDuplicate){
            this.undoSuccess = false;
            return false;
        }else{
            this.visitor.setCurrentVisit(null);
            this.lbms.removeVisit(this.visit);
            this.undoSuccess = true;
            return true;
        }
    }

    public boolean redo(){
        if(this.undoSuccess){
            this.visitor.setCurrentVisit(visit);
            this.lbms.addVisit(this.visit);
            return true;
        }else{
            return false;
        }
    }
}
