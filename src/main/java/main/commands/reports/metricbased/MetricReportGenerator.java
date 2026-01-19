package main.commands.reports.metricbased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;
import main.ticket.utils.TicketContainer;

import static main.common.SystemConstants.ROUNDING_FACTOR;

/**
 * main.commands.reports.metricbased.customerimpact.MetricReportGenerator is responsible
 * for centralizing all data about a given set of tickets, by distributing it
 * in multiple classes. This is heavily used in metric reports generating purposes.
 */
public final class MetricReportGenerator {
    /**
     * Creates and returns the repartition of a given set of tickets on
     * multiple types.
     * @param tickets the list of tickets that need to be split
     * @return a categorized-based map of ticket unique identifiers
     */
    public Map<TicketType, Integer> getTicketsByType(final List<Ticket> tickets) {

        TicketContainer container = TicketContainer.getInstance();
        Map<TicketType, Integer> ticketsByType = new LinkedHashMap<>();

        ticketsByType.put(TicketType.BUG, container.countTicketsByType(tickets, TicketType.BUG));
        ticketsByType.put(TicketType.FEATURE_REQUEST, container.countTicketsByType(tickets,
                                                                    TicketType.FEATURE_REQUEST));
        ticketsByType.put(TicketType.UI_FEEDBACK, container.countTicketsByType(tickets,
                                                                        TicketType.UI_FEEDBACK));

        return ticketsByType;
    }

    /**
     * Creates and returns the repartition of a given set of tickets on
     * multiple priorities.
     * @param tickets the list of tickets that need to be split
     * @return a categorized-based map of ticket unique identifiers
     */
    public Map<BusinessPriority, Integer> getTicketsByPriority(final List<Ticket> tickets) {
        TicketContainer container = TicketContainer.getInstance();
        Map<BusinessPriority, Integer> ticketsByPriority = new LinkedHashMap<>();

        ticketsByPriority.put(BusinessPriority.LOW,
                                container.countTicketsByPriority(tickets, BusinessPriority.LOW));
        ticketsByPriority.put(BusinessPriority.MEDIUM,
                                container.countTicketsByPriority(tickets, BusinessPriority.MEDIUM));
        ticketsByPriority.put(BusinessPriority.HIGH,
                                container.countTicketsByPriority(tickets, BusinessPriority.HIGH));
        ticketsByPriority.put(BusinessPriority.CRITICAL,
                            container.countTicketsByPriority(tickets, BusinessPriority.CRITICAL));

        return ticketsByPriority;
    }

    /**
     * Calculates the average scores of a list of {@link Ticket}s grouped by their type.
     * Each ticket is processed using a {@link TicketMetricVisitor} instance, which
     * computes a score for that ticket via the visitor pattern.
     * The method then groups scores by ticket type (as a string) and calculates the
     * average for each type. The returned map always contains entries for
     * "BUG", "FEATURE_REQUEST", and "UI_FEEDBACK", even if there are no tickets
     * of a given type, in which case the average is {@code 0.0}.
     * Scores are rounded to two decimal places.
     *
     * @param tickets the list of {@link Ticket}s to process
     * @param calculator the {@link TicketMetricVisitor} used to calculate scores for each ticket
     * @return a {@link Map} where the keys are ticket type names and the values are
     *         the average scores for tickets of that type, rounded to two decimal places
     */
    public Map<TicketType, Double> calculateScores(final List<Ticket> tickets,
                                                final TicketMetricVisitor calculator) {

        Map<TicketType, List<Double>> scoresByType = new HashMap<>();
        for (Ticket ticket : tickets) {
            double score = ticket.accept(calculator);

            TicketType type = ticket.getType();
            scoresByType.computeIfAbsent(type, k -> new ArrayList<>()).add(score);
        }
        Map<TicketType, Double> averages = new LinkedHashMap<>();
        // Always include all ticket types in order, even if no data
        for (TicketType ticketType : TicketType.values()) {
            List<Double> scores = scoresByType.get(ticketType);
            double avg;
            if (scores != null && !scores.isEmpty()) {
                avg = scores.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
                avg = Math.round(avg * ROUNDING_FACTOR) / ROUNDING_FACTOR;
            } else {
                avg = 0.0;
            }
            averages.put(ticketType, avg);
        }

        return averages;
    }
}
