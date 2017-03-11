import java.util.ArrayList;
/**
 * Visitor represents a registered visitor in the LBMS.
 * Visitors have unique 10 digit ID's generated upon creation.
 */
public class Visitor {

    /* Fields for visitors */
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNum;
    private String uniqueId;

    /* ArrayList of Books that the Visitor has on loan */
    private ArrayList<Book> booksLoaned;

    /**
     * Constructor for Visitor
     * @param firstName - The firstName of visitor
     * @param lastName - The lastName of visitor
     * @param address - the address of visitor
     * @param phoneNum - the phoneNum of visitor
     * @param uniqueId - the uniqueId of visitor
     */
    public Visitor(String firstName, String lastName, String address, String phoneNum, String uniqueId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNum = phoneNum;
        this.uniqueId = uniqueId;
        this.booksLoaned = new ArrayList<Book>();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Visitor){
            Visitor otherVisitor = (Visitor)other;
            return( this.firstName.equals(otherVisitor.firstName) &&
                    this.lastName.equals(otherVisitor.lastName) &&
                    this.address.equals(otherVisitor.address) &&
                    this.phoneNum.equals(otherVisitor.phoneNum));
        }else{
            return false;
        }
    }

    /**
     * Method for getting the uniqueId associated with visitor.
     * @return visitor's uniqueId
     */
    public String getUniqueId(){
        return this.uniqueId;
    }
}
