/**
 * Filename: VisitorAccount.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * VisiorAccount
 */
public class VisitorAccount implements Account {


    private String username;
    private String password;
    private Visitor visitor;

    private boolean loggedIn;


    /**
     * Constructor for VisitorAccount. On creation, visitors are not logged in.
     * @param username - the username for the visitor
     * @param password - the password for the visitor
     * @param visitor - the actual visitor
     */
    public VisitorAccount(String username, String password, Visitor visitor ){

        this.username = username;
        this.password = password;
        this.visitor = visitor;

        //initially creating an account does not log the user in
        this.loggedIn = false;

    }

    /**
     * getPassword return's the visitor's password and is used in LBMS authentication
     * @return - the String of the visitor's password
     */
    public String getPassword(){
        return this.password;
    }
    /**
     * login sets the user's login state to be true
     */
    public void login(){
        this.loggedIn = true;
    }

    /**
     * logout sets the user's logout state to be false
     */
    public void logout(){
        this.loggedIn = false;
    }

    /**
     * isLoggedIn returns whether or not the visitor is logged in
     * @return - boolean value if the visitor is logged in
     */
    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public String getVisitorId(){
        return  this.visitor.getUniqueId();
    }

    public String getUsername(){
        return this.username;
    }

    public String getRole(){
        return "visitor";
    }
}
