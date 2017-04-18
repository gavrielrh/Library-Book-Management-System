/**
 * Filname: EndVisitRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * EndVisitRequest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command ends the visit of the given visitor within the LBMS.
 */

/* imports */
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class EndVisitRequest implements Request, UndoableCommand {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* All the required information to end a visit */
    private Visitor visitor;
    private String visitorId;
    private Date visitDate;
    private Visit visit;
    /* boolean information associated with the ConcreteCommand to help response */
    private boolean isInvalidId;


    /**
     * Constructor for the EndVisitRequest
     * @param lbms - the system itself. This is so execute can call lbms commands
     * @param visitorId - the Id of the visitor ending a visit.
     */
    public EndVisitRequest(LBMS lbms, String visitorId){
        this.lbms = lbms;
        this.visitorId = visitorId;
        /* initially set potential errors to false */
        this.isInvalidId = false;
        /* initially no visitor, Date, or visit */
        this.visit = null;
        this.visitor = null;
        this.visitDate = null;
    }

    /**
     * execute check's if the library has the visitor given the id requested and
     * also checks if the visitor is visiting.
     * If appropriate, execute then ends the visit for the visitor.
     */
    @Override
    public void execute(){
        if(this.lbms.hasVisitor(this.visitorId)) {
            //If the lbms has the visitor, assign this commands visitor to be the visitor
            this.visitor = this.lbms.getVisitors().get(this.visitorId);
            if ((this.visitor.isVisiting())) {
                //If the visitor visiting, and in the LBMS, it is valid
                this.visitDate = lbms.getTime();
                this.visit = this.visitor.getCurrentVisit();
                this.visit.endVisit(this.visitDate);
                this.visitor.endVisit();
            } else {
                this.isInvalidId = true;
            }
        }else{
            //If lbms doesn't have the visitor, it is an inValidId error
            this.isInvalidId = true;
        }
    }

    /**
     * given any error caught with visitor id, response returns the appropriate response for the command
     * @return - String response representing the result from executing the command.
     */
    @Override
    public String response(){
        if(this.isInvalidId){
            return "arrive,invalid-id;";
        }else{
            return "depart," + this.visitorId + "," +
                    LBMS.timeFormatter.format(this.visitDate) + "," +
                    this.getVisitDuration(this.visit.getVisitDuration()) + ";";
        }
    }


    /**
     * getVisitDuration returns a string friendly version of the visit's duration
     * @param millis - the long value of ms of the visit
     * @return - String version of the duration
     */
    private String getVisitDuration(long millis){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public boolean undo(){
        if(this.isInvalidId){
            return false;
        }else{
            this.visitor.setCurrentVisit(visit);
            this.visit = this.visitor.getCurrentVisit();
            this.visit.endVisit(null);
            return true;
        }
    }

    public boolean redo(){
        if(this.isInvalidId){
            return false;
        }else{
            this.visit.endVisit(visitDate);
            this.visitor.endVisit();
            return true;
        }
    }
}
