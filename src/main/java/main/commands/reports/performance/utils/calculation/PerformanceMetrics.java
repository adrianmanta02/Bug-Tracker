package main.commands.reports.performance.utils.calculation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PerformanceMetrics {
    private final int closedTickets;
    private final int bugCount;
    private final int featureCount;
    private final int uiCount;
    private final int highPriorityTickets;
    private final double averageResolutionTime;

    public PerformanceMetrics(final int closedTickets,
                              final int bugCount,
                              final int featureCount,
                              final int uiCount,
                              final int highPriorityTickets,
                              final double averageResolutionTime) {
        this.closedTickets = closedTickets;
        this.bugCount = bugCount;
        this.featureCount = featureCount;
        this.uiCount = uiCount;
        this.highPriorityTickets = highPriorityTickets;
        this.averageResolutionTime = averageResolutionTime;
    }
}
