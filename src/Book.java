/*
 * File: Book.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Class for representing Books in the Library
 */

import java.util.Date;

/**
 * Books are unique and keep track of their copy and checked out count.
 * All books initially start out with 0 copies and 0 checked out.
 */
public class Book {
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private Date publishedDate;
    private int pageCount;
    private int totalCopies;
    private int numCheckedOut;

    /**
     * Constructor for Book
     *
     * @param isbn          13 digit isbn (id)
     * @param title         title
     * @param authors       list of author(s)
     * @param publisher     publisher
     * @param publishedDate published date (month/day/year)
     * @param pageCount     page count
     */
    public Book(String isbn, String title, String[] authors, String publisher, Date publishedDate, int pageCount) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;

        this.totalCopies = 0;
        this.numCheckedOut = 0;
    }

    /**
     * Gets the Book's isbn
     *
     * @return isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the Book's title
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the Book's authors
     * @return authors
     */
    public String[] getAuthors() {
        return authors;
    }

    /**
     * Gets the Book's publisher
     * @return publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Gets the Book's published date
     * @return published date
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * Gets the Book's page count
     * @return page count
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Gets the Book's number of total copies
     * @return total copies
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Gets the Book's number of copies available
     *
     * @return copies available
     */
    public int getCopiesAvailable() {
        return totalCopies - numCheckedOut;
    }

    /**
     * Sets the Book's number of total copies
     * @param totalCopies number of copies to set
     */
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    /**
     * Gets the Book's number of copies checked out
     * @return number checked out
     */
    public int getNumCheckedOut() {
        return numCheckedOut;
    }

    /**
     * Sets the Book's number of copies checked out
     * @param numCheckedOut number to check out
     */
    public void setNumCheckedOut(int numCheckedOut) {
        this.numCheckedOut = numCheckedOut;
    }

    /**
     * Gets whether the Book has any copies available
     * @return availability
     */
    public boolean isAvailable() {
        return totalCopies > numCheckedOut;
    }

    @Override
    public String toString(){
        return null;
    }
}
