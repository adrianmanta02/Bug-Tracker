package main.commands.reports.metricbased.efficiency;

import lombok.Getter;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;

import java.util.Map;

/**
 * main.commands.reports.metricbased.efficiency.ResolutionEfficiencyDTO is a DTO, non-business
 * logic class which helps with serialization in terms of calculating a report regarding
 * the efficiency of Developers in solving issues reported.
 */
@Getter
public final class ResolutionEfficiencyDTO {
    private final int totalTickets;
    private final Map<TicketType, Integer> ticketsByType;
    private final Map<BusinessPriority, Integer> ticketsByPriority;
    private final Map<TicketType, Double> efficiencyByType;

    public ResolutionEfficiencyDTO(final int totalTickets,
                                   final Map<TicketType, Integer> ticketsByType,
                                   final Map<BusinessPriority, Integer> ticketsByPriority,
                                   final Map<TicketType, Double> efficiencyByType) {
        this.totalTickets = totalTickets;
        this.ticketsByType = ticketsByType;
        this.ticketsByPriority = ticketsByPriority;
        this.efficiencyByType = efficiencyByType;
    }
}
