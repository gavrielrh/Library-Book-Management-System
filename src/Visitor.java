import java.util.ArrayList;
/**
 * Visitor represents a registered visitor in the LBMS.
 * Visitors have unique 10 digit ID's generated upon creation.
 */
public class Visitor {

    /* Fields for a Visitor */
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNum;
    private String uniqueId;
    private boolean isVisiting;

    /* ArrayList of Books that the Visitor has on loan */
    private ArrayList<Transaction> booksLoaned;

    /* If the visitor is visiting, keep that Visit object as the current visit */
    private Visit currentVisit;

    /**
     * Constructor for Visitor
     * @param firstName - The firstName of visitor
     * @param lastName - The lastName of visitor
     * @param address - the address of visitor
     * @param phoneNum - the phoneNum of visitor
     * @param uniqueId - the uniqueId of visitor
     */
    public Visitor(String firstName, String lastName, String address, String phoneNum, String uniqueId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNum = phoneNum;
        this.uniqueId = uniqueId;
        this.booksLoaned = new ArrayList<Transaction>();
        this.isVisiting = false;
        this.currentVisit = null;
    }

    public Visitor(String firstName, String lastName, String address, String phoneNum, String uniqueId, ArrayList<Transaction> booksOnLoan){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNum = phoneNum;
        this.uniqueId = uniqueId;
        this.booksLoaned = booksOnLoan;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Visitor){
            Visitor otherVisitor = (Visitor)other;
            return( this.firstName.equals(otherVisitor.firstName) &&
                    this.lastName.equals(otherVisitor.lastName) &&
                    this.address.equals(otherVisitor.address) &&
                    this.phoneNum.equals(otherVisitor.phoneNum));
        }else{
            return false;
        }
    }

    /**
     * Method for getting the uniqueId associated with visitor.
     * @return visitor's uniqueId
     */
    public String getUniqueId(){
        return this.uniqueId;
    }

    /**
     * Method for knowing if the visitor is in the Library or not.
     * @return boolean value of if the visitor is visiting the library.
     */
    public boolean isVisiting(){
        return this.isVisiting;
    }

    /**
     * startVisit sets the visitors isVisiting to be true.
     * This is used in BeginVisit and helps check for errors
     */
    public void startVisit(){
        this.isVisiting = true;
    }

    /**
     * endVisit sets the visitors isVisiting to be false.
     * This is used in EndVisit and helps check errors
     */
    public void endVisit() {
        this.isVisiting = false;
        this.currentVisit = null;
    }

    /**
     * Gets the current visit that a visitor is currently doing.
     * @return The current visit that the visitor currently is doing.
     * @throws AssertionError if the visitor isn't visiting
     */
    public Visit getCurrentVisit(){
        assert this.isVisiting;
        return this.currentVisit;
    }

    /**
     * Sets the current visit to the visitor.
     * @param visit - the visit object itself to attach to visitor
     * @throws AssertionError if th visitor isn't visiting
     */
    public void setCurrentVisit(Visit visit){
        this.currentVisit = visit;
    }

    /**
     * Returns the number of books the visitor has out on loan.
     * This is used to ensure the visitor doesn't exceed the max number of books allowed.
     * @return int value of the number of books ths visitor has.
     */
    public int getNumBooksCheckedOut(){
        return this.booksLoaned.size();
    }

    /**
     * Returns whether or not the visitor has any fines
     * @return boolean value for whether or not the visitor has fines
     */
    public boolean hasFines(){
        double totalFines = 0.0;
        for(Transaction t: this.booksLoaned){
            totalFines += t.calculateFine();
        }
        return (totalFines > 0.0);
    }

    public ArrayList<Transaction> getBooksLoaned(){
        return this.booksLoaned;
    }

    public void checkOutBook(Transaction transaction){
        this.booksLoaned.add(transaction);
    }
}
