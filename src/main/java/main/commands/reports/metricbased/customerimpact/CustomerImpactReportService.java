package main.commands.reports.metricbased.customerimpact;

import main.commands.context.CommandContext;
import main.commands.reports.ReportService;
import main.commands.reports.metricbased.MetricReportGenerator;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.List;
import java.util.Map;

public final class CustomerImpactReportService
        implements ReportService<CustomerImpactReportDTO> {

    /**
     * @see ReportService#generateReport(List, CommandContext)
     */
    @Override
    public CustomerImpactReportDTO generateReport(final List<Ticket> tickets,
                                                  final CommandContext commandContext) {
        TicketMetricVisitor visitor = new CustomerImpactVisitor();

        MetricReportGenerator generator = new MetricReportGenerator();

        int total = tickets.size();
        Map<TicketType, Integer> byType = generator.getTicketsByType(tickets);
        Map<BusinessPriority, Integer> byPriority = generator.getTicketsByPriority(tickets);
        Map<TicketType, Double> scores = generator.calculateScores(tickets, visitor);

        return new CustomerImpactReportDTO(total, byType,
                byPriority, scores);
    }
}
