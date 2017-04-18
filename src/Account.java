/**
 *
 * Filename: Account.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * Account is the interface for accounts.
 * Each account must have a log in, and a role type.
 */
public interface Account {

    /* accounts must support logging in and out */
    void login();
    void logout();

    /* used for account authentication */
    String getUsername();
    String getPassword();

    /* used for authentication checking */
    boolean isLoggedIn();
    String getVisitorId();

    /* used for determining which protection proxy to use */
    String getRole();
}
