package main.commands.query;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.commands.query.strategies.viewmilestones.MilestoneViewStrategy;
import main.commands.query.strategies.viewmilestones.MilestoneViewStrategyFactory;
import main.milestone.Milestone;
import main.users.enums.common.Role;

import java.util.List;

import static main.common.SystemConstants.MILESTONES_FIELD;

public final class ViewMilestonesCommand extends Command {
    public ViewMilestonesCommand(final JsonNode node) {
        super(node);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER, Role.DEVELOPER);
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }

    /**
     * Does a query inside milestone's system to find all milestones that are
     * either created by a {@link main.users.Manager} or assigned to a
     * {@link main.users.Developer}.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the Milestones
     * available for the invoker, with all the statistics being calculated
     * (number of closed & opened tickets inside each milestone, their deadlines
     * days and the number of days of being late on completing it).
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        MilestoneViewStrategy viewStrategy = MilestoneViewStrategyFactory
                .createCommandStrategy(this.getInvoker());

        List<Milestone> milestones = viewStrategy.getItems(commandContext, this.getInvoker());

        milestones.forEach(m -> m.calculateFields(this.timestamp));
        return new CommandResult.CommandResultBuilder(
                this.command,
                this.username,
                this.timestamp
        ).data(MILESTONES_FIELD, milestones)
        .build();
    }
}
