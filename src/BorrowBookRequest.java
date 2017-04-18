/*
 * Filename: BorrowBookRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * BorrowBookRequest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command borrows all books in the List of bookIds assuming no errors.
 * Errors are checked and reported in response.
 */

/* imports */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class BorrowBookRequest implements Request, UndoableCommand {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* All of the required information to Borrow Books */
    private String visitorId;
    private ArrayList<String> bookIds;
    private Visitor visitor;
    private int numBooksRequested;

    private HashMap<String, Transaction> transactions;

    /* Dates for the borrowed books */
    private Date dateBorrowed;
    private Date dueDate;

    /* boolean information associated with the ConcreteCommand to help response */
    private boolean invalidBookId;
    private boolean exceedBookLimit;
    private boolean invalidVisitorId;
    private boolean visitorHasFines;


    /**
     * Constructor for the the BorrowBookRequest. Initially no errors are caught.
     *
     * @param lbms      - The system itself. This is so execute can call lbms commands.
     * @param visitorId - the Id of the visitor borrowing books.
     * @param bookIds   - a List of bookIds that are being checked out.
     */
    public BorrowBookRequest(LBMS lbms, String visitorId, ArrayList<String> bookIds) {
        this.transactions = new HashMap<>();
        this.visitorId = visitorId;
        this.bookIds = bookIds;
        this.lbms = lbms;
        this.numBooksRequested = bookIds.size();

        //initially no errors
        this.invalidBookId = false;
        this.exceedBookLimit = false;
        this.invalidVisitorId = false;
        this.visitorHasFines = false;

        //initially no visitor
        this.visitor = null;

        //initially no date to set for the borrowed books
        this.dateBorrowed = null;
        this.dueDate = null;
    }

    /**
     * execute checks for any possible errors including:
     * - the visitor isn't registered
     * - the visitor has fines associated with their account
     * - the bookId isn't valid/in LBMS/nor available
     * - the borrow request doesn't exceed the maximum number of books allowed on loan
     * <p>
     * assuming no errors, the LBMS creates a transaction object and loans the book to the visitor
     */
    @Override
    public void execute() {
        if (this.validBookIds()) {
            if (this.lbms.hasVisitor(this.visitorId)) {
                this.visitor = this.lbms.getVisitor(this.visitorId);

                if ((this.visitor.getNumBooksCheckedOut() + this.numBooksRequested) > lbms.MAX_BOOKS) {
                    this.exceedBookLimit = true;
                } else {
                    if (this.visitor.hasFines()) {
                        this.visitorHasFines = true;
                    } else {
                        this.dateBorrowed = lbms.getTime();

                        long timeForSevenDays = (long) (6.912e+8);
                        this.dueDate = new Date(this.dateBorrowed.getTime() + timeForSevenDays);

                        this.checkOutBooks();
                    }
                }
            } else {
                this.invalidVisitorId = true;
            }
        } else {
            this.invalidBookId = true;
        }
    }

    /**
     * A helper method to see if all of the book Ids are valid and available within LBMS
     *
     * @return - boolean value if all of the requested books are available in LBMS
     */
    private boolean validBookIds() {
        boolean valid = true;

        for (String bookId : this.bookIds) {
            if (!(this.lbms.hasBook(bookId))) {
                valid = false;
            } else {
                Book book = this.lbms.getBook(bookId);
                valid = book.isAvailable();
            }
        }

        return valid;
    }

    /**
     * A helper method that is called in execute() assuming no errors.
     * This is called to do the logic of actually checking out the books
     */
    private void checkOutBooks() {
        for (String bookId : this.bookIds) {
            Book book = lbms.getBook(bookId);
            book.checkOutBook();

            Transaction transaction = new Transaction(this.lbms, book, this.dateBorrowed, this.dueDate);
            this.transactions.put(bookId, transaction);
            this.visitor.checkOutBook(transaction);
            this.lbms.addTransaction(transaction);
        }
    }

    /**
     * response is formatted based on what errors execute had found.
     *
     * @return - String response of the borrow action.
     */
    @Override
    public String response() {
        String response = "borrow,";
        boolean success = this.success();

        if (success) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            response += formatter.format(this.dueDate);
        } else if (invalidVisitorId) {
            response += "invalid-visitor-id";
        } else if (invalidBookId) {
            response += "invalid-book-id,{";
            for (int i = 0; i < this.bookIds.size(); i++) {
                if (i == this.bookIds.size() - 1) {
                    response += this.bookIds.get(i) + "}";
                } else {
                    response += this.bookIds.get(i) + ",";
                }
            }
        } else if (exceedBookLimit) {
            response += "book-limit-exceeded";
        } else if (visitorHasFines) {
            response += "outstanding-fine," + Double.toString(this.visitor.getBalance());
        }

        response += ";";

        return response;
    }

    /**
     * success is a private helper method to check any errors caught in execute.
     *
     * @return - boolean. True if executing found no errors, false otherwise.
     */
    private boolean success() {
        return (!this.invalidVisitorId && !this.invalidBookId && !this.exceedBookLimit && !this.visitorHasFines);
    }

    /**
     * undo checks if books were taken out, and if so returns them.
     *
     * @return - boolean. True if undo returned books, false otherwise.
     */
    @Override
    public boolean undo() {
        if (this.success()) {
            for (String bookId : this.transactions.keySet()) {
                Book book = lbms.getBook(bookId);
                book.returnBook();

                Transaction transaction = this.transactions.get(bookId);
                this.visitor.removeTransaction(transaction);
                this.lbms.removeTransaction(transaction);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * redo checks if books were taken out, and if so, takes them out again.
     *
     * @return - boolean. True if redo took out the books, false otherwise.
     */
    @Override
    public boolean redo() {
        if (this.success()) {
            for (String bookId : this.transactions.keySet()) {
                Book book = lbms.getBook(bookId);
                book.checkOutBook();

                Transaction transaction = this.transactions.get(bookId);
                this.visitor.checkOutBook(transaction);
                this.lbms.addTransaction(transaction);
            }
            return true;
        } else {
            return false;
        }
    }
}
