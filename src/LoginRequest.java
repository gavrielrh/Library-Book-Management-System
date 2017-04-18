/*
 * Filename: LoginRequest.java
 *
 * @author - Brendan Jones (bpj1651@rit.edu)
 *         <p>
 *         LoginRequest represents a concreteCommand in the Request subsystem.
 *         The request is attempting to log a user in, given a client.
 */

public class LoginRequest implements Request {

    private Client client;
    private LBMS lbms;
    private String username;
    private String password;

    boolean success;

    /**
     * Constructor for LoginRequest
     *
     * @param client   the client being used to log in
     * @param lbms     the lbms being logged into
     * @param username the username for the given user
     * @param password the password for the given user
     */
    public LoginRequest(Client client, LBMS lbms, String username, String password) {
        this.client = client;
        this.lbms = lbms;
        this.username = username;
        this.password = password;
        this.success = false;
    }

    @Override
    public void execute() {
        if (this.lbms.hasAccount(this.username)) {
            if (this.lbms.authenticate(this.username, this.password)) {
                Account account = this.lbms.getAccount(this.username);

                this.success = true;

                this.client.logIn(account);
            } else {
                this.success = false;
            }
        } else {
            this.success = false;
        }
    }

    @Override
    public String response() {
        String response = this.client.getId() + ",login,";

        if (this.success) {
            response += "success;";
        } else {
            response += "bad-username-or-password";
        }

        return response;
    }
}
