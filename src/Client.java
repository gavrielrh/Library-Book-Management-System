/**
 * Filename: Client.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * Client represents a client being connected to LBMS.
 */
public class Client {


    /* each client has a unique Id */
    private String clientId;

    private Account account;

    private ClientProxy proxy;

    private SystemInvoker systemInvoker;
    /**
     * Constructor for Client.
     * @param lbms - used for concurrent users.
     * When constructing the client, LBMS handles the connections
     */
    public Client(LBMS lbms){
        this.systemInvoker = new SystemInvoker(lbms);
    }

    /**
     * connect connects this to LBMS.
     */

    public void connect(String clientId){
        this.clientId = clientId;
    }

    public String getId(){
        return this.clientId;
    }

    public String handleCommand(String clientCommand){
        return this.systemInvoker.handleCommand(clientCommand);
    }




}
