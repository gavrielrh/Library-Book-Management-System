/*
 * File: BookStoreSearchRequest.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * ConcreteCommand for searching for purchasable books matching given parameters.
 */

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BookStoreSearchRequest represents a ConcreteCommand within the Command Design pattern.
 * Executing the command searches for books available for purchase within the LBMS.
 * A "*" may be specified in place of any search parameter; any such parameter
 * should be ignored for the purpose of query matching.
 */
public class BookStoreSearchRequest implements Request {

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
     * Constructor for the BookStoreSearchRequest
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
    public BookStoreSearchRequest(LBMS lbms, String title, String authors,
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
        HashSet<Book> byIsbn = new HashSet<>();
        HashSet<Book> byTitle = new HashSet<>();
        HashSet<Book> byPublisher = new HashSet<>();
        HashSet<Book> byAuthors = new HashSet<>();
        HashSet<Book> unfilteredResults = new HashSet<>();

        if (this.isbn != null) {
            byIsbn.add(this.lbms.getBookStore().get(this.isbn));
        }
        if (this.title != null) {
            byTitle.addAll(
                    this.lbms.getBookStore().entrySet().stream()
                            .filter(entry -> this.title.equals(entry.getValue().getTitle()))
                            .map(Map.Entry::getValue).collect(Collectors.toList()));
        }
        if (this.publisher != null) {
            byPublisher.addAll(
                    this.lbms.getBookStore().entrySet().stream()
                            .filter(entry -> this.publisher.equals(entry.getValue().getPublisher()))
                            .map(Map.Entry::getValue).collect(Collectors.toList()));
        }
        if (this.authors != null) {
            byAuthors.addAll(this.lbms.getBookStore().entrySet().stream()
                    .filter(entry -> !Collections.disjoint(new ArrayList<>(Arrays.asList(entry.getValue().getAuthors())), this.authors))
                    .map(Map.Entry::getValue).collect(Collectors.toList()));
        }

        unfilteredResults.addAll(byIsbn);
        unfilteredResults.addAll(byTitle);
        unfilteredResults.addAll(byPublisher);
        unfilteredResults.addAll(byAuthors);

        for (Book book : unfilteredResults) {
            if (byIsbn.size() > 0) {
                if (!byIsbn.contains(book)) {
                    break;
                }
            }
            if (byTitle.size() > 0) {
                if (!byTitle.contains(book)) {
                    break;
                }
            }
            if (byPublisher.size() > 0) {
                if (!byPublisher.contains(book)) {
                    break;
                }
            }
            if (byAuthors.size() > 0) {
                if (!byAuthors.contains(book)) {
                    break;
                }
            }
            searchResults.add(book);
        }
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
        String message = "search,";

        List<Book> sortedBooks = new ArrayList<>(searchResults);
        if (sortedBooks.isEmpty() || sortedBooks.get(0) == null) {
            return "search,0;";
        }

        if (this.sortOrder != null) {
            switch (this.sortOrder) {
                case "title":
                    Collections.sort(sortedBooks, QueryStrategy.INSTANCE.queryByTitleFunc);
                    break;
                case "publish-date":
                    Collections.sort(sortedBooks, Collections.reverseOrder(QueryStrategy.INSTANCE.queryByPublicationDateFunc));
                    break;
                default:
                    return "info,invalid-sort-order";
            }
        }

        message += searchResults.size();

        int id = 0;
        for (Book book : sortedBooks) {
            message += id + ",,";
            message += book.getIsbn() + ",";
            message += "\"" + book.getTitle() + "\",";
            message += "{";
            for (String author : book.getAuthors()) {
                message += author + ",";
            }
            message = message.substring(0, message.length() - 1);
            message += "},";
            message += getPublishedDate(book) + ",\n";
            id++;
        }

        return message + ";"; // 	info,title,{authors},[isbn, [publisher,[sort order]]];
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
