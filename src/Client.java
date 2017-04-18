/*
 * Filename: Client.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * Client represents a client being connected to LBMS.
 */

public class Client {
    /* each client has a unique Id */
    private String clientId;

    /* each client MAY have an account signed in to it. initially none */
    private Account account;

    /* each client MAY have a proxy associated with it. initially none */
    private PermissionProxy proxy;

    /* invoker used to handle client commands that don't require login */
    private SystemInvoker systemInvoker;

    /**
     * Constructor for Client.
     *
     * @param lbms - used for concurrent users.
     *             When constructing the client, LBMS handles the connections
     */
    public Client(LBMS lbms) {
        this.systemInvoker = new SystemInvoker(lbms);
    }

    /**
     * connect establishes the connection with the clientId to the LBMS.
     *
     * @param clientId - the Id associated with the Client object and LBMS.
     */
    public void connect(String clientId) {
        this.clientId = clientId;
    }

    /**
     * getId - returns the clientId used in the connection
     *
     * @return - String. clientId used in the connection.
     */
    public String getId() {
        return this.clientId;
    }

    /**
     * handleClientCommand
     *
     * @param clientCommand
     * @return
     */
    public String handleClientCommand(String clientCommand) {
        return this.proxy.handleCommand(clientCommand);
    }

    /**
     * Logs in the given account
     *
     * @param account the account to log in
     */
    public void logIn(Account account) {
        this.account = account;
        this.account.login();

        String role = account.getRole();

        if (role.equals("visitor")) {
            this.proxy = new VisitorProxy(new ClientInvoker(this.systemInvoker.getLBMS(), account));
        } else if (role.equals("employee")) {
            this.proxy = new EmployeeProxy(new ClientInvoker(this.systemInvoker.getLBMS(), account));
        }
    }

    /**
     * Returns whether client is logged in
     *
     * @return if the client is logged in or not
     */
    public boolean clientLoggedIn() {
        return this.account != null;
    }

    /**
     * Logs out the current account
     */
    public void logout() {
        if (this.account != null) {
            this.account.logout();
        }

        this.account = null;
        this.proxy = null;
    }
}
