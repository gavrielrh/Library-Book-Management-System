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

    //TODO REMOVE THIS TEMP VAR
    private enum BOOKSERVICE {
        local,
        google
    }

    private BOOKSERVICE bookService;

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
        //TODO REMOVE THIS TEMP VAR
        this.bookService = BOOKSERVICE.local;
    }

    /**
     * Sets internal searchResults based on query results.
     */
    @Override
    public void execute() {
        //TODO REMOVE THIS TEMP VAR
        if(this.bookService == BOOKSERVICE.local) {
            this.searchResults.addAll(this.lbms.getBookStore().values());
            this.searchResults.removeIf(book -> {
                if (this.isbn != null && !this.isbn.equals(book.getIsbn())) {
                    return true;
                }
                if (this.title != null && !book.getTitle().toLowerCase().contains(this.title.toLowerCase())) {
                    return true;
                }
                if (this.publisher != null && !this.publisher.equals(book.getPublisher())) {
                    return true;
                }
                if (!this.authors.isEmpty() && Collections.disjoint(new ArrayList<>(Arrays.asList(book.getAuthors())), this.authors)) {
                    return true;
                }
                return false;
            });
        } else {
            String queryURL = "https://www.googleapis.com/books/v1/volumes?q=";
            /*
             *
             Books are only considered available for purchase via the LBMS
              - if the "saleability" is "FOR_SALE" and in the "country" is "US."
             Title
             Authors
             Publisher
             PublishedDate
             PageCount
             type = ISBN_12_Identifier
             Country
             Saleability
             */
            if(this.isbn != null) {
                queryURL += "+isbn:" + this.isbn;
            }
            if(this.title != null) {
                queryURL += "+intitle:" + this.title;
            }
            if(this.publisher != null) {
                queryURL += "+inpublisher:" + this.publisher;
            }
            if(!this.authors.isEmpty()) {
                for(String author : authors) {
                    queryURL += "+inauthor:" + author;
                }
            }


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

        message += searchResults.size() + ",\n";

        HashMap<Integer, Book> booksForPurchaseById = new HashMap<>();

        int id = 0;
        for (Book book : sortedBooks) {
            message += id + ",,";
            message += book.toString() + ",\n";
            booksForPurchaseById.put(id, book);
            id++;
        }

        lbms.setBooksForPurchaseById(booksForPurchaseById);

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
