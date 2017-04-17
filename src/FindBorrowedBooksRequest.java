/**
 * Filename: FindBorrowedBooksRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * FindBorrowedBooksRequest represents a ConcreteCommand within the Command Design Pattern.
 * Executing the command registers the visitor within the LBMS.
 */

/* imports */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FindBorrowedBooksRequest implements Request {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* all information needed for the request */
    private String visitorId;
    private Visitor visitor;
    private int numBorrowed;
    private ArrayList<Transaction> booksCheckedOut;

    /* boolean information associated with the ConcreteCommand to help response */
    private boolean invalidId;

    /**
     * Constructor for the FindBorrowedBookRequest
     * @param lbms - the system itself. This is so execute can call lbms commands.
     * @param visitorId - the String value of the visitor Id that has the books borrowed.
     */
    public FindBorrowedBooksRequest(LBMS lbms, String visitorId){
        this.lbms = lbms;
        this.visitorId = visitorId;

        //initially no error or visitor
        this.invalidId = false;
        this.visitor = null;

        //initially set counter of numBorrowed to 0
        this.numBorrowed = 0;
    }

    /* execute checks if the visitor is in LBMS,
     * and if so, finds the books the visitor has checked out.
     */
    @Override
    public void execute(){
        if(this.lbms.hasVisitor(this.visitorId)){
            this.visitor = this.lbms.getVisitor(this.visitorId);
            this.numBorrowed = visitor.getNumBooksCheckedOut();
            this.booksCheckedOut = visitor.getBooksLoaned();
        }else{
            this.invalidId = true;
        }
    }

    /* response returns the books borrowed by query (with id) if given a valid visitor id,
     * otherwise, it just reports the invalid visitor id
     */
    @Override
    public String response(){
        String response = "borrowed,";
        if(!this.invalidId) {
            response += Integer.toString(this.numBorrowed);
            response += ",";
            int queryId = 1;
            HashMap<Integer, Transaction> booksQueried = new HashMap<>();

            for (Transaction transaction : this.booksCheckedOut) {
                response += "\n";
                response += Integer.toString(queryId);
                response += ",";
                response += transaction.getBookType().getIsbn();
                response += ",";
                response += transaction.getBookType().getTitle();
                response += ",";
                Date borrowDate = transaction.getDateBorrowed();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                response += formatter.format(borrowDate);
                booksQueried.put(queryId, transaction);
                queryId++;
            }
            this.visitor.setBorrowedBooksQuery(booksQueried);
        }else{
            response += "invalid-vsitor-id";
        }
        response += ";";
        return response;
    }

}
