package main.commands.query.strategies.viewtickets;

import main.commands.context.CommandContext;
import main.commands.query.strategies.ViewStrategy;
import main.ticket.Ticket;
import main.users.User;

import java.util.Comparator;
import java.util.List;

public interface TicketViewStrategy extends ViewStrategy<Ticket> {
    /**
     * Retrieves all Tickets a user can see, taking into account the rules
     * each {@link main.users.enums.common.Role} brings.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @param user User instance whose role is deciding the algorithm for viewing
     * @return a list of {@link Ticket} obtained after filtering according
     * to viewing rules
     */
    List<Ticket> getItems(CommandContext commandContext, User user);

    /**
     * Sorts the filtered result by creation time and id.
     * @param ticketsObtained list of filtered tickets
     * @return a sorted mutable list of tickets
     */
    @Override
    default List<Ticket> sortResult(final List<Ticket> ticketsObtained) {
        return ticketsObtained.stream()
                .sorted(Comparator.comparing(Ticket::getCreatedAt)
                        .thenComparing(Ticket::getId))
                .toList();
    }
}
