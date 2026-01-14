package main.errors;

import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.users.UserContainer;

public final class UserNotFoundHandler extends AbstractValidationHandler {
    /**
     * @see AbstractValidationHandler#validate(Command, CommandContext)
     */
    @Override
    protected CommandResult validate(final Command command,
                                     final CommandContext commandContext) {
        if (!UserContainer.getInstance().exists(command.getUsername())) {
            return new CommandResult.CommandResultBuilder(
                    command.getCommand(),
                    command.getUsername(),
                    command.getTimestamp()
            ).error("The user " + command.getUsername() + " does not exist.")
            .build();
        }

        return null; // user exists, validation passed;
    }
}
