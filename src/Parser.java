/*
 * Filename: Parser.java
 * @author - Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Provides text file parsing (specifically of books)
 * Assumes following format:
 * ISBN,"title",{comma-separated authors},"publisher",date,page count
 */

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides functions for parsing books from text files
 */
public class Parser {

    /**
     * Parses String in date format as Date object
     *
     * @param dateString date to parse
     * @return parsed date
     */
    public static Date parseDate(String dateString) {
        try {
            String format = "y-M-d";
            String[] splitDate = dateString.split("-");

            if (splitDate.length == 1) {
                format = "y";
            } else if (splitDate.length == 2) {
                format = "y-M";
            }

            return new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Reads book data from file into a Collection of Book(s)
     *
     * @param filepath file to read from
     * @return collection of books
     * @throws IOException if the specified file is not found
     */
    public static Collection<Book> readBooksFromFile(String filepath) throws IOException {
        File file = new File(filepath);
        assert file.isFile() : filepath;
        ArrayList<Book> books = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        Pattern pattern = Pattern.compile("(\\d+),\"([^\"]+)\",\\{([^}]+)\\},\"(.+?(?=\",))\",([\\d-]+),(\\d+)");

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                String isbn = matcher.group(1);
                String title = matcher.group(2);
                String[] authors = matcher.group(3).split(",");
                String publisher = matcher.group(4);
                Date publishedDate = parseDate(matcher.group(5));
                int pageCount = Integer.parseInt(matcher.group(6));

                books.add(new Book(isbn, title, authors, publisher, publishedDate, pageCount));
            }
        }

        return books;
    }
}
