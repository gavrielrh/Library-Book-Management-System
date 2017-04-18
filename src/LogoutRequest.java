/*
 * Filename: LoginRequest.java
 *
 * @author - Brendan Jones (bpj1651@rit.edu)
 *         <p>
 *         LoginRequest represents a concreteCommand in the Request subsystem.
 *         The request is attempting to log a user in, given a client.
 */

public class LogoutRequest implements Request {

    private Client client;
    private LBMS lbms;

    /**
     * Constructor for LogoutRequest
     *
     * @param client the client being used to log in
     * @param lbms   the lbms to log into
     */
    public LogoutRequest(Client client, LBMS lbms) {
        this.client = client;
        this.lbms = lbms;
    }

    @Override
    public void execute() {
        if (this.client.clientLoggedIn()) {
            this.client.logout();
        }
    }

    @Override
    public String response() {
        return "logout,success;";
    }
}
