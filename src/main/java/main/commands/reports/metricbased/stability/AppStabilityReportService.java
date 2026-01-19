package main.commands.reports.metricbased.stability;

import main.commands.context.CommandContext;
import main.commands.reports.ReportService;
import main.commands.reports.metricbased.TicketMetricVisitor;
import main.commands.reports.metricbased.customerimpact.CustomerImpactVisitor;
import main.commands.reports.metricbased.MetricReportGenerator;
import main.commands.reports.metricbased.ticketrisk.RiskLevel;
import main.commands.reports.metricbased.ticketrisk.RiskQualifier;
import main.commands.reports.metricbased.ticketrisk.TicketRiskVisitor;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AppStabilityReportService implements ReportService<AppStabilityReportDTO> {

    /**
     * Generates a complete application stability report based on the provided list of tickets.
     * This method orchestrates the calculation of various metrics, including:
     * -ticket counts by type and priority
     * -customer impact scores using the provided {@link TicketMetricVisitor}
     * -ticket risk scores, which are converted into qualitative risk levels
     * -the overall application stability based on risk qualifications and impact scores
     * Additionally, if the resulting stability is determined to be {@code "STABLE"},
     * the method will mark the application as stopped via the {@code CommandContext}.
     *
     * @param unsolvedTickets the list of tickets to analyze; typically all unsolved tickets
     * @param commandContext the {@link CommandContext} providing access to application state and
     *                       containers
     * @return a report containing all computed data.
     */
    public AppStabilityReportDTO generateReport(final List<Ticket> unsolvedTickets,
                                                    final CommandContext commandContext) {
        MetricReportGenerator generator = new MetricReportGenerator();

        // get common data
        int total = unsolvedTickets.size();
        Map<TicketType, Integer> byType = generator.getTicketsByType(unsolvedTickets);
        Map<BusinessPriority, Integer> byPriority = generator.getTicketsByPriority(unsolvedTickets);

        // calculate Customer Impact (numeric scores)
        TicketMetricVisitor impactVisitor = new CustomerImpactVisitor();
        Map<TicketType, Double> impactScores = generator
                                                .calculateScores(unsolvedTickets, impactVisitor);

        // calculate Ticket Risk (numeric then qualify)
        TicketMetricVisitor riskVisitor = new TicketRiskVisitor();
        Map<TicketType, Double> riskNumericScores = generator
                                                    .calculateScores(unsolvedTickets, riskVisitor);

        // convert risk scores to qualifications
        // always include all ticket types
        Map<TicketType, RiskLevel> riskQualifications = new LinkedHashMap<>();
        for (TicketType ticketType : TicketType.values()) {
            Double score = riskNumericScores.get(ticketType);
            if (score != null && score > 0.0) {
                riskQualifications.put(ticketType, RiskQualifier.qualify(score));
            } else {
                riskQualifications.put(ticketType, RiskLevel.NEGLIGIBLE);
            }
        }

        // determine stability
        String stability = StabilityCalculator.determineStability(
                total,
                riskQualifications,
                impactScores
        );

        // If STABLE, mark app as stopped
        if (stability.equals(StabilityLevel.STABLE.toString())) {
            commandContext.setAppRunning(false);
        }

        // build report
        return new AppStabilityReportDTO(
                total,
                byType,
                byPriority,
                riskQualifications,
                impactScores,
                stability
        );
    }
}
