/*
 * File: LibraryBookSearchRequest.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * ConcreteCommand for searching for books matching given parameters.
 */

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LibraryBookSearchRequest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command searches for books owned by and available for borrowing within the LBMS.
 * A "*" may be specified in place of any search parameter; any such parameter
 * should be ignored for the purpose of query matching.
 */
public class LibraryBookSearchRequest implements Request {

    /* The LBMS itself so execute can call commands */
    private LBMS lbms;

    /* All the required information to search for a book */
    private String title;
    private Set<String> authors;
    private String isbn;
    private String publisher;
    private String sortOrder;

    /* The books matching the request */
    private Set<Book> searchResults;

    /**
     * Constructor for the LibraryBookSearchRequest
     *
     * @param lbms      the system itself. This is so execute can call lbms commands
     * @param title     the title of the book
     * @param authors   the comma-separated list of authors of the book
     * @param isbn      the 13-digit International Standard Book NUmber (ISBN) for the book
     * @param publisher the name of the book's publisher
     * @param sortOrder one of: title, publish-date, book-status Sorting of the title will be
     *                  alphanumerical from 0..1-A-Z, publish date will be newest first,
     *                  and book status will only show books with at least one copy available for check out.
     */
    public LibraryBookSearchRequest(LBMS lbms, String title, String authors,
                                    String isbn, String publisher, String sortOrder) {
        this.lbms = lbms;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.sortOrder = sortOrder;
        this.authors = new HashSet<>();
        if (authors != null) {
            Collections.addAll(this.authors, authors.split(","));
        }
        this.searchResults = new HashSet<>();
    }

    /**
     * Sets internal searchResults based on query results.
     */
    @Override
    public void execute() {
        this.searchResults.addAll(this.lbms.getBooks().values());
        /*
        this.searchResults.removeIf(book -> {
            if(this.isbn != null && !this.isbn.equals(book.getIsbn())) {
                return true;
            }
            if(this.title != null && !this.title.equals(book.getTitle())) {
                return true;
            }
            if(this.publisher != null && !this.publisher.equals(book.getPublisher())) {
                return true;
            }
            if(!this.authors.isEmpty() && Collections.disjoint(new ArrayList<>(Arrays.asList(book.getAuthors())), this.authors)) {
                return true;
            }
            return false;
        });
        */
    }

    /**
     * n is the number of books that will follow
     * id is the identifier for the book
     * If no books were found, n will be 0 and no books will follow in the string
     * <p>
     * If the specified sort order doesn't match one of the expected values
     * return info,invalid-sort-order;
     *
     * @return info, n, [<nl>,number of copies available,id, book]{0..n};
     */
    @Override
    public String response() {
        String message = "info,";

        List<Book> sortedBooks = new ArrayList<>(searchResults);
        if (sortedBooks.isEmpty() || sortedBooks.get(0) == null) {
            return "info,0;";
        }

        if (this.sortOrder != null) {
            switch (this.sortOrder) {
                case "title":
                    Collections.sort(sortedBooks, QueryStrategy.INSTANCE.queryByTitleFunc);
                    break;
                case "publish-date":
                    Collections.sort(sortedBooks, Collections.reverseOrder(QueryStrategy.INSTANCE.queryByPublicationDateFunc));
                    break;
                case "book-status":
                    Collections.sort(sortedBooks, QueryStrategy.INSTANCE.queryByAvailabilityFunc);
                    break;
                default:
                    return "info,invalid-sort-order";
            }
        }

        message += searchResults.size();

        for (Book book : sortedBooks) {
            message += ",\n," + book.getCopiesAvailable() + ",";
            message += book.getIsbn() + ",";
            message += "\"" + book.getTitle() + "\",";
            message += "{";
            for (String author : book.getAuthors()) {
                message += author + ",";
            }
            message = message.substring(0, message.length() - 1);
            message += "},";
            message += book.getPublisher() + ",";
            message += getPublishedDate(book) + ",";
            message += book.getPageCount();
        }

        return message + ";";
    }

    /**
     * getPublishedDate is a helper method to get the string of the visit Date
     *
     * @return - String representaton of the visit Date
     * @throws AssertionError if the visit wasn't valid, meaning it didn't have a date
     */
    private String getPublishedDate(Book book) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(book.getPublishedDate());
    }
}
