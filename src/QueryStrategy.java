import java.util.Comparator;

public enum QueryStrategy {
    INSTANCE;

    QueryStrategy(){}

    public final Comparator queryByNumberOfCopiesFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            return book1.getTotalCopies() - book2.getTotalCopies();
        }
    };

    public final Comparator queryByPublicationDateFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            return book1.getPublishedDate().compareTo(book2.getPublishedDate());
        }
    };

    public final Comparator queryByTitleFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            return book1.getTitle().compareTo(book2.getTitle());
        }
    };

    public final Comparator queryByAvailabilityFunc = new Comparator<Book>() {

        @Override
        public int compare(Book book1, Book book2) {
            if (book1.isAvailable() && !book2.isAvailable()) {
                return 1;
            } else if(!book1.isAvailable() && book2.isAvailable()) {
                return -1;
            }
            return 0;
        }
    };
}
