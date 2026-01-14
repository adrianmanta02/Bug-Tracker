package main.commands;

import main.commands.context.CommandContext;
import main.commands.context.CommandResult;

public interface CommandValidationStrategy<C extends Command> {
    /**
     * Ensures extra validation for executing the command, depending on the role
     * calling the command.
     * @param commandContext instance containing useful data for running a command
     * @param command the command which needs validation
     * @return null if validation passed or error message
     */
    CommandResult validate(CommandContext commandContext, C command);
}
