package main.commands.reports.performance.strategies;

import main.commands.reports.performance.utils.calculation.PerformanceMetrics;

public interface PerformanceCalculationStrategy {
    /**
     * Performs the calculation needed for a Ticket Performance Report,
     * for all devs subordinated to a manager,
     * by taking into account multiple {@link main.ticket.Ticket} statistics
     * collected in a month.
     * @param performanceMetrics centralized entity with metrics needed for calculation
     * @return a real number value with the result of the calculation, following
     * a formula of determining the performance
     */
    double calculate(PerformanceMetrics performanceMetrics);
}
