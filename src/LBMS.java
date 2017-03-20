/*
 * Filename: LBMS.java
 *
 * @authors - Gavriel Rachel-Homann, gxr2329@rit.edu
 * - Brendan Jones, bpj1651@rit.edu
 * - Lucas Campbell, lxc7058@rit.edu
 * - Junwen Mai, jxm7861@rit.edu
 * <p>
 * LBMS is a Library Book Management System consisting of only one library.
 * LBMS has many visitors, that can loan/return books.
 * Visitors may be fined if they have a late book.
 */

/* imports */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

/**
 * LBMS is the system itself. Only one system is meant to be made, following the "Singleton" Design Pattern
 * The main method creates that Singleton LBMS and calls it "self"
 */
public class LBMS {

    /* The maximum amount of books LBMS allows visitors to take out */
    public static int MAX_BOOKS = 5;

    /* LBMS Data */
    private Date time;
    private HashMap<String, Book> books;
    private HashMap<String, Book> bookStore;
    private ArrayList<Visit> visits;
    private HashMap<String, Visitor> visitors;
    private ArrayList<Transaction> transactions;

    /**
     * LBMS Constructor
     *
     *
     * @precondition -  timeToSet is stored in "data/SystemDate.txt"
     */
    public LBMS() {


        // LBMS stores its' books in a HashMap<String bookId (isbn), Book bookObjectItself>
        this.books = new HashMap<String, Book>();

        // LBMS tores its' purchasable books in a HashMap<String bookId (isbn), Book bookObjectItself>
        this.bookStore = new HashMap<String, Book>();

        // LBMS stores its' visitors in a HashMap<String visitorId, Visitor visitorObjectItself>
        this.visitors = new HashMap<String, Visitor>();

        this.visits = new ArrayList<Visit>();

        //Set LBMS to current Date if it's the first time running LBMS
        this.time = new Date();

       this.transactions = new ArrayList<Transaction>();
    }

    /**
     * getTime returns the LBMS time. Used throughout other classes to help create Visits, loans, etc.
     * @return - the Date object of the simiulated LBMS time.
     */
    public Date getTime() {
        return time;
    }

    /**
     * setTime sets LBMS time to the Date object
     * @param time - the Date object to set LBMS to.
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * getBooks returns the actual HashMap<String bookId (isbn), Book bookObjectItself> of LBMS books.
     * @return - return the HashMap of the LBMS books.
     */
    public HashMap<String, Book> getBooks() {
        return books;
    }

    /**
     * getBookStore returns the actual HashMap<String bookId (isbn), Book bookObjectItself> of LBMS bookStore.
     *
     * @return - return the HashMap of the LBMS bookStore.
     */
    public HashMap<String, Book> getBookStore() {
        return bookStore;
    }

    /**
     * addBook adds a Book to the LBMS itself
     * @param book - the Book object itself to add
     */
    public void addBook(Book book) {
        this.books.put(book.getIsbn(), book);
    }

    /**
     * getVisits returns all of the Visit objects in LBMS.
     * @return - ArrayList<Visit> of all the visits in LBMS.
     */
    public ArrayList<Visit> getVisits() {
        return visits;
    }

    /**
     * addVisit adds a visit to LBMS. This is used in generating reports.
     * @param visit - the Visit object itself to add
     */
    public void addVisits(Visit visit) {
        this.visits.add(visit);
    }

    /**
     * getVisitors returns all of the registered visitors in LBMS.
     * @return - HashMap<String visitorId, Visitor visitorObjectItself> for all the visitors registered in the LBMS.
     */
    public HashMap<String, Visitor> getVisitors() {
        return visitors;
    }

   public Book getBookFromStore(String isbn){
      return this.bookStore.get(isbn);
   }


