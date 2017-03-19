import java.util.ArrayList;
import java.util.Random;

/**
 * RegisterVisitorRequest represents a ConcreteCommand within the Command Design Pattern.
 * Executing the command registers the visitor within the LBMS.
 */
public class RegisterVisitorRequest implements Request {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    /* All the visitor information required to register */
    private String visitorFirstName;
    private String visitorLastName;
    private String visitorAddress;
    private String visitorPhoneNum;

    /* Unique Id generated for the visitor */
    private String uniqueId;

    /* boolean information associated with the ConcreteCommand to help response */

    public boolean registeredSuccessfully;
    public boolean duplicateRegistration;

    /**
     * Constructor for the RegisterVisitorRequest
     * @param lbms - the system itself. This is so execute can call lbms commands
     * @param firstName - The firstName of visitor
     * @param lastName - The lastName of visitor
     * @param address - the address of visitor
     * @param phoneNum - the phoneNum of visitor
     */
    public RegisterVisitorRequest(LBMS lbms, String firstName, String lastName, String address, String phoneNum){
        this.lbms = lbms;
        this.visitorFirstName = firstName;
        this.visitorLastName = lastName;
        this.visitorAddress = address;
        this.visitorPhoneNum = phoneNum;

        //initially assume the registration was not successful
        this.registeredSuccessfully = false;

        //initially assume the registration is not a duplicate
        this.duplicateRegistration = false;
    }

    @Override
    public void execute() {
        this.uniqueId = generateId();

        //Create a visitor to see if that visitor is in the LBMS already.
        Visitor visitorToRegister = new Visitor(this.visitorFirstName, this.visitorLastName, this.visitorAddress,
                this.visitorPhoneNum, this.uniqueId);

        //Set registration status so response() can accurately respond to what was executed.
        if(this.lbms.getVisitors().values().contains(visitorToRegister)){
            this.duplicateRegistration = true;
        }
        else{
            this.lbms.registerVisitor(visitorToRegister);
            this.registeredSuccessfully = true;
        }
    }

    @Override
    public String response(){
        if (this.duplicateRegistration){
            return "register,duplicate;";
        }else if(this.registeredSuccessfully){
            return "register," + this.uniqueId + "," + lbms.getTime();
        }else{
            return "Should never happen";
        }
    }

    /**
     * generateId generates a 10 digit unique visitor Id.
     *
     * @return String representation of the 10 digit Id.
     */
     private String generateId(){
        ArrayList<String> visitorIds = this.lbms.getVisitorIds();
        Random rnd = new Random();
        int n = 100000000 + rnd.nextInt(900000000);

        //initially generate new ID.
        String stringId = Integer.toString(n);

        //Continuously generate new IDs until it is unique.
        while (visitorIds.contains(stringId)) {
            n = 100000000 + rnd.nextInt(900000000);
            stringId = Integer.toString(n);
        }

         String lastDigit = Integer.toString(((int) Math.floor(Math.random() * (10))));
         stringId += lastDigit;
         return stringId;
    }
}
