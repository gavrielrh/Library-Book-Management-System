/*
 * File: GoogleBooksAPI.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Util for reading books from the google books API (https://developers.google.com/books/).
 */

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class GoogleBooksAPI {
    private Set<Book> books;
    private String title;
    private String authors;
    private String isbn;
    private String publisher;

    /**
     * Getter for books set
     * @return set of Books
     */
    public Set<Book> getBooks() {
        return this.books;
    }

    /**
     * Constructor for the API. All fields are assumed to be present ("*" for unused fields)
     * @param title the title of the book
     * @param authors the authors of the book
     * @param isbn the isbn of the book
     * @param publisher the publisher of the book
     */
    public GoogleBooksAPI(String title, Set<String> authors, String isbn, String publisher){
        this.title = title.equals("*") ? "*" : "intitle:" + title;

        if(authors.size() > 1) {
            for(String author : authors) {
                this.authors += "+inauthor:" + author;
            }
        } else {
            this.authors = "*";
        }

        this.isbn = isbn.equals("*") ? "*" : "+isbn:" + isbn;

        this.publisher = publisher.equals("*") ? "*" : "+inpublisher:" + publisher;

        this.books = new HashSet<>();
    }

    /**
     * Initializes set of Books based on GET request.
     * @throws IOException if request failed
     */
    public void readBooksFromAPI() throws IOException {
        String url = "https://www.googleapis.com/books/v1/volumes?q=";
        url += this.isbn.equals("*") ? "" : this.isbn;
        url += this.title.equals("*") ? "" : this.title;
        url += this.publisher.equals("*") ? "" : this.publisher;
        url += this.authors.equals("*") ? "" : this.authors;

        if(this.isbn.equals("*") && this.title.equals("*") && this.publisher.equals("*") && this.authors.equals("*")) {
            url += "*";
        }

        url += "&filter=paid-ebooks";
        url += "&maxResults=40";

        url = url.replaceAll("\\s", "%20");

        // Create a URL and open a connection
        URL googleBooksUrl = new URL(url);

        HttpURLConnection con = (HttpURLConnection) googleBooksUrl.openConnection();

        // Set the HTTP Request type method to GET (Default: GET)
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        // Created a BufferedReader to read the contents of the request.
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        JsonReader reader = new JsonReader(in);
        reader.setLenient(true);

        JsonParser parser = new JsonParser();

        JsonArray items = parser.parse(reader)
                .getAsJsonObject().get("items")
                .getAsJsonArray();

        // MAKE SURE TO CLOSE YOUR CONNECTION!
        in.close();

        for(JsonElement item : items) {
            JsonObject itemObject = item.getAsJsonObject();
            JsonObject volumeInfo = itemObject.get("volumeInfo").getAsJsonObject();

            String title = volumeInfo.get("title").getAsString();
            List<String> authors = new ArrayList<>();
            String publisher;
            String isbn;
            int pageCount;
            Date publishedDate;
            JsonArray industryIdentifiers;
            JsonObject saleInfoObject = itemObject.get("saleInfo").getAsJsonObject();

            if(!saleInfoObject.get("country").getAsString().equals("US")
                    || !saleInfoObject.get("saleability").getAsString().startsWith("FOR_SALE")) {
                continue;
            }

            if(volumeInfo.get("authors") != null && volumeInfo.get("publisher") != null
                    && volumeInfo.get("pageCount") != null && volumeInfo.get("industryIdentifiers") != null
                    && volumeInfo.get("industryIdentifiers").getAsJsonArray().size() >= 2
                    && volumeInfo.get("publishedDate") != null) {

                JsonArray authorsArray = volumeInfo.get("authors").getAsJsonArray();

                for(int i = 0; i < authorsArray.size(); i++) {
                    authors.add(authorsArray.get(i).toString());
                }

                publisher = volumeInfo.get("publisher").getAsString();

                industryIdentifiers = volumeInfo.get("industryIdentifiers").getAsJsonArray();

                pageCount = volumeInfo.get("pageCount").getAsInt();

                isbn = industryIdentifiers.get(1).getAsJsonObject().get("identifier").getAsString();

                publishedDate = Parser.parseDate(volumeInfo.get("publishedDate").getAsString());

                String[] authArray = new String[authors.size()];
                authArray = authors.toArray(authArray);

                Book book = new Book(isbn, title, authArray, publisher, publishedDate, pageCount);
                books.add(book);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String testTitle = "*";
        String testIsbn = "*";
        String testPublisher = "*";

        HashSet<String> testAuthors = new HashSet<>();
        testAuthors.add("*");

        GoogleBooksAPI api = new GoogleBooksAPI(testTitle, testAuthors, testIsbn, testPublisher);
        api.readBooksFromAPI();
    }
}