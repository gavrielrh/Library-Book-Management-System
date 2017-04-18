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

    private PermissionProxy proxy;

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

    public String handleClientCommand(String clientCommand){
        return this.proxy.handleCommand(clientCommand);
    }

    public void logIn(Account account){
        this.account = account;
        this.account.login();
        String role = account.getRole();
        if(role.equals("visitor")){
            this.proxy = new VisitorProxy(new ClientInvoker(this.systemInvoker.getLBMS(), account));
        }else if(role.equals("employee")){
            this.proxy = new EmployeeProxy(new ClientInvoker(this.systemInvoker.getLBMS(), account));
        }
    }

    public boolean clientLoggedIn(){
        return this.account != null;
    }

    public void logout(){
        if(this.account != null) {
            this.account.logout();
        }
        this.account = null;
        this.proxy = null;
    }



}
