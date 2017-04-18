/**
 * Filename: connectClientRequest.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * connectClientRequest represents a concreteCommand to connect a client.
 */
public class DisconnectClientRequest implements Request{

    /* have the LBMS be inside the request for getting Id */
    private LBMS lbms;

    /* clientId is the id for the client */
    private String clientId;

    /**
     * Constructor for connectClientRequest
     * @param lbms - the LBMS itself.
     */
    public DisconnectClientRequest(LBMS lbms, String clientId){
        this.lbms = lbms;
    }

    /**
     * execute sets the clientId to the request for the response
     */
    @Override
    public void execute(){
        this.lbms.disconnectClient(this.lbms.getClient(this.clientId));
    }

    /**
     * response is the response to executing the command with the client Id
     * @return - String of response
     */
    @Override
    public String response(){
        return "disconnect," + this.clientId + ";";
    }
}
