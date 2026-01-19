package main.commands.reports.performance.utils.services;

import java.time.Month;
import java.util.List;

import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketType;
import main.ticket.utils.TicketContainer;
import main.users.Developer;

import static main.common.SystemConstants.NORMALIZATION_FACTOR;

/**
 * main.commands.reports.TicketSolvingAnalyst is a class responsible
 * for gathering data related to {@link Ticket}s solved by a given
 * {@link Developer}
 */
public final class TicketSolvingAnalyst {
    private Developer developer;
    public TicketSolvingAnalyst(final Developer developer) {
        this.developer = developer;
    }

    /**
     * Counts the number of bug tickets solved by this developer
     * during the specified month.
     * Only tickets of type {@code BUG} that were solved within
     * the given month and assigned to this developer are considered.
     * @param month the month for which solved bug tickets are to be counted
     * @return the number of solved bug tickets for the given month
     */
    public int countBugsSolved(final Month month) {
        return (int) TicketContainer
                .getInstance()
                .getTicketsSolvedInAMonthByDev(month, developer.getUsername())
                .stream()
                .filter(ticket -> ticket.getType() == TicketType.BUG)
                .count();
    }

    /**
     * Counts the number of UI feedback tickets solved by this developer
     * during the specified month.
     * Only tickets of type {@code UI_FEEDBACK} that were solved within
     * the given month and assigned to this developer are considered.
     * @param month the month for which solved UI feedback tickets are to be counted
     * @return the number of solved UI feedback tickets for the given month
     */
    public int countUiSolved(final Month month) {
        return (int) TicketContainer
                .getInstance()
                .getTicketsSolvedInAMonthByDev(month, developer.getUsername())
                .stream()
                .filter(ticket -> ticket.getType() == TicketType.UI_FEEDBACK)
                .count();
    }

    /**
     * Counts the number of feature request tickets solved by this developer
     * during the specified month.
     * Only tickets of type {@code FEATURE_REQUEST} that were solved within
     * the given month and assigned to this developer are considered.
     * @param month the month for which solved feature requests are to be counted
     * @return the number of solved feature request tickets for the given month
     */
    public int countFeatureSolved(final Month month) {
        return (int) TicketContainer
                .getInstance()
                .getTicketsSolvedInAMonthByDev(month, developer.getUsername())
                .stream()
                .filter(ticket -> ticket.getType() == TicketType.FEATURE_REQUEST)
                .count();
    }

    /**
     * Counts the number of tickets closed by this developer during the specified month.
     * Only tickets solved within the given month and assigned to this developer
     * are taken into account.
     * @param month the month for which closed tickets are to be counted
     * @return the number of tickets closed by the developer in the given month
     */
    public int countClosedTickets(final Month month) {
        return TicketContainer
                .getInstance()
                .getTicketsSolvedInAMonthByDev(month, developer.getUsername())
                .size();
    }

    /**
     * Retrieves the number of {@link Ticket}s that have the priority labeled
     * as "high", meaning the {@link BusinessPriority} surpasses
     * {@code BusinessPriority.HIGH}. All those tickets were already solved by the
     * {@link Developer} assigned.
     * @param month the LocalDate month in which the statistic is being made.
     * @return an integer showing the number of tickets having big priority.
     */
    public int countHighPriorityTickets(final Month month) {
        return (int) TicketContainer
                .getInstance()
                .getTicketsSolvedInAMonthByDev(month, developer.getUsername())
                .stream()
                .filter(ticket -> ticket.getBusinessPriority() == BusinessPriority.HIGH
                        || ticket.getBusinessPriority() == BusinessPriority.CRITICAL)
                .count();
    }

    /**
     * Computes the average time needed for a {@link Developer} to
     * solve tickets during a month specified
     * @param month month in which the analysis is being done
     * @return a real number representing the average resolution time
     * of all tickets registered in a month.
     */
    public double averageResolutionTime(final Month month) {
        List<Ticket> ticketsSolvedDuringMonth = TicketContainer
                .getInstance()
                .getTicketsSolvedInAMonthByDev(month, developer.getUsername());

        if (ticketsSolvedDuringMonth.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (Ticket ticket : ticketsSolvedDuringMonth) {
            int daysDiff = ticket.getDaysToResolve();
            if (daysDiff >= 0) {
                sum += daysDiff;
            }
        }

        double average = (double) sum / ticketsSolvedDuringMonth.size();
        return Math.round(average * NORMALIZATION_FACTOR) / NORMALIZATION_FACTOR;
    }
}
