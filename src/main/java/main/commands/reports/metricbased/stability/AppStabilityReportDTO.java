package main.commands.reports.metricbased.stability;

import lombok.Getter;
import main.commands.reports.metricbased.ticketrisk.RiskLevel;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.Map;

/**
 * main.commands.reports.metricbased.stability.AppStabilityReportDTO is a DTO, non-business
 * logic class which helps with serialization in terms of calculating the application's
 * report regarding its stability for usage.
 */
@Getter
public final class AppStabilityReportDTO {
    private final int totalOpenTickets;

    private final Map<TicketType, Integer> openTicketsByType;
    private final Map<BusinessPriority, Integer> openTicketsByPriority;
    private final Map<TicketType, RiskLevel> riskByType;
    private final Map<TicketType, Double> impactByType;
    private final String appStability;

    public AppStabilityReportDTO(final int totalOpenTickets,
                                 final Map<TicketType, Integer> openTicketsByType,
                                 final Map<BusinessPriority, Integer> openTicketsByPriority,
                                 final Map<TicketType, RiskLevel> riskByType,
                                 final Map<TicketType, Double> impactByType,
                                 final String appStability) {
        this.totalOpenTickets = totalOpenTickets;
        this.openTicketsByType = openTicketsByType;
        this.openTicketsByPriority = openTicketsByPriority;
        this.riskByType = riskByType;
        this.impactByType = impactByType;
        this.appStability = appStability;
    }
}
