package main.commands.reports.performance.utils.calculation;
import main.commands.reports.performance.strategies.JuniorCalculationStrategy;
import main.commands.reports.performance.strategies.MidCalculationStrategy;
import main.commands.reports.performance.strategies.PerformanceCalculationStrategy;
import main.commands.reports.performance.strategies.SeniorCalculationStrategy;
import main.users.Developer;
import java.time.Month;

public final class PerformanceCalculator {
    private final Developer developer;
    private final Month month;

    public PerformanceCalculator(final Developer developer, final Month month) {
        this.developer = developer;
        this.month = month;
    }

    /**
     * Wrapper method to do the performance calculation.
     * Each {@link Developer}'s performance report is being generated
     * different, according to their {@link main.users.enums.developer.Seniority},
     * by applying variate coefficients.
     * @param dev the Developer for whom the report is to be calculated
     * @param metrics centralized entity with all general Ticket solving statistics during
     *                a month
     * @return a real number value representing the performance parameter.
     */
    public double calculatePerformance(final Developer dev,
                                       final PerformanceMetrics metrics) {
        PerformanceCalculationStrategy strategy = switch (dev.getSeniority()) {
            case JUNIOR -> new JuniorCalculationStrategy();
            case MID -> new MidCalculationStrategy();
            case SENIOR -> new SeniorCalculationStrategy();
        };

        return strategy.calculate(metrics);
    }
}
