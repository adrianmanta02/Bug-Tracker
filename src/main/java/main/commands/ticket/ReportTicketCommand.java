package main.commands.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.parsing.ParserUtils;
import main.ticket.Ticket;
import main.ticket.TicketFactory;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;

import java.time.LocalDate;
import java.util.List;

import static main.common.SystemConstants.TESTING_PHASE_DURATION;

public final class ReportTicketCommand extends Command {
    private final JsonNode params;
    public ReportTicketCommand(final JsonNode node) {
        super(node);
        this.params = node.get("params");
    }

    /**
     * Checks if the application is currently running in the testing phase,
     * which ensures ticket reporting. A testing phase lasts exactly 12 days.
     * @param commandContext instance containing multiple data such as containers
     *                       useful for running a command
     * @return a boolean showing whether the timestamp in which the command
     *          occurs is inside a testing phase.
     */
    private boolean isInTestingPhase(final CommandContext commandContext) {
        LocalDate now = this.getTimestamp();
        LocalDate testingPhaseStart = commandContext.getLastTestingPhaseTimestamp();

        int daysBetween = ParserUtils.dayDifference(testingPhaseStart, now);
        return daysBetween <= TESTING_PHASE_DURATION;
    }
    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.REPORTER);
    }


    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        if (!this.isInTestingPhase(commandContext)) {
            return buildErrorResult("Tickets can only be reported during testing phases.");
        }

        final String type = params.get("type").asText();
        final String reportedBy = params.get("reportedBy").asText();
        final boolean isAnonymous = reportedBy.isEmpty();

        if (isAnonymous && !type.equals(TicketType.BUG.toString())) {
            return buildErrorResult("Anonymous reports are only allowed for tickets of type BUG.");
        }
        return null;
    }

    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        final String reportedBy = params.get("reportedBy").asText();
        final boolean isAnonymous = reportedBy.isEmpty();

        BusinessPriority businessPriority = BusinessPriority
                            .fromString(params.get("businessPriority").asText());
        if (isAnonymous) {
            businessPriority = BusinessPriority.LOW;
        }

        TicketContainer ticketContainer = commandContext.getTicketContainer();
        final int ticketId = ticketContainer.getNextId();

        Ticket ticket = TicketFactory.createTicket(params, ticketId,
                                            businessPriority, this.timestamp);
        ticketContainer.addTicket(ticket);
        return null;
    }
}
