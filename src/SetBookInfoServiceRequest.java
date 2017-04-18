/*
 * Filename: SetBookInfoServiceRequest.java
 * @author - Gavriel Rachael-Homann (gxr2329@rit.edu)
 * SetBookInfoServiceRequest represents a ConcreteCommand within the Command Design pattern.
 * Invoking the command sets the book information service within LBMS.
 */

public class SetBookInfoServiceRequest implements Request, UndoableCommand {

    /* Have the LBMS part of the request, in order to execute commands */
    private LBMS lbms;

    private String service;
    private BookStoreSearchRequest.BOOKSERVICE BOOKSERVICE;
    private BookStoreSearchRequest.BOOKSERVICE previousBOOKSERVICE;
    private String employeeId;

    private boolean undoSuccess;

    /**
     * Constructor for the SetBookInfoServiceRequest
     * @param lbms - the system itself. This is so execute can call lbms commands
     * @param employeeId - the Id of the employee beginning a request.
     * @param service - the service to set the system to use
     */
    public SetBookInfoServiceRequest(LBMS lbms, String employeeId, String service){
        this.lbms = lbms;
        this.service = service;
        this.employeeId = employeeId;
    }

    @Override
    public void execute() {
        switch (this.service) {
            case "google":
                this.BOOKSERVICE = BookStoreSearchRequest.BOOKSERVICE.google;
                break;
            case "local":
                this.BOOKSERVICE = BookStoreSearchRequest.BOOKSERVICE.local;
                break;
            default:
                break;
        }

        if(this.BOOKSERVICE != null) {
            this.lbms.setBookService(this.BOOKSERVICE);
        }
    }

    @Override
    public String response(){
        if(this.BOOKSERVICE == null) {
            return employeeId + ",unknown-service";
        } else {
            return employeeId + ",service,success";
        }
    }

    /**
     * Undo's the command
     * @return whether the command was successfully undone
     */
    public boolean undo(){
        if(previousBOOKSERVICE != null) {
            this.undoSuccess = true;
            this.lbms.setBookService(previousBOOKSERVICE);
            return true;
        } else {
            this.undoSuccess = false;
            return false;
        }
    }

    /**
     * Redo's the command
     * @return whether the command was successfully redone
     */
    public boolean redo(){
        if(this.undoSuccess){
            this.lbms.setBookService(this.BOOKSERVICE);
            return true;
        } else {
            return false;
        }
    }
}

