import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * FindBorrowedBooksRequest represents a ConcreteCommand within the Command Design Pattern.
 * Executing the command registers the visitor within the LBMS.
 */
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
        this.invalidId = false;
        this.visitor = null;
        this.numBorrowed = 0;
    }

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

    @Override
    public String response(){
        String response = "borrowed,";
        response += Integer.toString(this.numBorrowed);
        response += ",";
        for(Transaction transaction : this.booksCheckedOut){
            response += "\n";
            response += Integer.toString(transaction.getCopyNum());
            response += ",";
            response += transaction.getBookType().getIsbn();
            response += ",";
            response += transaction.getBookType().getTitle();
            Date borrowDate = transaction.getDateBorrowed();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            return formatter.format(borrowDate);
        }
        response += ";";
        return response;
    }
}
