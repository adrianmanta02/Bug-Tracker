package main.commands.reports.performance.utils.services;

import main.commands.reports.performance.utils.calculation.PerformanceCalculator;
import main.commands.reports.performance.utils.calculation.PerformanceMetrics;
import main.commands.reports.performance.utils.dtos.PerformanceReportDTO;
import main.ticket.utils.TicketContainer;
import main.users.Developer;
import main.users.Manager;
import main.users.UserContainer;

import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static main.common.SystemConstants.NORMALIZATION_FACTOR;

public final class PerformanceReportService {

    private final UserContainer userContainer;
    private final TicketContainer ticketContainer;

    public PerformanceReportService(final UserContainer userContainer,
                                    final TicketContainer ticketContainer) {
        this.userContainer = userContainer;
        this.ticketContainer = ticketContainer;
    }

    /**
     * Generates performance reports for all developers managed by the given manager.
     * Performance is calculated based on ticket activity from the specified month.
     * @param manager the manager whose subordinates are evaluated
     * @param month the month for which performance is calculated
     * @return a sorted list of performance report DTOs
     */
    public List<PerformanceReportDTO> generateForManager(final Manager manager,
                                                         final Month month) {

        List<PerformanceReportDTO> reports = new ArrayList<>();
        List<Developer> subordinates =
                userContainer.getManagerSubordinates(manager);

        for (Developer developer : subordinates) {
            PerformanceMetrics metrics = collectMetrics(developer, month);
            double score = calculatePerformanceScore(developer, metrics, month);

            reports.add(buildReport(developer, metrics, score));
        }

        return reports.stream()
                .sorted(Comparator.comparing(PerformanceReportDTO::getUsername))
                .toList();
    }

    /**
     * Creates and returns a new {@link PerformanceMetrics} object
     * storing all information needed in order to create a performance
     * report for a given developer. The report is being generated monthly,
     * and it needs all statistics regarding {@link Developer}'s work done.
     * @param developer the analyzed developer
     * @param month the month in which the report is being generated
     * @return a {@code PerformanceMetrics} object containing all stats
     */
    private PerformanceMetrics collectMetrics(final Developer developer,
                                              final Month month) {
        TicketSolvingAnalyst analyst = new TicketSolvingAnalyst(developer);

        return new PerformanceMetrics(
                analyst.countClosedTickets(month),
                analyst.countBugsSolved(month),
                analyst.countFeatureSolved(month),
                analyst.countUiSolved(month),
                analyst.countHighPriorityTickets(month),
                analyst.averageResolutionTime(month)
        );
    }

    /**
     * Calculates the score obtained after gathering all statistics
     * needed to generate the performance report of a given developer.
     * @param developer the analyzed developer
     * @param metrics all statistics gathered in one object
     * @param month month in which the report is being generated
     * @return a real value representing the performance score the developer
     * has obtained at the end of the month based on their work.
     */
    private double calculatePerformanceScore(final Developer developer,
                                            final PerformanceMetrics metrics,
                                             final Month month) {
        PerformanceCalculator calculator =
                new PerformanceCalculator(developer, month);

        double score = calculator.calculatePerformance(developer, metrics);
        score = Math.round(score * NORMALIZATION_FACTOR) / NORMALIZATION_FACTOR;
        developer.setPerformanceScore(score);

        return score;
    }

    /**
     * Creates and returns the DTO object associated with the performance report
     * @param developer the analyzed developer
     * @param metrics all statistics gathered in one object
     * @param score the performance score that was obtained by the dev at the
     *              end of a month
     * @return a {@link PerformanceReportDTO} describing the whole report,
     * helping with serialization
     */
    private PerformanceReportDTO buildReport(final Developer developer,
                                            final PerformanceMetrics metrics,
                                            final double score) {
        return new PerformanceReportDTO(
                developer.getUsername(),
                metrics.getClosedTickets(),
                metrics.getAverageResolutionTime(),
                score,
                developer.getSeniority()
        );
    }
}
