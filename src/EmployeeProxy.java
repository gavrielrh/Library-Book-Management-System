/**
 * Created by brendanjones44 on 4/18/17.
 */
public class EmployeeProxy implements PermissionProxy{

    private ClientInvoker clientInvoker;

    public EmployeeProxy(ClientInvoker clientInvoker){
        this.clientInvoker = clientInvoker;
    }

    public String handleCommand(String input){
        return this.clientInvoker.handleCommand(input);
    }

    public boolean hasPermission(String commandName){
        //all employees have "god status
        return true;
    }
}
