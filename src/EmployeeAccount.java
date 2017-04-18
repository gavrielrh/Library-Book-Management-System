/**
 * Filename: EmployeeAcount.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 * EmployeeAccount represents
 */
public class EmployeeAccount implements Account {


    private String username;
    private String password;
    private Visitor visitor;

    private boolean loggedIn;


    /**
     * Constructor for EmployeeAccount. On creation, employees are not logged in.
     * @param username - the username for the employee
     * @param password - the password for the employee
     * @param visitor - the actual visitor
     */
    public EmployeeAccount(String username, String password, Visitor visitor ){

        this.username = username;
        this.password = password;
        this.visitor = visitor;

        //initially creating an account does not log the user in
        this.loggedIn = false;

    }

    /**
     * getPassword return's the employee's password and is used in LBMS authentication
     * @return - the String of the employee's password
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
     * isLoggedIn returns whether or not the employee is logged in
     * @return - boolean value if the employee is logged in
     */
    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public String getVisitorId(){
        return this.visitor.getUniqueId();
    }

    public String getUsername(){
        return  this.username;
    }
}

