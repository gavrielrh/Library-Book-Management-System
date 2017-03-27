/*
 * Filename: LBMS.java
 *
 * @authors
 * - Gavriel Rachel-Homann, gxr2329@rit.edu
 * - Brendan Jones, bpj1651@rit.edu
 * - Lucas Campbell, lxc7058@rit.edu
 * - Junwen Mai, jxm7861@rit.edu
 * <p>
 * LBMS is a Library Book Management System consisting of only one library.
 * LBMS has many visitors, that can loan/return books.
 * Visitors may be fined if they have a late book.
 */

/* imports */

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * LBMS is the system itself. Only one system is meant to be made, following the "Singleton" Design Pattern
 * The main method creates that Singleton LBMS and calls it "self"
 */
public class LBMS implements java.io.Serializable {
    private static final long serialVersionUID = 8640384775929272738L;
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");


    /* The maximum amount of books LBMS allows visitors to take out */
    final int MAX_BOOKS = 5;

    /* The integer time that the library opens. 800 represents 8:00 */
    final int LIBRARY_OPEN_TIME = 800;

    /* The integer time that the library closes 1900 represents 19:00 or 7:00pm */
    final int LIBRARY_CLOSED_TIME = 1900;

    /* The maximum and minimum values LBMS allows time to be advanced by */
    final int MAX_ADVANCE_DAYS = 7;
    final int MIN_ADVANCE_DAYS = 0;

    final int MAX_ADVANCE_HOURS = 23;
    final int MIN_ADVANCE_HOURS = 0;

    /* The open and closing times of the LBMS */


    private double finesCollected;
    /* LBMS Data */
    private Date time;
    private HashMap<String, Book> books;
    private HashMap<Integer, Book> booksForPurchaseById;
    private HashMap<Integer, Book> booksForBorrowById;

    private HashMap<String, Book> bookStore;
    private ArrayList<Visit> visits;
    private HashMap<String, Visitor> visitors;
    private ArrayList<Transaction> transactions;

    public LBMS(LBMS otherLBMS) {
        this.finesCollected = otherLBMS.finesCollected;
        this.books = otherLBMS.books;
        this.bookStore = otherLBMS.bookStore;
        this.booksForPurchaseById = otherLBMS.booksForPurchaseById;
        this.time = otherLBMS.time;
        this.transactions = otherLBMS.transactions;
        this.visitors = otherLBMS.visitors;
        this.visits = otherLBMS.visits;
        this.transactions = otherLBMS.transactions;
    }

