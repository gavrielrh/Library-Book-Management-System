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
        Collections.addAll(this.authors, authors.split(","));
        this.searchResults = new HashSet<>();
    }

    @Override
    public void execute() {
        HashSet<Book> byIsbn = new HashSet<>();
        HashSet<Book> byTitle = new HashSet<>();
        HashSet<Book> byPublisher = new HashSet<>();
        HashSet<Book> byAuthors = new HashSet<>();
        HashSet<Book> unfilteredResults = new HashSet<>();

        if (!this.isbn.equals("*")) {
            byIsbn.add(this.lbms.getBooks().get(this.isbn));
        }
        if (!this.title.equals("*")) {
            byTitle.addAll(
                    this.lbms.getBooks().entrySet().stream()
                            .filter(entry -> this.title.equals(entry.getValue().getTitle()))
                            .map(Map.Entry::getValue).collect(Collectors.toList()));
        }
        if (!this.publisher.equals("*")) {
            byPublisher.addAll(
                    this.lbms.getBooks().entrySet().stream()
                            .filter(entry -> this.publisher.equals(entry.getValue().getPublisher()))
                            .map(Map.Entry::getValue).collect(Collectors.toList()));
        }
        if (!this.authors.contains("*")) {
            byAuthors.addAll(
                    this.lbms.getBooks().entrySet().stream()
                            .filter(entry -> Arrays.asList(entry.getValue().getAuthors()).containsAll(this.authors))
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

    @Override
    public String response() {
        String message = "info,";

        List<Book> sortedBooks = new ArrayList<>(searchResults);
        switch (this.sortOrder) {
            case "title":
                Collections.sort(sortedBooks, QueryStrategy.INSTANCE.queryByTitleFunc);
                break;
            case "publish-date":
                Collections.sort(sortedBooks, QueryStrategy.INSTANCE.queryByPublicationDateFunc);
                break;
            case "book-status":
                Collections.sort(sortedBooks, QueryStrategy.INSTANCE.queryByAvailabilityFunc);
                break;
            default:
                return "info,invalid-sort-order";
        }

        message += searchResults.size();

        for (Book book : sortedBooks) {
            message += "<nl>" + book.getIsbn() + ",";
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

        return message;
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