    public void startup() {
       /* get all the LBMS book data */

       File bookFile = new File("data/SystemBooks.txt");
       //(String isbn, String title, String[] authors, String publisher, Date publishedDate, int pageCount, int total copies, int numCheckedOut);
       try {
          Scanner fileReaderBook = new Scanner(bookFile);
          while(fileReaderBook.hasNext()) {
             String line = fileReaderBook.nextLine();
             String[] lineVals = line.split(",");
             int numAuthors = lineVals.length - 7;
             String[] authors = new String[numAuthors];
             String isbn = lineVals[0];
             String title = lineVals[1];
             String publisher = lineVals[2];
             long publishTime = Long.parseLong(lineVals[3]);
             int pageCount = Integer.parseInt(lineVals[4]);
             int totalCopies = Integer.parseInt(lineVals[5]);
             int numCheckedOut = Integer.parseInt(lineVals[6]);
             for(int i = 0; i < numAuthors; i++){
                authors[i] = lineVals[i+7];
             }
             Date publishedDate = new Date(publishTime);
             Book book = new Book(isbn, title, authors, publisher, publishedDate, pageCount, totalCopies, numCheckedOut);
             this.addBook(book);
          }

       } catch (IOException e) {
          e.printStackTrace();
       }
       //isbn,title,publisher,longDatePublished, intPageCount, author1, author2, ..authorn
        /* get the LBMS date from the stored file */
       File dateFile = new File("data/SystemDate.txt");

       // initialize to 0, if no date is saved, it stays as 0.
       long timeToSet = 0;
       try {
          Scanner fileReaderDate = new Scanner(dateFile);
          if (fileReaderDate.hasNext()) {
             timeToSet = fileReaderDate.nextLong();
          } else {
             timeToSet = 0;
          }
       } catch (IOException e) {
          e.printStackTrace();
       }

       boolean firstTime = (timeToSet == 0);

       if(!firstTime) {
          this.time = new Date(timeToSet);
       }
        /* get the LBMS visitors from the stored file */
       File visitorFile = new File("data/SystemVisitors.txt");
       try {
          Scanner fileReaderVisitor = new Scanner(visitorFile);
          while(fileReaderVisitor.hasNext()) {
             String line = fileReaderVisitor.nextLine();
             String[] lineVals = line.split(",");
             ArrayList<Transaction> booksOnLoan = new ArrayList<Transaction>();
             //Brendan,Jones,4 Chapman Way, 978-325-0430, 220939402903,5,isbn,long,long,copyNum,amountPaid


             String firstName = lineVals[0];
             String lastName = lineVals[1];
             String address = lineVals[2];
             String phoneNum = lineVals[3];
             String uniqueId = lineVals[4];
             int numBooks = Integer.parseInt(lineVals[5]);
             if(lineVals.length >=7) {
                for (int i = 0; i < numBooks; i++) {
                   String bookId = lineVals[6 + (4 * i)];
                   long timeForBorrowed = Long.parseLong(lineVals[7 + (4 * i)]);
                   long timeForDue = Long.parseLong(lineVals[8 + (4 * i)]);
                   int copyNum = Integer.parseInt(lineVals[9 + (4 * i)]);
                   Book bookToAdd = this.getBook(bookId);
                   Date dateBorrowed = new Date(timeForBorrowed);
                   Date dateDue = new Date(timeForDue);
                   double amountPaid = Double.parseDouble(lineVals[10 + (4 * i)]);
                   Transaction transaction = new Transaction(this, bookToAdd, dateBorrowed, dateDue, copyNum, amountPaid);
                   this.addTransaction(transaction);
                   booksOnLoan.add(transaction);
                }
             }
               Visitor visitor = new Visitor(firstName, lastName, address, phoneNum, uniqueId, booksOnLoan);
               this.registerVisitor(visitor);
          }
       } catch (IOException e) {
          e.printStackTrace();
       }
       HashMap<String, Visitor> visitorsToSet = new HashMap<String, Visitor>();
    }

