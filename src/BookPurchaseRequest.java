import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by brendanjones44 on 3/20/17.
 */
public class BookPurchaseRequest implements Request {

    private int quantity;
    private LBMS lbms;
    private ArrayList<String> isbns;

    public BookPurchaseRequest(LBMS lbms, int quantity, ArrayList<String> isbns){
        this.lbms = lbms;
        this.quantity = quantity;
        this.isbns = isbns;
    }

    public void execute(){
        for(String isbn : this.isbns){
            Book book = lbms.getBookFromStore(isbn);
            Book purchasedBook = new Book(book);
            purchasedBook.setTotalCopies(this.quantity);
            this.lbms.addBook(purchasedBook);
        }
    }

    public String response(){
        String response = "buy,succcess," + Integer.toString(this.isbns.size());
        if(this.isbns.size() >= 0) {
            for (String isbn : this.isbns){
                response += "\n";
                Book book = this.lbms.getBook(isbn);
                response += book.getIsbn();
                response += ",";
                response += book.getTitle();
                response += ",";
                response += " {";
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
