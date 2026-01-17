package main.commands.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.ticket.actions.TicketActionFactory;
import main.ticket.utils.HistoryManager;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;

import java.util.List;

public final class UndoAssignTicketCommand extends Command {
    private final int ticketId;

    public UndoAssignTicketCommand(final JsonNode node) {
        super(node);
        this.ticketId = node.get("ticketID").asInt();
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        final Ticket ticket = TicketContainer.getInstance().getTicketById(ticketId);

        ticket.unassign();
        HistoryManager.getInstance().addToTicketHistory(ticketId,
                TicketActionFactory.deAssigned(this.username, timestamp));
        return null;
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        final Ticket ticket = TicketContainer.getInstance().getTicketById(ticketId);
        final String errorMessage;

        if (!ticket.isInProgress()) {
            errorMessage = "Only IN_PROGRESS tickets can be unassigned.";
            return buildErrorResult(errorMessage);
        }
        return null;
    }
}
