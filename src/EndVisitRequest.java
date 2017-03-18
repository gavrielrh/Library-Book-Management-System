import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * EndVisitRequest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command ends the visit of the given visitor within the LBMS.
 */
public class EndVisitRequest implements Request {

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

    @Override
    public void execute(){
        if(this.lbms.hasVisitor(this.visitorId)) {
            //If the lbms has the visitor, assign this commands visitor to be the visitor
            this.visitor = this.lbms.getVisitors().get(this.visitorId);
            if ((this.visitor.isVisiting())) {
                //If the visitor visiting, and in the LBMS, it is valid
                this.visitDate = lbms.getTime();
                this.visitor.endVisit();
                this.visit = this.visitor.getCurrentVisit();
                this.visit.endVisit(this.visitDate);
            } else {
                this.isInvalidId = true;
            }
        }else{
            //If lbms doesn't have the visitor, it is an inValidId error
            this.isInvalidId = true;
        }
    }

    @Override
    public String response(){
        if(this.isInvalidId){
            return "arrive,invalid-id;";
        }else{
            return "depart," + this.visitorId + "," +
                    this.getVisitTime() + "," +
                    this.visit.getVisitDuration() + ";";
        }
    }

    /**
     * getVisitTime is a helper method to get the string of the visit Time
     * @return - String representation of the visit Time
     * @throws AssertionError if the visit wasn't valid, meaning it didn't have a date
     */
    public String getVisitTime(){
        assert this.visitDate != null;
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        return formatter.format(this.visitDate);
    }

}
