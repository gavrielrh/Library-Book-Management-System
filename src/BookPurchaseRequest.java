/**
 * Filename: BookPurchaseRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * BookPurchaseRequest is a concreteCommand for the LBMS.
 * Upon invoking this request, LBMS purchases the requested book from the "BookStore"
 */

/* imports */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class BookPurchaseRequest implements Request {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* all of the required information to PurchaseBook(s) */
    private int quantity;
    private ArrayList<Integer> ids;

    /**
     * Constructor for the concreteCommand.
     * @param lbms - the LBMS itself.
     * @param quantity - the number of books to purchase.
     * @param ids - the ArrayList<String> of book isbn numbers to purchase
     */
    public BookPurchaseRequest(LBMS lbms, int quantity, ArrayList<Integer> ids){
        this.lbms = lbms;
        this.quantity = quantity;
        this.ids = ids;
    }

    @Override
    public void execute(){
        if(quantity > 0) {
            for (Integer id : this.ids) {
                Book book = lbms.getBookFromQueryId(id);
                if (book != null) {
                    Book purchasedBook = new Book(book);
                    purchasedBook.setTotalCopies(this.quantity);
                    this.lbms.addBook(purchasedBook);
                }
            }
        }
    }

    @Override
    public String response(){
        String response = "buy,succcess," + Integer.toString(this.ids.size());
        if(this.ids.size() >= 0) {
            //get all book information
            for (Integer id : this.ids){
                response += "\n";
                Book book = this.lbms.getBookFromQueryId(id);
                response += book.toString() + ",";
                response += Integer.toString(this.quantity);
            }
        }
        return response;
    }


}
