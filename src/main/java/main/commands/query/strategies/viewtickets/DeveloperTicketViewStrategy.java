package main.commands.query.strategies.viewtickets;

import main.commands.context.CommandContext;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.users.User;

import java.util.List;

public final class DeveloperTicketViewStrategy implements TicketViewStrategy {
    /**
     * @see TicketViewStrategy#getItems(CommandContext, User)
     */
    @Override
    public List<Ticket> getItems(final CommandContext commandContext,
                                 final User user) {
        List<Milestone> assignedMilestones = commandContext
                .getMilestoneContainer()
                .getMilestonesAssociatedTo(user.getUsername());

        return sortResult(commandContext
                .getTicketContainer()
                .getOpenTicketsFromMilestones(assignedMilestones)
        );
    }
}
