import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private String isbn;
    private String visitorID;
    private Date dateBorrowed;
    private Date dueDate;

    public Transaction(String isbn, String visitorID, Date dateBorrowed, Date dueDate) {
        this.isbn = isbn;
        this.visitorID = visitorID;
        this.dateBorrowed = dateBorrowed;
        this.dueDate = dueDate;
    }

    public String getIsbn(){
        return isbn;
    }

    public String getVisitorID(){
        return visitorID;
    }

    public Date getDateBorrowed(){
        return dateBorrowed;
    }

    public Date getDueDate(){
        return dueDate;
    }

    public Double calculateFine(){
        return null;
    }
}
