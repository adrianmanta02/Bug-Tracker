package main.commands.reports.metricbased.customerimpact;

import lombok.Getter;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.Map;

/**
 * main.commands.reports.metricbased.customerimpact.CustomerImpactReportDTO is a DTO, non-business
 * logic class which helps with serialization in terms of calculating the application's
 * report regarding its impact over customers.
 */
@Getter
public final class CustomerImpactReportDTO {
    private final int totalTickets;
    private final Map<TicketType, Integer> ticketsByType;
    private final Map<BusinessPriority, Integer> ticketsByPriority;
    private final Map<TicketType, Double> customerImpactByType;

    public CustomerImpactReportDTO(final int totalTickets,
                                final Map<TicketType, Integer> ticketsByType,
                                final Map<BusinessPriority, Integer> ticketsByPriority,
                                final Map<TicketType, Double> customerImpactByType) {
        this.totalTickets = totalTickets;
        this.ticketsByType = ticketsByType;
        this.ticketsByPriority = ticketsByPriority;
        this.customerImpactByType = customerImpactByType;
    }
}
