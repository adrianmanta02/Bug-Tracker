package main.commands.reports.metricbased.efficiency;

import main.commands.context.CommandContext;
import main.commands.reports.ReportService;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.commands.reports.metricbased.MetricReportGenerator;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.List;
import java.util.Map;

public final class ResolutionEfficiencyReportService
        implements ReportService<ResolutionEfficiencyDTO> {


    /**
     * Generates a resolution efficiency report for a given list of tickets.
     * This method performs the following steps:
     * -uses a {@link ResolutionEfficiencyVisitor} to calculate efficiency scores for each
     * ticket type.
     * -uses a {@link MetricReportGenerator} to aggregate ticket counts by type and priority.
     * -compiles the total number of tickets, counts by type and priority, and calculated
     * scores into a {@link ResolutionEfficiencyDTO}
     * @param tickets the list of tickets to include in the efficiency report
     * @param commandContext the {@link CommandContext} providing access to the current
     *                       application state
     * @return a {@link ResolutionEfficiencyDTO} containing total ticket count, counts by
     * type and priority, and efficiency scores per ticket type
     */
    @Override
    public ResolutionEfficiencyDTO generateReport(final List<Ticket> tickets,
                                                  final CommandContext commandContext) {
        TicketMetricVisitor visitor = new ResolutionEfficiencyVisitor();
        MetricReportGenerator generator = new MetricReportGenerator();

        int total = tickets.size();
        Map<TicketType, Integer> byType = generator.getTicketsByType(tickets);
        Map<BusinessPriority, Integer> byPriority = generator.getTicketsByPriority(tickets);
        Map<TicketType, Double> scores = generator.calculateScores(tickets, visitor);

        return new ResolutionEfficiencyDTO(total, byType, byPriority, scores);
    }
}
