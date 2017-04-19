/*
 * Filename: PermissionProxy.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 *
 * PermissionProxy handles the inbetween for authority based commands
 */

public interface PermissionProxy {
    /**
     * Whether or not the command has permission.
     *
     * @param commandName the name of the command
     * @return if there is permission for the command
     */
    boolean hasPermission(String commandName);

    /**
     * Handles the command based on permission.
     *
     * @param input the command itself
     * @return the response after handling the command
     */
    String handleCommand(String input);
}
