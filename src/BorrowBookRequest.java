import java.util.ArrayList;

/**
 * BorrowBookRequest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command borrows all books in the List of bookIds assuming no errors.
 * Errors are checked and reported in response.
 */
public class BorrowBookRequest implements Request {


    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* All of the required information to Borrow Books */
    private String visitorId;
    private ArrayList<String> bookIds;
    private Visitor visitor;
    private int numBooksRequested;

    /* boolean information associated with the ConcreteCommand to help response */
    private boolean invalidBookId;
    private boolean exceedBookLimit;
    private boolean invalidVisitorId;
    private boolean visitorHasFines;


    /**
     * Constructor for the the BorrowBookRequest. Initially no errors are caught.
     * @param lbms - The system itself. This is so execute can call lbms commands.
     * @param visitorId - the Id of the visitor borrowing books.
     * @param bookIds - a List of bookIds that are being checked out.
     */
    public BorrowBookRequest(LBMS lbms, String visitorId, ArrayList<String> bookIds){
        this.visitorId = visitorId;
        this.bookIds = bookIds;
        this.lbms = lbms;
        this.numBooksRequested = bookIds.size();
        this.invalidBookId = false;
        this.exceedBookLimit = false;
        this.invalidVisitorId = false;
        this.visitorHasFines = false;
        this.visitor = null;
    }

    /**
     * execute checks for any possible errors including:
     *      - the visitor isn't registered
     *      - the visitor has fines associated with their account
     *      - the bookId isn't valid/in LBMS/nor available
     *      - the borrow request doesn't exceed the maximum number of books allowed on loan
     *
     * assuming no errors, the LBMS creates a transaction object and loans the book to the visitor
     */
    @Override
    public void execute() {
        if (this.validBookIds()) {
            if (this.lbms.hasVisitor(this.visitorId)) {
                this.visitor = this.lbms.getVisitor(this.visitorId);
                if ((this.visitor.getNumBooksCheckedOut() + this.numBooksRequested) > LBMS.MAX_BOOKS) {
                    this.exceedBookLimit = true;
                }else{
                    if(this.visitor.hasFines()){
                        this.visitorHasFines = true;
                    }else{
                        this.checkOutBooks();
                    }
                }

            } else {

                this.invalidVisitorId = true;
            }
        }else{
            this.invalidBookId = true;
        }
    }

    /**
     * A helper method to see if all of the book Ids are valid and available within LBMS
     * @return - boolean value if all of the requested books are available in LBMS
     */
    private boolean validBookIds(){
        boolean valid = true;
        for(String bookId : this.bookIds){
            if(!(this.lbms.hasBook(bookId))){
                valid = false;
            }else{
                valid = this.lbms.hasCopy(bookId);
            }
        }
        return valid;
    }

    @Override
    public String response(){
        return null;
    }

    /**
     * A helper method that is called in execute() assuming no errors.
     * This is called to do the logic of actually checking out the books
     */
    private void checkOutBooks(){
        for(String bookId: this.bookIds){
            //TODO: Checkout each book
        }
    }
}
