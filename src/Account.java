/**
 *
 * Filename: Account.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * Account is the interface for accounts.
 * Each account must have a log in, and a role type.
 */
public interface Account {
    void login();
    void logout();
    String getPassword();
    boolean isLoggedIn();

}
