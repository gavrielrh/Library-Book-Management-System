/**
 * Filename: ReturnBookRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * ReturnBookRequest represents a ConcreteCommand in the Command pattern for the request subsystem.
 * Returning a book requries the visitorId and bookId(s) to return from the most recent query.
 *
 */

/* imports */
import java.util.ArrayList;

public class ReturnBookRequest implements Request {

    /* have the lbms be in the request for data/actions */
    private LBMS lbms;

    /* information needed for the request */
    private String visitorId;
    private ArrayList<Integer> bookIds;
    private String invalidIds;

    /* store information for response */
    private ArrayList<Transaction> overDueTransactions;
    double overDueFine;
    boolean isOverdue;
    boolean invalidVisitorId;
    boolean invalidBookId;

    /* visitor used for managing their loaned books */
    private Visitor visitor;

    /**
     * Constructor for ReturnBookRequest
     * @param lbms - the LBMS itself so the request can execute commands
     * @param visitorId - the visitorId that is making the request
     * @param bookIds - the ArrayList of bookIds from the most recent borrowed book query of books to retrun
     */
    public ReturnBookRequest(LBMS lbms, String visitorId, ArrayList<Integer> bookIds){
        this.lbms = lbms;
        this.visitorId = visitorId;
        this.bookIds = bookIds;
        this.visitor = this.lbms.getVisitor(this.visitorId);
        this.invalidIds = "";
        this.overDueTransactions = new ArrayList<>();
    }

    /**
     * execute checks for any potential errors, and returns books if no errors were found.
     * If there was an error, it updates the request, so the response can respond properly.
     */
    @Override
    public void execute(){
        if(validBookIds()) {
            if (this.lbms.hasVisitor(this.visitorId)) {
                this.visitor = this.lbms.getVisitor(this.visitorId);
                if(visitor.hasFines()){
                    this.isOverdue = true;
                    this.returnBooks();
                }else{
                    this.returnBooks();
                }
            }else{
                this.invalidVisitorId = true;
            }
        }else{
            this.invalidBookId = true;
        }
    }

    /**
     * validBookIds is a helper method used to check if the bookIds in the request are in the most recent query.
     * if an invalid book id is found, it is concatenated to the invalidId response String.
     * @return - the boolean for validBookIds, true if all Ids in request are valid, false otherwise.
     */
    private boolean validBookIds(){
        boolean valid = true;
        for(int bookId : this.bookIds) {
            if (!(this.visitor.wasQueried(bookId))){
                this.invalidIds += "," + Integer.toString(bookId);
                valid = false;
            }
        }
        return valid;
    }

    /**
     * returnBooks is a helper method to this request. It goes through all the the books queried for return and returns
     * them.
     */
    private void returnBooks(){

        //Update the Book itself (num copies)
        for(int bookId: this.bookIds){
            Book bookToReturn = this.visitor.getTransactionFromQuery(bookId).getBookType();
            bookToReturn.returnBook();
        }

        //Check all Books loaned out and get their fines if they have them.
        for(Transaction transaction : this.visitor.getBooksLoaned()){
            if(this.isOverdue){
                if(transaction.calculateFine() > 0.0){
                    //keep track of overdue transactions for response
                    this.overDueTransactions.add(transaction);
                    this.overDueFine += transaction.calculateFine();
                }
            }
            //"Remove" the transaction by cloning the ArrayList minus the transaction to remove.
            ArrayList<Transaction> newBooksLoaned = new ArrayList<>();
            for(Transaction t: this.visitor.getBooksLoaned()){
                if(t != transaction){
                    newBooksLoaned.add(t);
                }
            }
            this.visitor.setBooksLoaned(newBooksLoaned);
        }
    }

    /**
     * response is a String based response based on what errors were caught in execute.
     * @return - The response to the request being executed.
     */
    @Override
    public String response(){
        if(this.invalidBookId){
            return "return,invalid-book-id" + this.invalidIds + ";";
        }else if(this.invalidVisitorId){
            return "return,invalid-visitor-id;";
        }else if(this.isOverdue){
            String response =  "return,overdue,$" + Double.toString(this.overDueFine);
            for(Transaction overDueTransaction : this.overDueTransactions){
                response += "," + overDueTransaction.getBookType().getIsbn();
            }
            response += ";";
            return response;
        }else{
            return "return,success;";
        }
    }
}
