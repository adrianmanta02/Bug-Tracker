package main.errors;

import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;

public abstract class AbstractValidationHandler implements ValidationHandler {
    private ValidationHandler next;
    /**
     * @see ValidationHandler#handle(Command, CommandContext)
     */
    @Override
    public CommandResult handle(final Command command, final CommandContext commandContext) {
        CommandResult error = this.validate(command, commandContext);
        if (error != null) {
            return error;
        }

        if (next == null) {
            return null;
        }
        return next.handle(command, commandContext);
    }

    /**
     * @see ValidationHandler#setNext(ValidationHandler)
     */
    @Override
    public void setNext(final ValidationHandler next) {
        this.next = next;
    }

    /**
     * Does a set of validations regarding the execution of a command
     * and the given parameters. Once passed, the command begins its execution
     * phase, having to return (or not) an output.
     * @param command the command being checked.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of the command
     */
    protected abstract CommandResult validate(Command command,
                                              CommandContext commandContext);
}
