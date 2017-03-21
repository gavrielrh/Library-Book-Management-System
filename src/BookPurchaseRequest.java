/**
 * Filename: BookPurchaseRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * BookPurchaseRequest is a concreteCommand for the LBMS.
 * Upon invoking this request, LBMS purchases the requested book from the "BookStore"
 */

/* imports */
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class BookPurchaseRequest implements Request {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* all of the required information to PurchaseBook(s) */
    private int quantity;
    private ArrayList<String> isbns;

    /**
     * Constructor for the concreteCommand.
     * @param lbms - the LBMS itself.
     * @param quantity - the number of books to purchase.
     * @param isbns - the ArrayList<String> of book isbn numbers to purchase
     */
    public BookPurchaseRequest(LBMS lbms, int quantity, ArrayList<String> isbns){
        this.lbms = lbms;
        this.quantity = quantity;
        this.isbns = isbns;
    }

    @Override
    public void execute(){
        for(String isbn : this.isbns){
            Book book = lbms.getBookFromStore(isbn);
            Book purchasedBook = new Book(book);
            purchasedBook.setTotalCopies(this.quantity);
            this.lbms.addBook(purchasedBook);
        }
    }

    @Override
    public String response(){
        String response = "buy,succcess," + Integer.toString(this.isbns.size());
        if(this.isbns.size() >= 0) {
            //get all book information
            for (String isbn : this.isbns){
                response += "\n";
                Book book = this.lbms.getBook(isbn);
                response += book.getIsbn();
                response += ",";
                response += book.getTitle();
                response += ",";
                response += " {";
                //potential multiple authors
                for(String author : book.getAuthors()){
                    response += author;
                    response += ",";
                }
                //remove the last ,
                response = response.substring(0, response.length() - 1);
                response += "},";
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                response += formatter.format(book.getPublishedDate());
                response += ",";
                response += Integer.toString(this.quantity);
            }
        }
        return response;
    }
}
