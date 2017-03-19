import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LBMS {
   public static int MAX_BOOKS = 5;
   private Date startupDate;
   private Date time;
   private HashMap<String, Book> books;
   private ArrayList<Visit> visits;
   private HashMap<String, Visitor> visitors;

   public LBMS(){
      this.books = new HashMap<String, Book>();
      this.visitors = new HashMap<String, Visitor>();
      this.visits = new ArrayList<Visit>();
      //TODO: REPLACE! JUST HERE TO HELP!
      this.time = new Date();

   }
   //GETTERS AND SETTERS
   public Date getTime() {
      return time;
   }

   public void setTime(Date time) {
      this.time = time;
   }

   public HashMap<String, Book> getBooks() {
      return books;
   }

   public void addBooks(Book book) {
      this.books.put(book.getIsbn(), book);
   }

   public ArrayList<Visit> getVisits() {
      return visits;
   }

   public void addVisits(Visit visit) {
      this.visits.add(visit);
   }

   public HashMap<String, Visitor> getVisitors() {
      return visitors;
   }

   public void startup(){

   }

   public void shutdown(){

   }

   /**
    * Creates initial library of books from txt file
    * TODO decide whether initialization of books belongs in this function
    * @param filename file to load books from
    * @throws IOException if file is not found / not readable
    */
   public void seedInitialLibrary(String filename) throws IOException {
      List<Book> booksList = (List<Book>)Parser.readBooksFromFile(filename);
      books = new HashMap<>();
      booksList.forEach(b -> books.put(b.getIsbn(), b));
   }

   /**
    * NEEDS REVISING
    * @param elapsedTime time to move forward
    * @return return string of time elapsed
    */
   public String advanceTime(Date elapsedTime){
      this.time.after(elapsedTime);

      return elapsedTime.toString();
   }

   public ArrayList<String> getVisitorIds(){
      ArrayList<String> visitorIds = new ArrayList<String>();
      Set<String> visitorNumIds = this.visitors.keySet();
      for(String v : visitorNumIds){
         visitorIds.add(v);
      }
      return visitorIds;
   }


   public String generateVisitorReport(){
      return null;
   }

   public String generateBookReport(){
      String report = "";
      report += String.format("Number of books currently owned by LBMS: %d\n", this.books.size());
      report += String.format("Number of unique visitors: %d/\n", this.visitors.size());
      report += String.format("Average time per library visit: %f\n", null);
      report += String.format("Books purchased: %d\n", null);
      report += String.format("Fines collected: %f\n", null);

      return report;
   }


   public boolean hasVisitor(String visitorId){
      return this.visitors.containsKey(visitorId);
   }

   /**
    *
    * @param visitorId - the String id of the visitor that is requested
    * @return the visitor object itself matching the visitorId
    * @throws AssertionError if the lbms does not have the visitor in it's registered visitors
     */
   public Visitor getVisitor(String visitorId){
      assert this.hasVisitor(visitorId);
      return this.visitors.get(visitorId);
   }

    /**
     * registerVisitor adds the visitor object to the LBMS HashMap of visitors
     * @param visitor - the visitor object to register to the LBMS
     */
   public void registerVisitor(Visitor visitor){
      this.visitors.put(visitor.getUniqueId(), visitor);
   }

    /**
     * beginVisit adds the visit to the LBMS set of visits
     * @param visit - the visit to begin
     */
   public void beginVisit(Visit visit){
      this.visits.add(visit);
   }

    /**
     * hasBook is used to see if the book, based on Id, is in the LBMS
     * @param bookId - the String Id value of the book to look up
     * @return - boolean value if the book is in the LBMS
     */
   public boolean hasBook(String bookId){
      return (this.books.keySet().contains(bookId));
   }

    /**
     *
     * @param bookId - String value of the bookId to look up.
     * @return - the Book object from LBMS
     * @throws AssertionError - if the LBMS doesn't have the book.
     */
   public Book getBook(String bookId){
      assert this.hasBook(bookId);
      return this.books.get(bookId);
   }

    /**
     *
     * @param bookId -
     * @return
     */
   public boolean hasCopy(String bookId){
      if(this.hasBook(bookId)){
         Book book = this.getBook(bookId);
         return book.isAvailable();
      }else{
         return false;
      }
   }

   /**
    * The main method in LBMS acts as the invoker in the Command Design Pattern.
    * @param args - not used
     */
   public static void main(String[] args) {
      LBMS self = new LBMS();
      try {
         self.seedInitialLibrary("./data/books.txt");
      } catch (IOException e) {
         e.printStackTrace();
      }
      Scanner inputRequest = new Scanner(System.in);
      while(true) {
         String requestLine = inputRequest.nextLine();
         String[] request = requestLine.split(",");
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
            String firstWord = request[0];
            if(firstWord.endsWith(";")){
               firstWord = firstWord.substring(0,firstWord.length()-1);
            }
            switch (firstWord) {
               //<1> Register Visitor - register,first name,last name,address, phone-number;
               case "register": {
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
                  } else {
                        String firstName = request[1];
                        String lastName = request[2];
                        String address = request[3];
                        String phoneNum = request[4];
                        phoneNum = phoneNum.substring(0,phoneNum.length()-1);
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
                  }else{
                     String visitorId = request[1];
                     visitorId= visitorId.substring(0,visitorId.length()-1);
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
                  }else{
                     String visitorId = request[1];
                     visitorId= visitorId.substring(0,visitorId.length()-1);
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
                  }else{
                     String isbn = null;
                     String sortOrder = null;
                     String publisher = null;
                     String title = requestLine.split("info,")[1].split(",\\{}")[0];

                     String authors = requestLine.split(",\\{")[1].split("\\}")[0];

                     if (request.length > 3) {
                        String optionalPartsStr = requestLine.split("}")[1];
                        optionalPartsStr = optionalPartsStr.substring(1, optionalPartsStr.length() - 1);

                        String[] optionalParts = optionalPartsStr.split(",");

                        isbn = optionalParts[0].substring(1, optionalParts[0].length());
                        publisher = optionalParts.length > 1 ?
                                optionalParts[1].substring(2, optionalParts[1].length()) : null;
                        sortOrder = optionalParts.length > 2 ?
                                optionalParts[2].substring(1, optionalParts[2].length() - 3) : null;
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

            }
         }
      }
   }
}
