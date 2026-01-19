package main.commands.flow;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.milestone.MilestoneContainer;
import main.users.enums.common.Role;
import java.util.List;

public final class StartTestingPhaseCommand extends Command {
    public StartTestingPhaseCommand(final JsonNode node) {
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
     * Checks if there is the possibility of starting a new testing
     * phase, in which the reporters can start creating tickets for issues
     * found across the application. A testing phase cannot be started
     * if there are still milestones active (having {@link main.ticket.Ticket}s
     * to solve).
     * @param commandContext the context in which the command is executed
     *                         providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of a failed validation
     * or null if all validations have passed
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        MilestoneContainer milestoneContainer = commandContext.getMilestoneContainer();
        if (milestoneContainer.hasActiveMilestones()) {
            return buildErrorResult("Cannot start a new testing phase.");
        }
        return null;
    }

    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        commandContext.setLastTestingPhaseTimestamp(this.timestamp);
        commandContext.setEnableTestingPhase(true);
        return null;
    }
}
