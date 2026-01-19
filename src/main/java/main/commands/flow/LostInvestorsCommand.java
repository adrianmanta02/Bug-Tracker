package main.commands.flow;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.users.enums.common.Role;

import java.util.List;

public final class LostInvestorsCommand extends Command {
    public LostInvestorsCommand(final JsonNode node) {
        super(node);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER);
    }

    /**
     * @see Command#execute(CommandContext)
     * Stops the execution of the map, since the investors
     * have lost their patience.
     * @return null as there is no output for the command
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        commandContext.setAppRunning(false);
        return null;
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }
}
