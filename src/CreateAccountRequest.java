/**
 * Filename: CreateAccountRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * CreateAccountRequest represents a concrete command for the request subystem.
 * Executing checks for errors and updates the request for the response
 */
public class CreateAccountRequest implements Request{
    Client client;
    LBMS lbms;
    String username;
    String password;
    String role;
    String visitorId;
    boolean duplicateUsername;
    boolean duplicateId;
    boolean invalidVisitor;
    public CreateAccountRequest(Client client, LBMS lbms, String username, String password, String role, String visitorId){
        this.client = client;
        this.lbms = lbms;
        this.username = username;
        this.password = password;
        this.role = role;
        this.visitorId = visitorId;
    }

    @Override
    public void execute(){
        if(this.lbms.hasAccount(this.username)){
            this.duplicateUsername = true;
        }else if(this.lbms.visitorHasAccount(this.visitorId)){
            this.duplicateId = true;
        }else if(!this.lbms.hasVisitor(this.visitorId)){
            this.invalidVisitor = true;
        }else{
            Visitor visitor = this.lbms.getVisitor(visitorId);
            if(this.role.equals("employee")){
                Account employeeAccount = new EmployeeAccount(this.username, this.password, visitor);
                this.lbms.addAccount(employeeAccount);
            }else if(this.role.equals("visitor")){
                Account visitorAccount = new VisitorAccount(this.username, this.password, visitor);
                this.lbms.addAccount(visitorAccount);
            }
        }
    }

    @Override
    public String response(){
        String response = this.client.getId() + ",create,";
        if(this.duplicateUsername){
            response += "duplicate-username;";
        }else if(this.duplicateId){
            response += "duplicate-visitor;";
        }else if(this.invalidVisitor){
            response += "invalid-visitor;";
        }else{
            response += "success;";
        }
        return response;
    }
}
