package main.errors;

import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.users.User;
import main.users.enums.common.Role;

import java.util.List;
import java.util.stream.Collectors;

public final class NoPermissionError extends AbstractValidationHandler {
    /**
     * Does a role validation before running the command.Each command
     * has a list of allowed roles. The method checks invoker's role
     * against trusted ones.
     * @param command the command being checked.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of handling the command
     */
    @Override
    protected CommandResult validate(final Command command,
                                        final CommandContext commandContext) {
        User searchedUser = command.getInvoker();
        List<Role> rolesRequired = command.getAllowedRoles();

        String roles = rolesRequired
                .stream()
                .map(Role::toString)
                .collect(Collectors.joining(", "));

        if (!rolesRequired.contains(searchedUser.getRole())) {
            return new CommandResult.CommandResultBuilder(
                    command.getCommand(),
                    command.getUsername(),
                    command.getTimestamp()
            )
            .error("The user does not have permission to execute this command: "
                    + "required role " + roles + "; user role "
                    + searchedUser.getRole().toString()  + ".")
            .build();
        }
        return null;
    }
}
