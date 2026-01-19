package main.commands.reports.performance.utils.calculation;

import static main.common.SystemConstants.TICKET_TYPE_COUNT;

public final class PerformanceCalculationUtils {
    private PerformanceCalculationUtils() { }

    /**
     * Computes the average number of resolved tickets per ticket type.
     * The average is calculated across the three supported ticket types
     * (bug, feature request, and UI feedback).
     * @param bug the number of resolved bug tickets
     * @param feature the number of resolved feature request tickets
     * @param ui the number of resolved UI feedback tickets
     * @return the average number of resolved tickets per ticket type
     */
    public static double averageResolvedTicketType(final int bug, final int feature, final int ui) {
        return (bug + feature + ui) / TICKET_TYPE_COUNT;
    }

    /**
     * Computes the standard deviation of resolved tickets across ticket types.
     * This value represents how unevenly resolved tickets are distributed
     * among bug, feature request, and UI feedback ticket types. A higher value
     * indicates a greater imbalance.
     * @param bug the number of resolved bug tickets
     * @param feature the number of resolved feature request tickets
     * @param ui the number of resolved UI feedback tickets
     * @return the standard deviation of resolved tickets per ticket type
     */
    public static double standardDeviation(final int bug, final int feature, final int ui) {
        double mean = averageResolvedTicketType(bug, feature, ui);
        double variance = (Math.pow(bug - mean, 2)
                + Math.pow(feature - mean, 2)
                + Math.pow(ui - mean, 2)) / TICKET_TYPE_COUNT;
        return Math.sqrt(variance);
    }

    /**
     * Computes a diversity factor for resolved tickets across ticket types.
     * The diversity factor is defined as the ratio of the standard deviation
     * to the mean number of resolved tickets. It provides a normalized measure
     * of how evenly tickets are resolved across different types.
     * A value of {@code 0.0} indicates either no resolved tickets or a perfectly
     * balanced distribution.
     * @param bug the number of resolved bug tickets
     * @param feature the number of resolved feature request tickets
     * @param ui the number of resolved UI feedback tickets
     * @return the ticket diversity factor, or {@code 0.0} if no tickets were resolved
     */
    public static double ticketDiversityFactor(final int bug, final int feature, final int ui) {
        double mean = averageResolvedTicketType(bug, feature, ui);

        // diversity is 0 if there are no tickets
        if (mean == 0.0) {
            return 0.0;
        }

        double std = standardDeviation(bug, feature, ui);
        return std / mean;
    }
}
