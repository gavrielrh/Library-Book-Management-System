import java.util.*;

public class LBMS {
   private Date time;
   private HashMap<Integer, Book> books;
   private Set<Visit> visits;
   //private ArrayList<Visit> visits;
   private HashMap<String, Visitor> visitors;

   //GETTERS AND SETTERS
   public Date getTime() {
      return time;
   }

   public void setTime(Date time) {
      this.time = time;
   }

   public HashMap<Integer, Book> getBooks() {
      return books;
   }

   public void addBooks(Book book) {
      this.books.put(Integer.parseInt(book.getIsbn()), book);
   }
   /*
   public ArrayList<Visit> getVisits() {
      return visits;
   }

   public void addVisits(Visit visit) {
      this.visits.add(visit);
   }

   public ArrayList<Visitor> getVisitors() {
      return visitors;
   }

   public void addVisitors(Visitor visitor) {
      this.visitors.add(visitor);
   }
   */

   public void startup(){

   }

   public void shutdown(){

   }

   public ArrayList<String> getVisitorIds(){
      ArrayList<String> visitorIds = new ArrayList<String>();
      Set<String> visitorNumIds = this.visitors.keySet();
      for(String v : visitorNumIds){
         visitorIds.add(v);
      }
      return visitorIds;
   }

   public HashMap<String, Visitor> getVisitors(){
      return this.visitors;
   }
   public String generateVisitorReport(){
      return null;
   }

   public String gernerateBookReport(){
      return null;
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
}
