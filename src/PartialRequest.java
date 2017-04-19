/*
 * Filename: PartialRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * PartialRequest represents a ConcreteCommand within the Command Design Pattern.
 * No action is handled in the execute method, just a simple response back
 */

public class PartialRequest implements Request {

    /**
     * Constructor for a PartialRequest Command
     */
    public PartialRequest() {
        //no fields in PartialRequest
    }

    @Override
    public void execute() {
        //do nothing. The command was a partial request
    }

    @Override
    public String response() {
        return "partial-request;";
    }
}
