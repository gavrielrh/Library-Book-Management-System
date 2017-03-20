import java.time.LocalDate;
import java.util.Date;

/**
 * Transaction takes in information about the visitor, book and date.
 * Records this information for later use when the book is returned
 * At the point of return, calculates and potential fine.
 */
public class Transaction {
    /* The LBMS itself so the current date can be checked */
    private LBMS lbms;

    private String isbn;
    private String visitorID;
    private Date dateBorrowed;
    private Date dueDate;

    /**
     * Constructor for Transaction
     * @param lbms  -  The system itself. This is so execute can call lbms commands.
     * @param isbn - 13 digit isbn (id)
     * @param visitorID  - the Id of the visitor beginning a request.
     * @param dateBorrowed  -  date the book was taken out from the library, LMBS
     * @param dueDate  -  date the book is due back the the LMBS, at most 7 days after the dateBorrowed
     */
    public Transaction(LBMS lbms, String isbn, String visitorID, Date dateBorrowed, Date dueDate) {
        this.lbms = lbms;
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

    /**
     * Calculates the total fine for an overdue Transaction
     * First overdue day: $10.00
     * Each following week: +$2.00
     *
     * @return total fine (capped at $30.00)
     */
    public Double calculateFine(){
        double fine = 0.0;

        if (lbms.getTime().getTime() < dueDate.getTime()) {
            return fine;
        } else {
            fine += 10;
        }

        double msOverdue = lbms.getTime().getTime() - dueDate.getTime();
        int weeksOverdue = (int) msOverdue / (7 * 24 * 60 * 60 * 1000);

        if (weeksOverdue > 1) {
            fine += (weeksOverdue - 1) * 2;
        }

        return fine < 30 ? fine : 30.0;
    }
}
