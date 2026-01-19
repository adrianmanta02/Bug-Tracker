package main.commands.reports.metricbased.ticketrisk;

import main.commands.context.CommandContext;
import main.commands.reports.ReportService;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.commands.reports.metricbased.MetricReportGenerator;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TicketRiskReportService implements ReportService<TicketRiskReportDTO> {

    /**
     * Generates a risk report for a given list of unsolved tickets.
     * This method performs the following
     * -uses a {@link TicketRiskVisitor} to calculate numeric risk scores for each ticket type.
     * -uses a {@link MetricReportGenerator} to aggregate ticket counts by type and priority.
     * -converts numeric risk scores into qualitative risk levels using {@link RiskQualifier}.
     * @param unsolvedTickets the list of unsolved tickets to include in the risk report
     * @param context the {@link CommandContext} providing access to application state
     *                (not used in this implementation but kept for future extension)
     * @return a {@link TicketRiskReportDTO} containing ticket counts and risk qualifications
     */
    public TicketRiskReportDTO generateReport(final List<Ticket> unsolvedTickets,
                                              final CommandContext context) {
        TicketMetricVisitor visitor = new TicketRiskVisitor();
        MetricReportGenerator generator = new MetricReportGenerator();

        int total = unsolvedTickets.size();
        Map<TicketType, Integer> byType = generator.getTicketsByType(unsolvedTickets);
        Map<BusinessPriority, Integer> byPriority = generator.getTicketsByPriority(unsolvedTickets);
        Map<TicketType, Double> numericScores = generator.calculateScores(unsolvedTickets, visitor);

        Map<TicketType, RiskLevel> qualifications = new LinkedHashMap<>();
        for (Map.Entry<TicketType, Double> entry : numericScores.entrySet()) {
            qualifications.put(entry.getKey(), RiskQualifier.qualify(entry.getValue()));
        }

        return new TicketRiskReportDTO(total, byType, byPriority, qualifications);
    }
}
