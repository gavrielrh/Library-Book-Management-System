import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LBMS {
   private Date time;
   private HashMap<Integer, Book> books;
   //private ArrayList<Visit> visits;
   //private ArrayList<Visitor> visitors;

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

   public String generateVisitorReport(){
      return null;
   }

   public String gernerateBookReport(){
      return null;
   }
}