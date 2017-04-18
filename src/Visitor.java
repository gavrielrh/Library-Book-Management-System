/**
 * Filename: Visitor.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * Purpose:
     * Visitor represents a visitor in the LBMS.
     * Visitors have:
     * - information associated with their account
     * - information about if they're visiting the library.
     * - information with their books checked out from the library
 *
 */

/* imports */
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Visitor represents a registered visitor in the LBMS.
 * Visitors have unique 10 digit ID's generated upon creation.
 */
public class Visitor implements java.io.Serializable{

    /* Fields for a Visitor */
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNum;
    private String uniqueId;

    /* borrowedBooksQuery used to return books */
    private HashMap<Integer, Transaction> borrowedBooksQuery;

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
        this.booksLoaned = new ArrayList<>();
        this.currentVisit = null;
        this.borrowedBooksQuery = new HashMap<>();
    }

    /**
     * a visitor is considered equal to another visitor if:
     * - they have the same firstname, lastname, address, and phone number
     */
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
        return this.currentVisit != null;
    }

    /**
     * endVisit sets the visitors isVisiting to be false.
     * This is used in EndVisit and helps check errors
     */
    public void endVisit() {
        this.currentVisit = null;
    }

    /**
     * Gets the current visit that a visitor is currently doing.
     * @return The current visit that the visitor currently is doing.
     * @throws AssertionError if the visitor isn't visiting
     */
    public Visit getCurrentVisit(){
        return this.currentVisit;
    }

    /**
     * Sets the current visit to the visitor.
     * @param visit - the visit object itself to attach to visitor
     */
    public void setCurrentVisit(Visit visit){
        this.currentVisit = visit;
    }

    /**
     * getBooksLoaned returns all transactions associated with the visitor
     * @return - ArrayList of Transactions the visitor has on loan.
     */
    public ArrayList<Transaction> getBooksLoaned(){
        return this.booksLoaned;
    }

    /**
     * checkOutBook is a method to add a transaction to the visitor. This is to track what books
     * a visitor has out and when they are due.
     * @param transaction - the transaction object that reprents the book being checked out.
     */
    public void checkOutBook(Transaction transaction){
        this.booksLoaned.add(transaction);
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
        return (this.getBalance() > 0.0);
    }

    /**
     * getBalance accumulates all the fines the visitor has on any books they have loaned out
     * and returns the sum, being what the visitor owes in total.
     * @return - the double amount the visitor owes the library
     */
    public double getBalance(){
        double totalFines = 0.0;
        for(Transaction t: this.booksLoaned){
            totalFines += t.calculateFine();
        }
        return totalFines;
    }

    /**
     * payFine is method called when a visitor wants to pay towards their balance with the library.
     * To pay the fine, the method checks all books on loan the visitor has and pays as much as possible
     * (with no order).
     * @param amount - the amount to pay for one or more books.
     */
    public void payFine(double amount){
        double amountLeft = amount;
        for(Transaction t : this.booksLoaned){
            if (amountLeft > 0.0) {
                double fineLeftOnBook = t.calculateFine();
                if (fineLeftOnBook > amountLeft) {
                    t.pay(amountLeft);
                    amountLeft = 0.0;
                } else {
                    t.pay(fineLeftOnBook);
                    amountLeft -= fineLeftOnBook;
                }
            }
        }
    }

    /**
     * setBorrowedBooksQuery is a method that is called when the LBMS queries for
     * the borrowed books from the visitor.
     * This query is used later when returning books as visitors return books
     * based on the Integer Key - ID of the query.
     * @param borrowedBooksQuery - a HashMap of <Integer - queryId, Transaction - each book checked out>
     */
    public void setBorrowedBooksQuery(HashMap<Integer, Transaction> borrowedBooksQuery){
        this.borrowedBooksQuery = borrowedBooksQuery;
    }

    /**
     * getTransaction is a method used to get a transaction from the borrowed query id. This is used
     * for returning books.
     * @param queryId - the id from the query for the transaction
     * @return - the Transaction object representing the book to return
     */
    public Transaction getTransactionFromQuery(int queryId){
        return this.borrowedBooksQuery.get(queryId);
    }

    /**
     * wasQueried checks the borrowed book's query to see if an id is there. This is used to validate
     * a return books request.
     * @param idToCheck - the id to check
     * @return - boolean, true if id was in borrowed query, false otherwise
     */
    public boolean wasQueried(int idToCheck){
        return this.borrowedBooksQuery.keySet().contains(idToCheck);
    }

    /**
     * setBooksLoaned is a method used to help "remove" the transaction when returning a book
     * @param transactions - the new ArrayList without the returned book in it.
     */
    public void setBooksLoaned(ArrayList<Transaction> transactions) {
        this.booksLoaned = transactions;
    }

    public void removeTransaction(Transaction t){
        this.booksLoaned.remove(t);
    }

}
