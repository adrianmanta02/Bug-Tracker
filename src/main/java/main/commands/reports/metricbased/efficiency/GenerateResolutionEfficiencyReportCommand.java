package main.commands.reports.metricbased.efficiency;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;

import java.util.List;

import static main.common.SystemConstants.REPORT_FIELD;

public final class GenerateResolutionEfficiencyReportCommand extends Command {
    public GenerateResolutionEfficiencyReportCommand(final JsonNode jsonNode) {
        super(jsonNode);
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

    /**
     * Executes the command to generate a resolution efficiency report for all
     * solved and closed tickets in the system.
     * The method performs the following
     * Retrieves all solved and closed tickets from {@link TicketContainer}.
     * Uses a {@link ResolutionEfficiencyVisitor} to calculate metrics for each ticket.
     * Uses {@code MetricReportGenerator} to count tickets by type, count tickets by priority,
     * calculate scores for each ticket type.
     * It aggregates the results into a {@link ResolutionEfficiencyDTO} report.
     * @param commandContext the context providing access to containers and services
     * @return a {@link CommandResult} containing the generated resolution efficiency report
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        TicketContainer container = TicketContainer.getInstance();
        List<Ticket> solvedAndClosedTickets = container.getSolvedAndClosedTickets();

        ResolutionEfficiencyReportService reportService = new ResolutionEfficiencyReportService();
        ResolutionEfficiencyDTO report = reportService.generateReport(solvedAndClosedTickets,
                                                                        commandContext);

        return new CommandResult.CommandResultBuilder(
                command, username, timestamp)
                .data(REPORT_FIELD, report)
                .build();
    }
}
