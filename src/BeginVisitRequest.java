import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BeginVisitReuqest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command begins the visit of the given visitor within the LBMS.
 */
public class BeginVisitRequest implements Request {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* All the required information to begin a visit */
    private Visitor visitor;
    private String visitorId;
    private Date visitDate;

    /* boolean information associated with the ConcreteCommand to help response */
    private boolean isDuplicate;
    private boolean isInvalidId;


    /**
     * Constructor for the BeginVisitRequest
     * @param lbms - the system itself. This is so execute can call lbms commands
     * @param visitorId - the Id of the visitor beginning a request.
     */
    public BeginVisitRequest(LBMS lbms, String visitorId){
        this.lbms = lbms;
        this.visitorId = visitorId;
        /* initially set potential errors to false */
        this.isDuplicate = false;
        this.isInvalidId = false;
        /* initially no visitor or Date */
        this.visitor = null;
        this.visitDate = null;
    }

    @Override
    public void execute(){
        if(this.lbms.hasVisitor(this.visitorId)) {
            //If the lbms has the visitor, assign this commands visitor to be the visitor
            this.visitor = this.lbms.getVisitors().get(this.visitorId);
            if (this.visitor.isVisiting()) {
                //If the visitor is already visiting, the error is a duplicate
                this.isDuplicate = true;
            } else {
                //If a visit is valid, get the lbms time for the visit
                this.visitDate = lbms.getTime();
                //Set the visitors isVisiting to be true
                this.visitor.startVisit();
                this.lbms.beginVisit(new Visit(this.visitor, this.visitDate));
            }
        }else{
            //If lbms doesn't have the visitor, it is an inValidId error
            this.isInvalidId = true;
        }
    }

    @Override
    public String response(){
        if(this.isDuplicate){
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
     */
    public String getVisitTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        return formatter.format(this.visitDate);
    }

    /**
     * getVisitDate is a helper method to get the string of the visit Date
     * @return - String representaton of the visit Date
     */
    public String getVisitDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.visitDate);
    }
}
