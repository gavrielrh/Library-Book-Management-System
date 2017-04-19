/*
 * File: Request.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * Interface for all the LBMS Requests
 * - Follows the Command design pattern
 * - Since all LMBS Requests have a response associated with it, the interface requires a response
 */

public interface Request {

    /**
     * Execute method required by all ConcreteCommands. Executes the command for LBMS
     */
    void execute();

    /**
     * response is what lets the invoker know what the result of executing the ConcreteCommand was
     *
     * @return The String response from executing the command.
     */
    String response();
}
