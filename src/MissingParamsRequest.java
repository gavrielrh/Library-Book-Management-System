/**
 * Filename: MissingParamsRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 * MissingParamsRequest represents a ConcreteCommand within the Command Design Pattern.
 * No action is handled in the execute method, just a simple response back.
 */

/* imports */
import java.util.ArrayList;

/**
 * MissingParamsRequest represents a ConcreteCommand within the Command Design Pattern.
 * No action is handled in the execute method, just a simple response back.
 */
public class MissingParamsRequest implements Request {

    /* information needed for request */
    private String requestName;
    private ArrayList<String> missingParamNames;

    /**
     * Constructor for a PartialRequest Command
     * @param requestName - The name of the request associated with the missing parameters.
     * @param missingParamNames - An ArrayList of missingParamNames used for returning the response
     */
    public MissingParamsRequest(String requestName, ArrayList<String> missingParamNames){
        this.requestName = requestName;
        this.missingParamNames = missingParamNames;
    }

    @Override
    public void execute() {
        //do nothing. The command was a partial request
    }

    /**
     * response returns the missing parameters in a String format.
     * @return - the String of missing parameters.
     */
    @Override
    public String response(){
        String returnString = this.requestName + ",missing-parameters,{";
        for (int i = 0; i < this.missingParamNames.size(); i++){
            String missingParam = this.missingParamNames.get(i);
            returnString += (missingParam);
            if(!(i == this.missingParamNames.size() -1)) {
                returnString += ",";
            }
        }
        returnString += "};";
        return returnString;
    }
}
