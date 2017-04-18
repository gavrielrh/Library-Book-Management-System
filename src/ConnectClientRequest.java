/**
 * Filename: ConnectClientRequest.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * ConnectClientRequest represents a concreteCommand to connect a client.
 */
public class ConnectClientRequest implements Request{

    /* have the LBMS be inside the request for getting Id */
    private LBMS lbms;

    /* clientId is the id for the client */
    private String clientId;

    /**
     * Constructor for ConnectClientRequest
     * @param lbms - the LBMS itself.
     */
    public ConnectClientRequest(LBMS lbms){
        this.lbms = lbms;
    }

    /**
     * execute sets the clientId to the request for the response
     */
    @Override
    public void execute(){
        this.clientId = this.lbms.connectClient();
    }

    /**
     * response is the response to executing the command with the client Id
     * @return - String of response
     */
    @Override
    public String response(){
        return "connect," + this.clientId + ";";
    }
}