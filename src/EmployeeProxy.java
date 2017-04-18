/*
 * Filename: EmployeeProxy.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * EmployeeProxy handles the inbetween for Employee commands
 */

public class EmployeeProxy implements PermissionProxy {

    private ClientInvoker clientInvoker;

    /**
     * Constructor for EmployeeProxy.
     *
     * @param clientInvoker the invoker for the employee
     */
    public EmployeeProxy(ClientInvoker clientInvoker) {
        this.clientInvoker = clientInvoker;
    }

    @Override
    public String handleCommand(String input) {
        return this.clientInvoker.handleCommand(input);
    }

    @Override
    public boolean hasPermission(String commandName) {
        //all employees have "god status
        return true;
    }
}
