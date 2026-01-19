package main.commands.query.strategies.viewtickets;

import main.commands.context.CommandContext;
import main.ticket.Ticket;
import main.users.User;

import java.util.List;

public final class ManagerTicketViewStrategy implements TicketViewStrategy {
    /**
     * @see TicketViewStrategy#getItems(CommandContext, User)
     */
    @Override
    public List<Ticket> getItems(final CommandContext commandContext,
                                 final User user) {
        return sortResult(commandContext
                .getTicketContainer()
                .getAllTickets()
        );
    }
}
