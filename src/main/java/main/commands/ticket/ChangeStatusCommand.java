package main.commands.ticket;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.milestone.MilestoneService;
import main.ticket.actions.TicketActionFactory;
import main.ticket.enums.common.TicketStatus;
import main.ticket.utils.HistoryManager;

@Getter
public final class ChangeStatusCommand extends StatusCommand {
    public ChangeStatusCommand(final JsonNode node) {
        super(node);
    }

    /**
     * Performs the execution for {@code ChangeStatusCommand}. After its validation,
     * the command is responsible with changing the status for a given ticket.
     * It also handles the case in which a closed ticket might be the last one
     * available in a {@link main.milestone.Milestone}, meaning that the milestone
     * is completed and will not block other milestones anymore (if it has already blocked).
     * All developers belonging to those milestones must be notified.
     * On the other hand, the status changing for the given {@link main.ticket.Ticket}
     * must be tracked inside ticket's history.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result obtained after
     * running the command.
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        if (this.ticketToChange.isClosed()) {
            return null;
        }

        ticketToChange.changeStatus();
        ticketToChange.checkForSolved(timestamp);

        commandContext
                .getMilestoneService()
                .checkForMilestoneFreezing(ticketToChange, timestamp);

        MilestoneService milestoneService = commandContext.getMilestoneService();

        if (ticketToChange.getStatus() == TicketStatus.CLOSED) {
            milestoneService.handleMilestoneCompletionAfterTicketClosing(ticketId, timestamp);
        }

        HistoryManager historyManager = commandContext.getHistoryManager();
        final String latestStatus = historyManager.getLatestHistoryStatusForTicket(ticketId);

        historyManager.addToTicketHistory(ticketId,
                TicketActionFactory.statusChanged(
                        username,
                        timestamp,
                        latestStatus,
                        ticketToChange.getStatus().toString()
                )
        );

        return null;
    }
}
