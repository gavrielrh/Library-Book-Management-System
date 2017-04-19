/*
 * File: VisitorProxy.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * Inbetween for visitors, handling commands run by visitor
 */

import java.util.HashSet;

public class VisitorProxy implements PermissionProxy {
    private ClientInvoker clientInvoker;
    Account account;

    /**
     * Constructor for VisitorProxy.
     *
     * @param clientInvoker the invoker for the client.
     */
    public VisitorProxy(ClientInvoker clientInvoker) {
        this.clientInvoker = clientInvoker;
    }

    /**
     * Handles the given command
     *
     * @param input the command itself
     * @return whether the command ran successfully
     */
    public String handleCommand(String input) {
        String[] array = input.split(",");
        String commandName = array[0];

        if (commandName.contains(";")) {
            commandName = commandName.substring(0, commandName.length() - 1);
        }

        if (hasPermission(commandName)) {
            //check to make sure the visitor isn't attempting to check someone else in/out.
            if (commandName.equals("arrive") || commandName.equals("depart")) {
                if (array.length > 1) {
                    return "not-authorized;";
                }
            }

            return clientInvoker.handleCommand(input);
        } else {
            return "not-authorized;";
        }
    }

    /**
     * Returns whether the visitor has permission for the given command.
     *
     * @param commandName the name of the command
     * @return if the visitor has permission to use the command
     */
    public boolean hasPermission(String commandName) {
        HashSet<String> acceptableCommands = new HashSet<>();

        acceptableCommands.add("arrive");
        acceptableCommands.add("info");
        acceptableCommands.add("borrow");
        acceptableCommands.add("depart");
        acceptableCommands.add("undo");
        acceptableCommands.add("redo");

        return acceptableCommands.contains(commandName);
    }
}
