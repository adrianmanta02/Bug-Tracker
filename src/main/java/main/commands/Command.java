package main.commands;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.errors.NoPermissionError;
import main.errors.UserNotFoundHandler;
import main.errors.ValidationHandler;
import main.users.User;
import main.users.UserContainer;
import main.users.enums.common.Role;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public abstract class Command {
    protected final String command;
    protected final String username;
    protected final LocalDate timestamp;

    protected Command(final JsonNode node) {
        this.command = node.get("command").asText();
        this.username = node.get("username").asText();
        this.timestamp = LocalDate.parse(node.get("timestamp").asText());
    }

    /**
     * Method merging validation and the proper execution of the command.
     * It also handles the common issues that
     * can be encountered across commands, such as user problems or permissions.
     * Uses Chain of Responsibility to keep track of the order of validation.
     * Further on, it handles the particular validation rule.
     * If validation passed, proceeds to execute the command
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a command result containing an error if validation did not pass,
     * or the result obtained after running
     */
    public CommandResult executeCommand(final CommandContext commandContext) {
        ValidationHandler userNotFoundHandler = new UserNotFoundHandler();
        ValidationHandler noPermissionError = new NoPermissionError();

        userNotFoundHandler.setNext(noPermissionError);

        CommandResult generalValidationError = userNotFoundHandler.handle(this, commandContext);
        if (generalValidationError != null) {
            return generalValidationError;
        }

        CommandResult commandValidationError = validateCommand(commandContext);
        if (commandValidationError != null) {
            return commandValidationError;
        }

        // all validations passed, execute command;
        return execute(commandContext);
    }

    /**
     * Builds a {@link CommandResult} object with an error message, used
     * in validation for commands.
     * @param errorMessage String representing the error alert given for a missed validation
     * @return {@link CommandResult}
     */
    public CommandResult buildErrorResult(final String errorMessage) {
        return new CommandResult.CommandResultBuilder(
                this.command,
                this.username,
                this.timestamp
        )
        .error(errorMessage)
        .build();
    }

    /**
     * @return the user associated with the current command. The application is user-centric,
     * meaning each command is called by a user with a certain role.
     */
    public User getInvoker() {
        return UserContainer.getInstance().getUserByUsername(this.username);
    }

    /**
     * Shows which user roles are allowed to perform the command
     * @return a list of {@link Role}
     */
    public abstract List<Role> getAllowedRoles();

    /**
     * Performs validation depending on the type of command. Each command may
     * have (or not) rules in order to be run.
     * @param commandContext the context in which the command is executed
     *                         providing necessary environment or state
     * @return null if there is no validation to be done / if validation passed;
     * error message if conditions are not met
     */
    protected abstract CommandResult validateCommand(CommandContext commandContext);

    /**
     * Does the proper execution of the command, after it being filtered
     * through the {@link Command#validateCommand}
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return null if there is no output associated for the command or an object
     * that is going to be serialized.
     */
    public abstract CommandResult execute(CommandContext commandContext);
}
