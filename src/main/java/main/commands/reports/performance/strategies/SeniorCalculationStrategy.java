package main.commands.reports.performance.strategies;

import main.commands.reports.performance.utils.calculation.PerformanceMetrics;

import static main.common.SystemConstants.CLOSED_TICKETS_COEFFICIENT;
import static main.common.SystemConstants.SENIOR_RESOLUTION_TIME_PENALTY;
import static main.common.SystemConstants.SENIOR_PERFORMANCE_BONUS;

public class SeniorCalculationStrategy implements PerformanceCalculationStrategy {
    /**
     * @see PerformanceCalculationStrategy#calculate(PerformanceMetrics)
     */
    @Override
    public double calculate(final PerformanceMetrics metrics) {
        if (metrics.getClosedTickets() == 0) {
            return 0;
        }

        return Math.max(0, CLOSED_TICKETS_COEFFICIENT * metrics.getClosedTickets()
               + 1.0 * metrics.getHighPriorityTickets()
               - SENIOR_RESOLUTION_TIME_PENALTY * metrics.getAverageResolutionTime())
               + SENIOR_PERFORMANCE_BONUS;
    }
}