    /**
     * LBMS Constructor
     */
    public LBMS() {

        this.finesCollected = 0.0;
        // LBMS stores its' books in a HashMap<String bookId (isbn), Book bookObjectItself>
        this.books = new HashMap<String, Book>();

        // LBMS stores its' purchasable books in a HashMap<String bookId (isbn), Book bookObjectItself>
        this.bookStore = new HashMap<String, Book>();

        // LBMS stores its' list of latest queried book ids for purchase
        this.booksForPurchaseById = new HashMap<Integer, Book>();

        this.booksForBorrowById = new HashMap<Integer, Book>();

        // LBMS stores its' visitors in a HashMap<String visitorId, Visitor visitorObjectItself>
        this.visitors = new HashMap<String, Visitor>();

        this.visits = new ArrayList<Visit>();

        //Set LBMS to current Date if it's the first time running LBMS
        this.time = new Date();

        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * getTime returns the LBMS time. Used throughout other classes to help create Visits, loans, etc.
     *
     * @return - the Date object of the simiulated LBMS time.
     */
    public Date getTime() {
        return time;
    }

    /**
     * getBooks returns the actual HashMap<String bookId (isbn), Book bookObjectItself> of LBMS books.
     *
     * @return - return the HashMap of the LBMS books.
     */
    public HashMap<String, Book> getBooks() {
        return books;
    }

    /**
     * getBookStore returns the actual HashMap<String bookId (isbn), Book bookObjectItself> of LBMS bookStore.
     *
     * @return - return the HashMap of the LBMS bookStore.
     */
    public HashMap<String, Book> getBookStore() {
        return bookStore;
    }

    /**
     * addBook adds a Book to the LBMS itself
     *
     * @param book - the Book object itself to add
     */
    public void addBook(Book book) {
        if(this.books.containsKey(book.getIsbn())) {
            book.setTotalCopies(book.getTotalCopies() + 1);
        } else {
            this.books.put(book.getIsbn(), book);
        }
    }

    /**
     * getVisits returns all of the Visit objects in LBMS.
     *
     * @return - ArrayList<Visit> of all the visits in LBMS.
     */
    public ArrayList<Visit> getVisits() {
        return visits;
    }

    /**
     * addVisit adds a visit to LBMS. This is used in generating reports.
     *
     * @param visit - the Visit object itself to add
     */
    public void addVisits(Visit visit) {
        this.visits.add(visit);
    }

    /**
     * getVisitors returns all of the registered visitors in LBMS.
     *
     * @return - HashMap<String visitorId, Visitor visitorObjectItself> for all the visitors registered in the LBMS.
     */
    public HashMap<String, Visitor> getVisitors() {
        return visitors;
    }

    /**
     * setBooksForPurchaseById sets the internal list of queried books for purchase
     * @param booksForPurchaseById the new list of queried books
     */
    public void setBooksForPurchaseById(HashMap<Integer, Book> booksForPurchaseById) {
        this.booksForPurchaseById = booksForPurchaseById;
    }

    /**
     * getBooksFromQueryId returns the book specified by the purchase query id
     * @param id purchase query identification number
     * @return - Book the book if found, otherwise null
     */
    public Book getBookFromQueryId(int id) {
        return this.booksForPurchaseById.get(id);
    }

    public void setBooksForBorrowById(HashMap<Integer, Book> booksForBorrowById){
        this.booksForBorrowById = booksForBorrowById;
    }

    public Book getBookFromBorrowId(int id){
        return this.booksForBorrowById.get(id);
    }

    /**
     * Creates initial library of books from txt file
     *
     * @param filename file to load books from
     * @throws IOException if file is not found / not readable
     */
    public void seedInitialLibrary(String filename) throws IOException {
        List<Book> booksList = (List<Book>) Parser.readBooksFromFile(filename);
        booksList.forEach(b -> this.bookStore.put(b.getIsbn(), b));
    }

    /**
     * NEEDS REVISING
     *
     * @param elapsedTime time to move forward
     * @return return string of time elapsed
     */
    public String advanceTime(Date elapsedTime) {
        //TODO: logic for advancing time. I don't think this shoiuld take in a Date object, but rather a "long".
        return null;
    }

    /**
     * getVisitorIds gets all of the Ids of visitors registered in LBMS. This is used for making sure an ID is unique.
     *
     * @return - an ArrayList<String> of the visitorIds.
     */
    public ArrayList<String> getVisitorIds() {
        ArrayList<String> visitorIds = new ArrayList<String>();
        Set<String> visitorNumIds = this.visitors.keySet();
        for (String v : visitorNumIds) {
            visitorIds.add(v);
        }
        return visitorIds;
    }

    /**
     * generateVisitorReport creates and returns a report of all of the visitor data
     *
     * @return - String representation of Visitor Report
     */
    public String generateVisitorReport() {
        //TODO: get the actual report itself.
        return null;
    }

    /**
     *
     * @param timeClosed
     */
    public void close(Date timeClosed){
        for(Visitor v : this.visitors.values()){
            v.endVisit();
        }
        for(Visit v : this.visits){
            v.endVisit(timeClosed);
        }
    }

    /**
     * isOpen is used to determine whether or not the libary is open, based on the OPEN and CLOSE times
     * @return - the boolean value if the libarary is open or not
     */
    public boolean isOpen(){
        int currentTime = Integer.parseInt(String.format("%tH%tm", this.time, this.time));
        return (currentTime >= this.LIBRARY_OPEN_TIME && currentTime <= this.LIBRARY_CLOSED_TIME);
    }
    public double getFinesCollected() {
        return this.finesCollected;
    }

    public void payFine(double amount) {
        this.finesCollected += amount;
    }

    /**
     * averageVisitDuration gets the integer value of the average visit duration for all visits in LBMS.
     *
     * @return - the int value of the average visit duration in LBMS.
     * //TODO: what does the int represent? miliseconds? minutes?
     */
    public long averageVisitDuration() {
        long sum = 0;
        for (Visit v : visits) {
            if (v.isComplete()) {
                sum += v.getVisitDuration();
            }
        }
        return sum;
    }

    /**
     * hasVisitor checks if a given visitorId is registered in the LBMS. This is useful for checking duplicates.
     *
     * @param visitorId - the String of the visitorId to check.
     * @return - boolean value if the LBMS has the visitorId registered in the system.
     */
    public boolean hasVisitor(String visitorId) {
        return this.visitors.containsKey(visitorId);
    }

    /**
     *
     */
    public void setTime(long time) {
        this.time = new Date(time);
    }

    /**
     * @param visitorId - the String id of the visitor that is requested
     * @return the visitor object itself matching the visitorId
     * @throws AssertionError if the lbms does not have the visitor in it's registered visitors
     */
    public Visitor getVisitor(String visitorId) {
        assert this.hasVisitor(visitorId);
        return this.visitors.get(visitorId);
    }

    /**
     * registerVisitor adds the visitor object to the LBMS HashMap of visitors
     *
     * @param visitor - the visitor object to register to the LBMS
     */
    public void registerVisitor(Visitor visitor) {
        this.visitors.put(visitor.getUniqueId(), visitor);
    }

    /**
     * beginVisit adds the visit to the LBMS set of visits
     *
     * @param visit - the visit to begin
     */
    public void beginVisit(Visit visit) {
        this.visits.add(visit);
    }

    /**
     * hasBook is used to see if the book, based on Id, is in the LBMS
     *
     * @param bookId - the String Id value of the book to look up
     * @return - boolean value if the book is in the LBMS
     */
    public boolean hasBook(String bookId) {
        return (this.books.keySet().contains(bookId));
    }

    /**
     * @param bookId - String value of the bookId to look up.
     * @return - the Book object from LBMS
     * @throws AssertionError - if the LBMS doesn't have the book.
     */
    public Book getBook(String bookId) {
        assert this.hasBook(bookId);
        return this.books.get(bookId);
    }

    /**
     * hasCopy checks if the LBMS has an available copy of the book requested by the String bookId (isbn)
     *
     * @param bookId - String value of the book's id (isbn).
     * @return - boolean value if there is an avaiable copy of the book being requested.
     */
    public boolean hasCopy(String bookId) {
        if (this.hasBook(bookId)) {
            Book book = this.getBook(bookId);
            return book.isAvailable();
        } else {
            return false;
        }
    }

    /**
     * addTransaction adds the transaction object created when checking out a book
     *
     * @param transaction - the transaction object itself
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public int getNumMinWhenClosed(){
        int numMinInHours = 60 * (this.LIBRARY_CLOSED_TIME / 100);
        return (this.LIBRARY_CLOSED_TIME % 100) + numMinInHours;
    }

}
