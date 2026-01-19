package main.commands.reports.performance.strategies;

import main.commands.reports.performance.utils.calculation.PerformanceCalculationUtils;
import main.commands.reports.performance.utils.calculation.PerformanceMetrics;

import static main.common.SystemConstants.CLOSED_TICKETS_COEFFICIENT;
import static main.common.SystemConstants.JUNIOR_PERFORMANCE_BONUS;

public final class JuniorCalculationStrategy implements PerformanceCalculationStrategy {
    /**
     * @see PerformanceCalculationStrategy#calculate(PerformanceMetrics)
     */
    @Override
    public double calculate(final PerformanceMetrics performanceMetrics) {
        if (performanceMetrics.getClosedTickets() == 0) {
            return 0;
        }

        return Math.max(0, CLOSED_TICKETS_COEFFICIENT * performanceMetrics.getClosedTickets()
                - PerformanceCalculationUtils.ticketDiversityFactor(
                        performanceMetrics.getBugCount(),
                        performanceMetrics.getFeatureCount(),
                        performanceMetrics.getUiCount()
                )) + JUNIOR_PERFORMANCE_BONUS;
    }
}
