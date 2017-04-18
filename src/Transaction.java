/**
 * Filename: Transaction.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * @author - Gavriel Rachael-Homann, gxr2329@rit.edu
 * @author - Lucas Campbell, lxc7058@rit.edu
 * Transaction takes in information about the visitor, book and date.
 * Records this information for later use when the book is returned
 * When prompted, calculates any potential fines.
 */

/* imports */
import java.util.Date;

/**
 * Transaction takes in information about the visitor, book and date.
 * Records this information for later use when the book is returned
 * At the point of return, calculates and potential fine.
 */
public class Transaction implements java.io.Serializable{

    /* The LBMS itself so the current date can be checked */
    private LBMS lbms;

    /* the book that was checked out, when it was borrowed, and when it is due. */
    private Book bookType;
    private Date dateBorrowed;
    private Date dueDate;

    /* amountPaid towards the transaction is stored */
    private double amountPaid;

    /**
     * Constructor for Transaction
     * @param lbms  -  The system itself. This is so execute can call lbms commands.
     * @param bookType - the Book object itself that is being loaned out.
     * @param dateBorrowed  -  date the book was taken out from the library, LMBS
     * @param dueDate  -  date the book is due back the the LMBS, at most 7 days after the dateBorrowed
     */
    public Transaction(LBMS lbms, Book bookType, Date dateBorrowed, Date dueDate) {
        this.lbms = lbms;
        this.bookType = bookType;
        this.dateBorrowed = dateBorrowed;
        this.dueDate = dueDate;
        this.amountPaid = 0.0;
    }

    /***
     * getBookType gets the Book object associated with the loan
     * @return - the Book object that the transaction was for.
     */
    public Book getBookType(){
        return this.bookType;
    }

    /**
     * getDateBorrowed gets the Date object when the transaction started
     * @return - the Date object of when the transaction started.
     */
    public Date getDateBorrowed(){
        return dateBorrowed;
    }

    /**
     * Calculates the total fine for an overdue Transaction
     * First overdue day: $10.00
     * Each following week: +$2.00
     * Takes into account the amount paid for the fine.
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
        int weeksOverdue = (int) (msOverdue / (7 * 24 * 60 * 60 * 1000));

        if (weeksOverdue >= 1) {
            fine += (weeksOverdue) * 2;
        }

        double amountBeforePaying = fine < 30 ? fine : 30.0;
        return (amountBeforePaying - this.amountPaid);
    }

    /**
     * pay adds to the amount paid for the transaction.
     * @param amount - the amount to pay towards the transaction.
     */
    public void pay(double amount){
        this.amountPaid += amount;
    }
}
