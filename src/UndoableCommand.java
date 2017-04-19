/*
 * File: UndoableCommand.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * Interface for all the undoable commands
 */

public interface UndoableCommand {
    /**
     * Handles undo of command
     *
     * @return whether the command was undone successfully
     */
    boolean undo();

    /**
     * Handles redo of command
     *
     * @return whether the command was redone successfully
     */
    boolean redo();
}
