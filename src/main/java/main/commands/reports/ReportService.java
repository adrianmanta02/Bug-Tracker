package main.commands.reports;

import main.commands.context.CommandContext;
import main.ticket.Ticket;

import java.util.List;

/**
 * Interface used for encapsulating the calculation algorithms
 * and information gathering in terms of reports that are being generated
 * regarding app stability, developers' performances etc.
 * @param <T> the DTO which represents the encapsulated report obtained
 */
public interface ReportService<T> {
    /**
     * Generates a complete application report based on the provided list of tickets.
     * This method orchestrates the calculation of various metrics.
     * @param tickets the list of tickets to analyze;
     * @param commandContext the {@link CommandContext} providing access to application
     *                       state and containers
     * @return a report of type T containing all computed data.
     */
    T generateReport(List<Ticket> tickets,
                     CommandContext commandContext);
}
