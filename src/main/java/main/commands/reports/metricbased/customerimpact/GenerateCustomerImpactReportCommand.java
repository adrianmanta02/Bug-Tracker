package main.commands.reports.metricbased.customerimpact;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;

import java.util.List;

import static main.common.SystemConstants.REPORT_FIELD;

public final class GenerateCustomerImpactReportCommand extends Command {
    public GenerateCustomerImpactReportCommand(final JsonNode node) {
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
     * Generates a customer impact report of the app, relying on the tickets
     * being OPEN and IN_PROGRESS, by doing computations with certain fields
     * they have. It also provides a statistic regarding tickets being split
     * on categories.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of the command,
     * being a report for customer purposes.
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        TicketContainer container = commandContext.getTicketContainer();

        List<Ticket> unsolvedTickets = container.getUnsolvedTickets();
        CustomerImpactReportService reportService = new CustomerImpactReportService();
        CustomerImpactReportDTO report = reportService.generateReport(unsolvedTickets,
                                                                    commandContext);

        return new CommandResult.CommandResultBuilder(
                command, username, timestamp)
            .data(REPORT_FIELD, report)
            .build();
    }

    /**
     * @see Command#validateCommand(CommandContext)
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        return null;
    }
}