    /**
     * shutdown() saves all of the LBMS data to the appropriate .txt files
     */
    public void shutdown() {


      /* Save the Date */
        try {
            PrintWriter dateWriter = new PrintWriter("data/SystemDate.txt", "UTF-8");
            dateWriter.println(this.time.getTime());
            dateWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }

       /* save the Books */
       try {
          PrintWriter bookWriter = new PrintWriter("data/SystemBooks.txt", "UTF-8");
          for(Book b : this.getBooks().values()) {
             bookWriter.print(b.getIsbn());
             bookWriter.print(",");
             bookWriter.print(b.getTitle());
             bookWriter.print(",");
             bookWriter.print(b.getPublisher());
             bookWriter.print(",");
             bookWriter.print(b.getPublishedDate().getTime());
             bookWriter.print(",");
             bookWriter.print(b.getPageCount());
             bookWriter.print(",");
             bookWriter.print(b.getTotalCopies());
             bookWriter.print(",");
             bookWriter.print(b.getNumCheckedOut());
             bookWriter.print(",");
            for(int i = 0; i < b.getAuthors().length; i++){
               if(i == b.getAuthors().length - 1){
                  bookWriter.print(b.getAuthors()[i]);
               }else{
                  bookWriter.print(b.getAuthors()[i]);
                  bookWriter.print(",");
               }
            }
             bookWriter.println();
             bookWriter.close();
          }
       } catch (IOException e) {
          System.out.println(e);
       }

       /* Save the visitors */
       try {
          PrintWriter visitorWriter = new PrintWriter("data/SystemVisitors.txt", "UTF-8");
          for(Visitor visitor : this.visitors.values()){
             //Brendan,Jones,4 Chapman Way, 978-325-0430, 220939402903,5,isbn,long,long,copyNum,amountPaid
             visitorWriter.print(visitor.getFirstName());
             visitorWriter.print(",");
             visitorWriter.print(visitor.getLastName());
             visitorWriter.print(",");
             visitorWriter.print(visitor.getAddress());
             visitorWriter.print(",");
             visitorWriter.print(visitor.getPhoneNum());
             visitorWriter.print(",");
             visitorWriter.print(visitor.getUniqueId());
             visitorWriter.print(",");
             visitorWriter.print(visitor.getNumBooksCheckedOut());
             if(visitor.getNumBooksCheckedOut() > 0){
                visitorWriter.print(",");
                ArrayList<Transaction> booksLoaned = visitor.getBooksLoaned();
                for(int i = 0; i < visitor.getNumBooksCheckedOut(); i++){
                   visitorWriter.print(booksLoaned.get(i).getBookType().getIsbn());
                   visitorWriter.print(",");
                   visitorWriter.print(booksLoaned.get(i).getDateBorrowed().getTime());
                   visitorWriter.print(",");
                   visitorWriter.print(booksLoaned.get(i).getDueDate().getTime());
                   visitorWriter.print(",");
                   visitorWriter.print(booksLoaned.get(i).getCopyNum());
                   visitorWriter.print(",");
                   if(i == visitor.getNumBooksCheckedOut() - 1){
                      visitorWriter.print(booksLoaned.get(i).getAmountPaid());
                   }else{
                      visitorWriter.print(booksLoaned.get(i).getAmountPaid());
                      visitorWriter.print(",");
                   }
                }
             }
             visitorWriter.print("\n");
          }
          visitorWriter.close();
       } catch (IOException e) {
          System.out.println(e);
       }



        //TODO: More saving
    }

    /**
     * Creates initial library of books from txt file
     *
     * @param filename file to load books from
     * @throws IOException if file is not found / not readable
     */
    public void seedInitialLibrary(String filename) throws IOException {
        List<Book> booksList = (List<Book>) Parser.readBooksFromFile(filename);
        booksList.forEach(b -> this.bookStore.put(b.getIsbn(), b));
    }

    /**
     * NEEDS REVISING
     *
     * @param elapsedTime time to move forward
     * @return return string of time elapsed
     */
    public String advanceTime(Date elapsedTime) {
        //TODO: logic for advancing time. I don't think this shoiuld take in a Date object, but rather a "long".
        return null;
    }

