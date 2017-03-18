import java.io.IOException;
import java.util.*;

public class LBMS {
   public static int MAX_BOOKS = 5;
   private Date startupDate;
   private Date time;
   private HashMap<String, Book> books;
   private Set<Visit> visits;
   private HashMap<String, Visitor> visitors;


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

   public Set<Visit> getVisits() {
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
      Scanner inputRequest = new Scanner(System.in);
      String requestLine = inputRequest.nextLine();
      String[] request = requestLine.split(",");
      /*
      * requests can start with:
      *
      *
      *
      *
      *
      *
      *
      *
      *
      *
       */
      switch (request[0]){
         case "hi":

      }


   }
}
