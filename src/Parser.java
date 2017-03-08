import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static void main(String[] args) {
        try {
            ArrayList<Book> books = (ArrayList<Book>)readBooksFromFile(new File("./data/books.txt"));
            Collections.sort(books, QueryStrategy.INSTANCE.queryByTitleFunc);
            for(Book b : books) {
                System.out.println(b.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Date parseDate(String dateString) {
        String[] formats = {"y", "M-d-y"};
        for (String format : formats) {
            try {
                return new SimpleDateFormat(format).parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Collection<Book> readBooksFromFile(File file) throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        Pattern pattern = Pattern.compile("(\\d+),\"([^\"]+)\",\\{([^}]+)\\},(.+?(?=,\\d)),([\\d-]+),(\\d+),(.+)");

        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);

            if(matcher.matches()) {
                String isbn = matcher.group(1);
                String title = matcher.group(2);
                String[] authors = matcher.group(3).split(",");
                String publisher = matcher.group(4);
                Date publishedDate = parseDate(matcher.group(5));
                int pageCount = Integer.parseInt(matcher.group(6));
                // System.out.println(matcher.group(7));   // book-status: ignored
                books.add(new Book(isbn, title, authors, publisher, publishedDate, pageCount));
            }
        }

        return books;
    }
}
