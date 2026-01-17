package main.commands.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.milestone.MilestoneService;
import main.ticket.Ticket;
import main.ticket.actions.TicketAction;
import main.ticket.actions.TicketActionFactory;
import main.ticket.enums.common.TicketStatus;
import main.ticket.utils.HistoryManager;

import java.util.Optional;

public final class UndoChangeStatusCommand extends StatusCommand {
    public UndoChangeStatusCommand(final JsonNode node) {
        super(node);
    }

    /**
     * Resets the Ticket status to the last had before it being changed by
     * {@link ChangeStatusCommand}. Manages all the actions needed to be
     * reverted, including the solved parameters being refactored after undoing
     * the status change. The action itself is recorded in {@link Ticket}'s
     * history
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return null, no output for this command
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        Ticket ticket = commandContext.getTicketContainer().getTicketById(ticketId);
        HistoryManager historyManager = commandContext.getHistoryManager();
        MilestoneService milestoneService = commandContext.getMilestoneService();

        Optional<TicketAction> lastStatusChange = historyManager.getLastStatusChange(ticketId,
                                                                                this.username);
        if (lastStatusChange.isEmpty()) {
            return null;
        }

        // get the "from" status (what it was before the change)
        String previousStatusName = lastStatusChange.get().getFrom();
        TicketStatus previousStatus = TicketStatus.valueOf(previousStatusName);
        TicketStatus currentStatus = ticket.getStatus();

        ticket.setStatus(previousStatus);
        if (previousStatus == TicketStatus.IN_PROGRESS || previousStatus == TicketStatus.OPEN) {
            ticket.setSolvedAt(null);
        }

        milestoneService.unfreezeMilestoneByTicketStatus(ticketId,
                previousStatus,
                currentStatus
        );

        HistoryManager.getInstance().addToTicketHistory(ticketId,
                TicketActionFactory.statusChanged(
                        this.username,
                        this.timestamp,
                        currentStatus.name(),
                        previousStatus.name()
                ));

        return null;
    }
}
