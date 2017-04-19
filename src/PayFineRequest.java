/*
 * Filename: PayFineRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * PayFineRequest represents a ConcreteCommand in the Command Design Pattern used for Request subsystem.
 * PayFine will pay any possible fees the Visitor has in the LBMS.
 */

import java.util.ArrayList;

public class PayFineRequest implements Request, UndoableCommand {

    /* have the LBMS be in the request so commands can be executed */
    private LBMS lbms;

    /* information needed for the Request */
    private String visitorId;
    private double amount;

    /* boolean info so response can know what execute did */
    private boolean invalidAmount;
    private boolean invalidVisitorId;

    /* Visitor to check and pay fines */
    private Visitor visitor;

    private ArrayList<Transaction> transactionsBeforePaying;

    private ArrayList<Transaction> transactionsAfterPaying;

    /**
     * Constructor for PayFineRequest.
     *
     * @param lbms      - the LBMS itself so commands can be executed.
     * @param visitorId - the visitorId attempting to pay
     * @param amount    - the amount the visitor would like to pay
     */
    public PayFineRequest(LBMS lbms, String visitorId, double amount) {
        this.lbms = lbms;
        this.visitorId = visitorId;
        this.visitor = this.lbms.getVisitor(this.visitorId);
        this.amount = amount;
        this.invalidAmount = false;
        this.transactionsBeforePaying = new ArrayList<>();
        this.transactionsAfterPaying = new ArrayList<>();
    }

    /**
     * execute checks if the LBMS has the visitor, and if it does, it will pay the amount off
     */
    @Override
    public void execute() {
        if (this.lbms.hasVisitor(visitorId)) {
            this.invalidAmount = (this.visitor.getBalance() < this.amount);

            if (!invalidAmount) {
                for(Transaction t : this.visitor.getBooksLoaned()){
                    this.transactionsBeforePaying.add(new Transaction(t));
                }
                this.visitor.payFine(this.amount);
                this.lbms.payFine(this.amount);
                this.transactionsAfterPaying = this.visitor.getBooksLoaned();
            }
        } else {
            this.invalidVisitorId = true;
        }
    }

    /**
     * response is based on what execute performed.
     *
     * @return - String response on what execute perfomed.
     */
    @Override
    public String response() {
        double balance = this.visitor.getBalance();

        if (this.invalidVisitorId) {
            return "pay,invalid-visitor-id;";
        } else if (this.invalidAmount) {
            return "pay,invalid-amount," + Double.toString(this.amount) + "," + Double.toString(balance) + ";";
        } else {
            return "pay,success," + Double.toString(balance) + ";";
        }
    }

    @Override
    public boolean undo() {
        if(this.invalidAmount || this.invalidVisitorId){
            return false;
        }else{
            this.visitor.setBooksLoaned(this.transactionsBeforePaying);
            this.lbms.payFine(-1 * this.amount);
            return true;
        }
    }

    @Override
    public boolean redo() {
        if(this.invalidAmount || this.invalidVisitorId) {
            return false;
        }else {
            this.visitor.setBooksLoaned(this.transactionsAfterPaying);
            return true;
        }
    }
}
