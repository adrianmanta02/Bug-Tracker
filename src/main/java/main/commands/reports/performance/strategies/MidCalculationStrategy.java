package main.commands.reports.performance.strategies;

import main.commands.reports.performance.utils.calculation.PerformanceMetrics;

import static main.common.SystemConstants.*;

public final class MidCalculationStrategy implements PerformanceCalculationStrategy {
    /**
     * @see PerformanceCalculationStrategy#calculate(PerformanceMetrics)
     */
    @Override
    public double calculate(final PerformanceMetrics performanceMetrics) {
        if (performanceMetrics.getClosedTickets() == 0) {
            return 0;
        }

        return Math.max(0, CLOSED_TICKETS_COEFFICIENT * performanceMetrics.getClosedTickets()
                + HIGH_PRIORITY_COEFFICIENT * performanceMetrics.getHighPriorityTickets()
                - MID_RESOLUTION_TIME_PENALTY * performanceMetrics.getAverageResolutionTime())
                + MID_PERFORMANCE_BONUS;
    }
}
