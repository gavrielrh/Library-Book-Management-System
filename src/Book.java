import java.time.LocalDate;
import java.util.Date;

public class Book {
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private Date publishedDate;
    private int pageCount;
    private int totalCopies;
    private int numCheckedOut;


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

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getNumCheckedOut() {
        return numCheckedOut;
    }

    public void setNumCheckedOut(int numCheckedOut) {
        this.numCheckedOut = numCheckedOut;
    }

    public boolean isAvailable() {
        return totalCopies - numCheckedOut > 0;
    }
}
