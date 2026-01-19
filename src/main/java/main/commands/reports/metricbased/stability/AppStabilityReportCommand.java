package main.commands.reports.metricbased.stability;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;

import java.util.List;

import static main.common.SystemConstants.REPORT_FIELD;

public final class AppStabilityReportCommand extends Command {
    public AppStabilityReportCommand(final JsonNode node) {
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
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }

    @Override
    public CommandResult execute(final CommandContext commandContext) {
        TicketContainer container = commandContext.getTicketContainer();
        List<Ticket> unsolvedTickets = container.getUnsolvedTickets();

        AppStabilityReportService reportService = new AppStabilityReportService();
        AppStabilityReportDTO report = reportService
                .generateReport(unsolvedTickets, commandContext);

        return new CommandResult.CommandResultBuilder(
                command, username, timestamp)
                .data(REPORT_FIELD, report)
                .build();
    }
}
