package main.commands.query;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.users.enums.common.Role;

import java.util.Comparator;
import java.util.List;

import static main.common.SystemConstants.ASSIGNED_TICKETS_FIELD;

public final class ViewAssignedTicketsCommand extends Command {
    public ViewAssignedTicketsCommand(final JsonNode node) {
        super(node);
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    /**
     * Retrieves all assigned tickets for current Developer, which is
     * requesting the command. After performing the search, it returns the
     * set of Tickets in an ordered manner, by {@link main.ticket.enums.common.BusinessPriority}
     * by creation time and unique identifier.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the details after
     * filtering the tickets needed for view.
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        List<Ticket> assignedTickets = commandContext
                .getTicketContainer()
                .getTicketsAssignedTo(this.getInvoker().getUsername());

        List<AssignedTicketView> orderedTickets = assignedTickets
                .stream()
                .sorted(
                        Comparator.comparing(Ticket::getBusinessPriority, Comparator.reverseOrder())
                                .thenComparing(Ticket::getCreatedAt)
                                .thenComparing(Ticket::getId)
                )
                .map(AssignedTicketView::new)
                .toList();

        return new CommandResult.CommandResultBuilder(
                this.command,
                this.username,
                this.timestamp
        ).data(ASSIGNED_TICKETS_FIELD, orderedTickets)
        .build();
    }

    /**
     * @see Command#validateCommand(CommandContext)
     * */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }
}
