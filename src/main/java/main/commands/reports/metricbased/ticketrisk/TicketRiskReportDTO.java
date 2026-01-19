package main.commands.reports.metricbased.ticketrisk;
import lombok.Getter;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.Map;

@Getter
public final class TicketRiskReportDTO {
    private final int totalTickets;
    private final Map<TicketType, Integer> ticketsByType;
    private final Map<BusinessPriority, Integer> ticketsByPriority;
    private final Map<TicketType, RiskLevel> riskByType;

    public TicketRiskReportDTO(final int totalTickets,
                               final Map<TicketType, Integer> ticketsByType,
                               final Map<BusinessPriority, Integer> ticketsByPriority,
                               final Map<TicketType, RiskLevel> riskByType) {
        this.totalTickets = totalTickets;
        this.ticketsByType = ticketsByType;
        this.ticketsByPriority = ticketsByPriority;
        this.riskByType = riskByType;
    }
}
