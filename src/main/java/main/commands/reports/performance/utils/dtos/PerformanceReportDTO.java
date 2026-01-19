package main.commands.reports.performance.utils.dtos;

import lombok.Getter;
import main.users.enums.developer.Seniority;

/**
 * main.commands.reports.performance.PerformanceReportDTO is a DTO, non-business
 * logic class which helps with serialization in terms of calculating the monthly
 * performance report for all developers coming from a manager's suborder
 */
@Getter
public final class PerformanceReportDTO {
    private final String username;
    private final int closedTickets;
    private final double averageResolutionTime;
    private final double performanceScore;
    private final Seniority seniority;

    public PerformanceReportDTO(final String username,
                                final int closedTickets,
                                final double averageResolutionTime,
                                final double performanceScore,
                                final Seniority seniority) {
        this.username = username;
        this.closedTickets = closedTickets;
        this.averageResolutionTime = averageResolutionTime;
        this.performanceScore = performanceScore;
        this.seniority = seniority;
    }
}
