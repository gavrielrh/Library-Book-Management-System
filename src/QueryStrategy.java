/*
 * File: QueryStrategy.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Provides sorting functionality for book queries using the Strategy design pattern.
 */

import java.util.Comparator;

/**
 * Enumerable for choosing sorting method. Can be called using QueryStrategy.INSTANCE.<Comparator Name>
 */
public enum QueryStrategy {

    /* Allows you to access a static "instance" of the class */
    INSTANCE;

    /**
     * Comparator for sorting books by number of copies.
     */
    public final Comparator queryByNumberOfCopiesFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            return book1.getTotalCopies() - book2.getTotalCopies();
        }
    };

    /**
     * Comparator for sorting books by publication date.
     */
    public final Comparator queryByPublicationDateFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            return book1.getPublishedDate().compareTo(book2.getPublishedDate());
        }
    };

    /**
     * Comparator for sorting books by title.
     */
    public final Comparator queryByTitleFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            return book1.getTitle().compareTo(book2.getTitle());
        }
    };

    /**
     * Comparator for sorting books by availability.
     */
    public final Comparator queryByAvailabilityFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            if (book1.isAvailable() && !book2.isAvailable()) {
                return 1;
            } else if (!book1.isAvailable() && book2.isAvailable()) {
                return -1;
            }

            return 0;
        }
    };
}
