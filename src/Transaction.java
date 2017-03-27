import java.time.LocalDate;
import java.util.Date;

/**
 * Transaction takes in information about the visitor, book and date.
 * Records this information for later use when the book is returned
 * At the point of return, calculates and potential fine.
 */
public class Transaction implements java.io.Serializable{
    /* The LBMS itself so the current date can be checked */
    private LBMS lbms;

    private Book bookType;
    private Visitor visitor;
    private Date dateBorrowed;
    private Date dueDate;
    private int copyNum;

    private boolean isComplete;
    private double amountPaid;

    /**
     * Constructor for Transaction
     * @param lbms  -  The system itself. This is so execute can call lbms commands.
     * @param bookType - the Book object itself that is being loaned out.
     * @param dateBorrowed  -  date the book was taken out from the library, LMBS
     * @param dueDate  -  date the book is due back the the LMBS, at most 7 days after the dateBorrowed
     */
    public Transaction(LBMS lbms, Book bookType, Date dateBorrowed, Date dueDate, int copyNum) {
        this.lbms = lbms;
        this.bookType = bookType;
        this.dateBorrowed = dateBorrowed;
        this.dueDate = dueDate;
        this.copyNum = copyNum;
        this.amountPaid = 0.0;
        this.isComplete = false;
    }

    public Transaction(LBMS lbms, Book bookType, Date dateBorrowed, Date dueDate, int copyNum, double amountPaid) {
        this.lbms = lbms;
        this.bookType = bookType;
        this.dateBorrowed = dateBorrowed;
        this.dueDate = dueDate;
        this.copyNum = copyNum;
        this.amountPaid = amountPaid;
    }
    public int getCopyNum(){
        return this.copyNum;
    }

    public Book getBookType(){
        return this.bookType;
    }
    public Date getDateBorrowed(){
        return dateBorrowed;
    }

    public void complete(){
        this.isComplete = true;
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
        double fine = (-1 * this.amountPaid);

        //TODO: days
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

    public Double getAmountPaid(){
        return this.amountPaid;
    }
}
