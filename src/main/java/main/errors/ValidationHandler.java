package main.errors;

import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;

public interface ValidationHandler {
    /**
     * Handles a given command within the provided context.
     * Implementations of this method execute the logic associated with the command
     * and return a {@link CommandResult} representing the outcome.
     * @param command the command to be processed
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of handling the command
     */
    CommandResult handle(Command command, CommandContext commandContext);

    /**
     * Sets the next validation rule in the chain.
     * This method is used to implement the Chain of Responsibility pattern,
     * where each validation handler delegates to the next one in sequence.
     * @param next the next {@link ValidationHandler} to be executed after
     *             this one
     */
    void setNext(ValidationHandler next);
}
