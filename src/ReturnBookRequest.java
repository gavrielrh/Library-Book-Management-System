/*
 * Filename: ReturnBookRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * ReturnBookRequest represents a ConcreteCommand in the Command pattern for the request subsystem.
 * Returning a book requires the visitorId and bookId(s) to return from the most recent query.
 */

/* imports */

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReturnBookRequest implements Request, UndoableCommand{

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

    /* store information for potential undo-redo */
    private ArrayList<Transaction> transactionsBeforeReturn;

    ArrayList<Transaction> newBooksLoaned;


    /* visitor used for managing their loaned books */
    private Visitor visitor;

    /**
     * Constructor for ReturnBookRequest
     *
     * @param lbms      - the LBMS itself so the request can execute commands
     * @param visitorId - the visitorId that is making the request
     * @param bookIds   - the ArrayList of bookIds from the most recent borrowed book query of books to retrun
     */
    public ReturnBookRequest(LBMS lbms, String visitorId, ArrayList<Integer> bookIds) {
        this.lbms = lbms;
        this.visitorId = visitorId;
        this.bookIds = bookIds;
        this.visitor = this.lbms.getVisitor(this.visitorId);
        this.invalidIds = "";
        this.overDueTransactions = new ArrayList<>();
        this.transactionsBeforeReturn = new ArrayList<>();
        this.newBooksLoaned = new ArrayList<>();
    }

    /**
     * execute checks for any potential errors, and returns books if no errors were found.
     * If there was an error, it updates the request, so the response can respond properly.
     */
    @Override
    public void execute() {
        this.visitor.getBooksLoaned();
        if (validBookIds()) {
            if (this.lbms.hasVisitor(this.visitorId)) {
                this.visitor = this.lbms.getVisitor(this.visitorId);

                if (visitor.hasFines()) {
                    this.isOverdue = true;
                    this.returnBooks();
                } else {
                    this.returnBooks();
                }
            } else {
                this.invalidVisitorId = true;
            }
        } else {
            this.invalidBookId = true;
        }
    }

    /**
     * validBookIds is a helper method used to check if the bookIds in the request are in the most recent query.
     * if an invalid book id is found, it is concatenated to the invalidId response String.
     *
     * @return - the boolean for validBookIds, true if all Ids in request are valid, false otherwise.
     */
    private boolean validBookIds() {
        boolean valid = true;

        for (int bookId : this.bookIds) {
            if (!(this.visitor.wasQueried(bookId))) {
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
    private void returnBooks() {

        //Update the Book itself (num copies)
        for (int bookId : this.bookIds) {
            Book bookToReturn = this.visitor.getTransactionFromQuery(bookId).getBookType();

            bookToReturn.returnBook();
        }

        //Check all Books loaned out and get their fines if they have them.
        for (Transaction transaction : this.visitor.getBooksLoaned()) {
            this.transactionsBeforeReturn.add(transaction);
            if (this.isOverdue) {
                if (transaction.calculateFine() > 0.0) {
                    //keep track of overdue transactions for response
                    this.overDueTransactions.add(transaction);

                    this.overDueFine += transaction.calculateFine();
                }
            }
            //"Remove" the transaction by cloning the ArrayList minus the transaction to remove.

            for (Transaction t : this.visitor.getBooksLoaned()) {
                if (t != transaction) {

                    this.newBooksLoaned.add(t);
                }
            }

            this.visitor.setBooksLoaned(newBooksLoaned);
        }
    }

    /**
     * response is a String based response based on what errors were caught in execute.
     *
     * @return - The response to the request being executed.
     */
    @Override
    public String response() {
        if (this.invalidBookId) {
            return "return,invalid-book-id" + this.invalidIds + ";";
        } else if (this.invalidVisitorId) {
            return "return,invalid-visitor-id;";
        } else if (this.isOverdue) {
            String response = "return,overdue,$" + Double.toString(this.overDueFine);
            response += ",";

            for (Transaction overDueTransaction : this.overDueTransactions) {
                response += "," + overDueTransaction.getBookType().getIsbn();
            }

            response += ";";

            return response;
        } else {
            return "return,success;";
        }
    }

    @Override
    public boolean undo(){
        if(!this.invalidVisitorId && !this.invalidBookId){
            //Update the Book itself (num copies)
            for (int bookId : this.bookIds) {
                Book bookToReCheckOut = this.visitor.getTransactionFromQuery(bookId).getBookType();

                bookToReCheckOut.checkOutBook();
            }

            //Add all transactions back to Visitor
            this.visitor.setBooksLoaned(this.transactionsBeforeReturn);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean redo(){
        if(!this.invalidVisitorId && !this.invalidBookId) {
            //set the transactions back to what it was after returning those books
            this.visitor.setBooksLoaned(this.newBooksLoaned);
            //Update the Book itself (num copies)
            for (int bookId : this.bookIds) {
                Book bookToReCheckOut = this.visitor.getTransactionFromQuery(bookId).getBookType();
                bookToReCheckOut.returnBook();
            }
            return true;
        }else{
            return false;
        }
    }
}
