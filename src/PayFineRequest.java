/**
 * Created by brendanjones44 on 3/20/17.
 */
public class PayFineRequest implements Request {

    private LBMS lbms;

    private String visitorId;

    private double amount;

    private boolean invalidAmount;

    private boolean invalidVisitorId;

    private Visitor visitor;

    public PayFineRequest(LBMS lbms, String visitorId, double amount){
        this.lbms = lbms;
        this.visitorId = visitorId;
        this.amount = amount;
    }

    public void execute(){
        if(this.lbms.hasVisitor(visitorId)) {
            this.invalidAmount = (this.visitor.getFine() < this.amount);
            if(!invalidAmount){
                this.visitor.payFine(this.amount);
                this.lbms.payFine(this.amount);
            }
        }else{
            this.invalidVisitorId = true;
        }
    }

    public String response(){
        double balance = this.visitor.getFine();
        if(this.invalidVisitorId){
            return "pay,invalid-visitor-id;";
        }else if(this.invalidAmount){
            return "pay,invalid-amount," + Double.toString(this.amount) + "," + Double.toString(balance) + ";";
        }else{
            return "pay,success," + Double.toString(balance) + ";";
        }
    }
}