    /**
     * getVisitorIds gets all of the Ids of visitors registered in LBMS. This is used for making sure an ID is unique.
     * @return - an ArrayList<String> of the visitorIds.
     */
    public ArrayList<String> getVisitorIds() {
        ArrayList<String> visitorIds = new ArrayList<String>();
        Set<String> visitorNumIds = this.visitors.keySet();
        for (String v : visitorNumIds) {
            visitorIds.add(v);
        }
        return visitorIds;
    }

    /**
     * generateVisitorReport creates and returns a report of all of the visitor data
     * @return - String representation of Visitor Report
     */
    public String generateVisitorReport() {
        //TODO: get the actual report itself.
        return null;
    }

    /**
     * generateBookReport creates and returns a report of all of the Book data.
     * @return - String representation of Book Report
     */
    public String generateBookReport() {
        String report = "";
        report += String.format("Number of books currently owned by LBMS: %d\n", this.books.size());
        report += String.format("Number of unique visitors: %d/\n", this.visitors.size());
        report += String.format("Average time per library visit: %f\n", Integer.toString(averageVisitDuration()));
        report += String.format("Books purchased: %d\n", null);
        report += String.format("Fines collected: %f\n", null);

        return report;
    }

    /**
     * averageVisitDuration gets the integer value of the average visit duration for all visits in LBMS.
     * @return - the int value of the average visit duration in LBMS.
     * //TODO: what does the int represent? miliseconds? minutes?
     */
    public int averageVisitDuration() {
        int sum = 0;
        for (Visit v : visits) {
            sum += v.getVisitDuration();
        }
        return sum / visits.size();
    }

    /**
     * hasVisitor checks if a given visitorId is registered in the LBMS. This is useful for checking duplicates.
     * @param visitorId - the String of the visitorId to check.
     * @return - boolean value if the LBMS has the visitorId registered in the system.
     */
    public boolean hasVisitor(String visitorId) {
        return this.visitors.containsKey(visitorId);
    }

    /**
     *
     * @param visitorId - the String id of the visitor that is requested
     * @return the visitor object itself matching the visitorId
     * @throws AssertionError if the lbms does not have the visitor in it's registered visitors
     */
    public Visitor getVisitor(String visitorId) {
        assert this.hasVisitor(visitorId);
        return this.visitors.get(visitorId);
    }

    /**
     * registerVisitor adds the visitor object to the LBMS HashMap of visitors
     * @param visitor - the visitor object to register to the LBMS
     */
    public void registerVisitor(Visitor visitor) {
        this.visitors.put(visitor.getUniqueId(), visitor);
    }

    /**
     * beginVisit adds the visit to the LBMS set of visits
     * @param visit - the visit to begin
     */
    public void beginVisit(Visit visit) {
        this.visits.add(visit);
    }

    /**
     * hasBook is used to see if the book, based on Id, is in the LBMS
     * @param bookId - the String Id value of the book to look up
     * @return - boolean value if the book is in the LBMS
     */
    public boolean hasBook(String bookId) {
        return (this.books.keySet().contains(bookId));
    }

    /**
     *
     * @param bookId - String value of the bookId to look up.
     * @return - the Book object from LBMS
     * @throws AssertionError - if the LBMS doesn't have the book.
     */
    public Book getBook(String bookId) {
        assert this.hasBook(bookId);
        return this.books.get(bookId);
    }

    /**
     * hasCopy checks if the LBMS has an available copy of the book requested by the String bookId (isbn)
     * @param bookId - String value of the book's id (isbn).
     * @return - boolean value if there is an avaiable copy of the book being requested.
     */
    public boolean hasCopy(String bookId) {
        if (this.hasBook(bookId)) {
            Book book = this.getBook(bookId);
            return book.isAvailable();
        } else {
            return false;
        }
    }

