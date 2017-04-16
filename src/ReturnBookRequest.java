import java.util.ArrayList;

/**
 * Created by brendanjones44 on 3/20/17.
 */
public class ReturnBookRequest implements Request {

    private LBMS lbms;
    private String visitorId;
    private ArrayList<Integer> bookIds;
    private ArrayList<Transaction> overDueTransactions;
    private String invalidIsbns;

    double overDueFine;
    boolean isOverdue;
    boolean invalidVisitorId;
    boolean invalidBookId;

    private Visitor visitor;

    public ReturnBookRequest(LBMS lbms, String visitorId, ArrayList<Integer> bookIds){
        this.lbms = lbms;
        this.visitorId = visitorId;
        this.bookIds = bookIds;
        this.visitor = this.lbms.getVisitor(this.visitorId);
        this.invalidIsbns = "";
    }

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

    private boolean validBookIds(){
        boolean valid = true;
        for(int bookId : this.bookIds) {
            if (!(this.visitor.wasQueried(bookId))){
                this.invalidIsbns += "," + Integer.toString(bookId);
                valid = false;
            }
        }
        return valid;
    }

    private void returnBooks(){
        for(int bookId: this.bookIds){
            Book bookToReturn = this.visitor.getTransactionFromQuery(bookId).getBookType();
            bookToReturn.returnBook();
        }
        for(Transaction transaction : this.visitor.getBooksLoaned()){
            if(this.isOverdue){
                if(transaction.calculateFine() > 0.0){
                    this.overDueTransactions.add(transaction);
                    this.overDueFine += transaction.calculateFine();
                }
            }
            transaction.complete();
            ArrayList<Transaction> newBooksLoaned = new ArrayList<>();
            for(Transaction t: this.visitor.getBooksLoaned()){
                if(t != transaction){
                    newBooksLoaned.add(t);
                }
            }
            this.visitor.setBooksLoaned(newBooksLoaned);
        }
    }
    public String response(){
        if(this.invalidBookId){
            return "return,invalid-book-id" + this.invalidIsbns + ";";
        }else if(this.invalidVisitorId){
            return "return,invalid-visitor-id;";
        }else if(this.isOverdue){
            String response =  "return,overdue,$" + Double.toString(this.overDueFine);
            for(Transaction overDueTransaction : overDueTransactions){
                response += "," + overDueTransaction.getBookType().getIsbn();
            }
            response += ";";
            return response;
        }else{
            return "return,success;";
        }
    }
}