    /**
     * addTransaction adds the transaction object created when checking out a book
     * @param transaction - the transaction object itself
     */
   public void addTransaction(Transaction transaction){
      this.transactions.add(transaction);
   }

    /**
     * The main method in LBMS acts as the invoker in the Command Design Pattern.
     * Running the main method "starts" the system.
     * @param args - not used
     */
    public static void main(String[] args) {



        //"start up" the system by creating the LBMS with the previous data.
        LBMS self = new LBMS();
        //get all of the books in LBMS
        try {
            self.seedInitialLibrary("./data/books.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
       self.startup();


        //requests are from System.in
        Scanner inputRequest = new Scanner(System.in);

        while (true) {

            //get the request and split it by comma
            String requestLine = inputRequest.nextLine();
            String[] request = requestLine.split(",");

            // All requests must end with ";". Otherwise it's a PartialRequest.
            if (!(request[request.length - 1].endsWith(";"))) {
                Request partialRequest = new PartialRequest();
                partialRequest.execute();
                System.out.println(partialRequest.response());
            } else {
            /*
            * requests can be:
            *
            * <num> Name of Request - format,of,request;
            * <1> Register Visitor - register,first name,last name,address, phone-number;
            * <2> Begin Visit - arrive,visitor ID;
            * <3> End Visit - depart,visitor ID;
            * <4> Library Book Search - info,title,{authors},[isbn, [publisher,[sort order]]];
            * <5> Borrow Book - borrow,visitor ID,{id};
            * <6> Find Borrowed Books - borrowed,visitor ID;
            * <7> Return Book - return,visitor ID,id[,ids];
            * <8> Pay Fine - pay,visitor ID,amount;
            * <9> Book Store Search - search,title,[{authors},isbn[,publisher[,sort order]]];
            * <10> Book Purchase - buy,quantity,id[,ids];
            * <11> Advance Time - advance,number-of-days[,number-of-hours];
            * <12> Current Date & Time - datetime;
            * <13> Library Statistics Report - report[,days];
            *
            *
             */

                //Slice off the ; in the first word for easier cases in switch statement.
                String firstWord = request[0];
                if (firstWord.endsWith(";")) {
                    firstWord = firstWord.substring(0, firstWord.length() - 1);
                }
               String lastWord = request[request.length-1];
               if (lastWord.endsWith(";")) {
                  request[request.length-1] = lastWord.substring(0, lastWord.length() - 1);
               }

                switch (firstWord) {

                    //<1> Register Visitor - register,first name,last name,address, phone-number;
                    case "register": {

                        //Find all missing parameters.
                        if (request.length < 5) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            for (int i = request.length; i < 5; i++) {
                                if (i == 1) {
                                    missingParameters.add(missingParameters.size(), "first name");
                                } else if (i == 2) {
                                    missingParameters.add(missingParameters.size(), "last name");
                                } else if (i == 3) {
                                    missingParameters.add(missingParameters.size(), "address");
                                } else if (i == 4) {
                                    missingParameters.add(missingParameters.size(), "phone-number");
                                }
                            }
                            Request missingParam = new MissingParamsRequest("Register Visitor", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());

                            // Begin visit is valid, get all necessary data.
                        } else {
                            String firstName = request[1];
                            String lastName = request[2];
                            String address = request[3];
                            String phoneNum = request[4];
                            //slice off the ; ending the request.

                            Request register = new RegisterVisitorRequest(self, firstName, lastName, address, phoneNum);
                            register.execute();
                            System.out.println(register.response());
                        }
                    }
                    break;


                    //<2> Begin Visit - arrive,visitor ID;
                    case "arrive":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "visitor ID");
                            Request missingParam = new MissingParamsRequest("Begin Visit", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String visitorId = request[1];
                            Request beginVisitRequest = new BeginVisitRequest(self, visitorId);
                            beginVisitRequest.execute();
                            System.out.println(beginVisitRequest.response());
                        }
                        break;
                    //<3> End Visit - depart,visitor ID;
                    case "depart":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "visitor ID");
                            Request missingParam = new MissingParamsRequest("End Visit", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String visitorId = request[1];
                            Request endVisitRequest = new EndVisitRequest(self, visitorId);
                            endVisitRequest.execute();
                            System.out.println(endVisitRequest.response());
                        }
                        break;
                    //<4> Library Book Search - info,title,{authors},[isbn, [publisher,[sort order]]];
                    case "info":
                        if (request.length < 3) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            for (int i = request.length; i < 3; i++) {
                                if (i == 1) {
                                    missingParameters.add(missingParameters.size(), "title");
                                } else if (i == 2) {
                                    missingParameters.add(missingParameters.size(), "{authors}");
                                }
                            }
                            Request missingParam = new MissingParamsRequest("Library Book Search", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String isbn = null;
                            String sortOrder = null;
                            String publisher = null;
                            String authors = null;
                            int removeIdx = 0;
                            String title = requestLine.split("info,")[1].split(",[{\\*].+")[0];
                            removeIdx += "info".length() + title.length() + 2;

                            if (requestLine.charAt(removeIdx) == '*') {
                                removeIdx += 2;
                            } else {
                                String authorPart = requestLine.substring(removeIdx).split("}")[0];
                                authors = authorPart.substring(1);
                                removeIdx += authors.length() + 3;
                            }

                            String optionalPartsStr = requestLine.substring(removeIdx);
                            if (!optionalPartsStr.isEmpty()) {
                                optionalPartsStr = optionalPartsStr.substring(0, optionalPartsStr.length() - 1);
                                System.out.println("optional parts: " + optionalPartsStr);

                                String[] optionalParts = optionalPartsStr.split(",");


                                if (optionalParts.length > 0) {
                                    isbn = optionalParts[0].equals("*") ? null : optionalParts[0];
                                    publisher = optionalParts.length > 1 ? optionalParts[1].substring(1, optionalParts[1].length()) : null;
                                    sortOrder = optionalParts.length > 2 ? optionalParts[2] : null;
                                }
                            }

                            Request libraryBookSearchRequest = new LibraryBookSearchRequest(self, title, authors, isbn, publisher, sortOrder);
                            libraryBookSearchRequest.execute();
                            System.out.println(libraryBookSearchRequest.response());
                        }
                        break;
                    //<5> Borrow Book - borrow,visitor ID,{id};
                    case "borrow":
                        if (request.length < 3) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            for (int i = request.length; i < 3; i++) {
                                if (i == 1) {
                                    missingParameters.add(missingParameters.size(), "visitor ID");
                                } else if (i == 2) {
                                    missingParameters.add(missingParameters.size(), "{id}");
                                }
                            }
                            Request missingParam = new MissingParamsRequest("Borrow Book", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            ArrayList<String> bookIds = new ArrayList<String>();
                            String visitorId = request[1];
                            for (int i = 2; i < request.length; i++) {
                                bookIds.add(request[i]);
                            }
                            Request borrowBookRequest = new BorrowBookRequest(self, visitorId, bookIds);
                            borrowBookRequest.execute();
                            System.out.println(borrowBookRequest.response());
                        }
                        break;
                    //<6> Find Borrowed Books - borrowed,visitor ID;
                    case "borrowed":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "visitor ID");
                            Request missingParam = new MissingParamsRequest("Find Borrowed Books", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String visitorId = request[2];
                            Request findBorrowedBooks = new FindBorrowedBooksRequest(self, visitorId);
                            findBorrowedBooks.execute();
                            System.out.println(findBorrowedBooks.response());
                        }
                        break;
                    //<7> Return Book - return,visitor ID,id[,ids];
                    case "return":
                        if (request.length < 3) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            for (int i = request.length; i < 3; i++) {
                                if (i == 1) {
                                    missingParameters.add(missingParameters.size(), "visitor ID");
                                } else if (i == 2) {
                                    missingParameters.add(missingParameters.size(), "id");
                                }
                            }
                            Request missingParam = new MissingParamsRequest("Return Book", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        }
                        //<8> Pay Fine - pay,visitor ID,amount;
                    case "pay":
                        if (request.length < 3) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            for (int i = request.length; i < 3; i++) {
                                if (i == 1) {
                                    missingParameters.add(missingParameters.size(), "visitor ID");
                                } else if (i == 2) {
                                    missingParameters.add(missingParameters.size(), "amount");
                                }
                            }
                            Request missingParam = new MissingParamsRequest("Pay Fine", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        }
                        break;
                    //<9> Book Store Search - search,title,[{authors},isbn[,publisher[,sort order]]];
                    case "search":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "title");
                            Request missingParam = new MissingParamsRequest("Book Store Search", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String isbn = null;
                            String sortOrder = null;
                            String publisher = null;
                            String authors = null;
                            String title = null;

                            requestLine = requestLine.substring("search,".length(), requestLine.length() - 1);

                            if (requestLine.startsWith("*")) {
                                requestLine = requestLine.substring(1);
                            } else {
                                requestLine = requestLine.substring(1);
                                title = requestLine.substring(0, requestLine.indexOf("\""));
                            }
                            requestLine = requestLine.substring(title != null ? title.length() + 1 : 0, requestLine.length());
                            if (!requestLine.isEmpty()) {
                                requestLine = requestLine.substring(1);

                                if (requestLine.startsWith("{")) {
                                    authors = requestLine.substring(1, requestLine.indexOf("}"));
                                    requestLine = requestLine.substring(authors.length() + 2);
                                } else {
                                    requestLine = requestLine.substring(1);
                                }
                                if (!requestLine.isEmpty()) {
                                    String[] optionalParts = requestLine.split(",");
                                    isbn = optionalParts[1].equals("*") ? null : optionalParts[1];
                                    publisher = optionalParts.length > 2 ? optionalParts[2].substring(1, optionalParts[2].length()) : null;
                                    sortOrder = optionalParts.length > 3 ? optionalParts[3] : null;
                                }
                            }

                            Request bookStoreSearchRequest = new BookStoreSearchRequest(self, title, authors, isbn, publisher, sortOrder);
                            bookStoreSearchRequest.execute();
                            System.out.println(bookStoreSearchRequest.response());
                        }
                        break;
                    //<10> Book Purchase - buy,quantity,id[,ids];
                    case "buy":
                        if (request.length < 3) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            for (int i = request.length; i < 3; i++) {
                                if (i == 1) {
                                    missingParameters.add(missingParameters.size(), "quantity");
                                } else if (i == 2) {
                                    missingParameters.add(missingParameters.size(), "id");
                                }
                            }
                            Request missingParam = new MissingParamsRequest("Book Purchase", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        }else{
                           String quantityVal = request[1];
                           int quantity = Integer.parseInt(quantityVal);
                           ArrayList<String> isbns = new ArrayList<String>();
                           for(int i = 2; i < request.length; i++){
                              isbns.add(request[i]);
                           }
                           Request bookPurchaseRequest = new BookPurchaseRequest(self, quantity, isbns);
                           bookPurchaseRequest.execute();
                           System.out.println(bookPurchaseRequest.response());
                        }
                        break;
                    //<11> Advance Time - advance,number-of-days[,number-of-hours];
                    case "advance":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "number-of-days");
                            Request missingParam = new MissingParamsRequest("Advance Time", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        }
                        break;
                    //<12> Current Date & Time - datetime;
                    case "datetime":
                        // No possible potential missing parameter requests.
                        break;
                    //<13> Library Statistics Report - report[,days];
                    case "report":
                        // No possible potential missing parameter requests.
                        break;


                    //TODO: Replace this with the actual prompt for shutting down the system.
                    case "quit":
                        self.shutdown();
                        break;
                }
            }
        }
    }
}
