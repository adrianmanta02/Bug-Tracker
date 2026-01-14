package main.ticket.utils;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.ticket.enums.common.BusinessPriority;
import main.ticket.enums.common.TicketStatus;
import main.ticket.enums.common.TicketType;

@Getter
public final class TicketContainer {
    private List<Ticket> allTickets = new ArrayList<>();
    private static TicketContainer instance;
    private int nextId = 0;

    /**
     * Returns the Singleton instance of the container.
     * The instance is lazily initialized on the first invocation of this
     * method.
     * @return the singleton {@code TicketContainer} instance
     */
    public static TicketContainer getInstance() {
        if (instance == null) {
            instance = new TicketContainer();
        }
        return instance;
    }

    public int getNextId() {
        return this.nextId++;
    }

    /**
     * Adds a ticket to the Container.
     * @param ticket Ticket instance to add.
     */
    public void addTicket(final Ticket ticket) {
        this.allTickets.add(ticket);
    }

    /**
     * Queries a ticket by its unique id.
     * @param id integer representing id
     * @return Ticket instance associated to the id.
     */
    public Ticket getTicketById(final int id) {
        return allTickets.stream()
                .filter(ticket -> ticket.getId() == id)
                .findFirst()
                .orElse(null);  // Return null instead of throwing
    }

    /**
     * Retrieves all tickets corresponding to the given list of identifiers.
     * The returned list preserves the order of the provided identifiers.
     * @param ids the list of ticket identifiers to retrieve
     * @return a list of tickets matching the given identifiers
     */
    public List<Ticket> getAllTicketsByIds(final List<Integer> ids) {
        return ids.stream().map(this::getTicketById).toList();
    }

    /**
     * Filters the Tickets having {@link TicketStatus} on OPEN, IN_PROGRESS, or RESOLVED.
     * Only CLOSED tickets are excluded from active tickets.
     * @param ids list of Integers representing the ids for a bunch of Tickets.
     * @return list of Tickets.
     */
    public List<Ticket> getActiveTicketsByIds(final List<Integer> ids) {
        return getAllTicketsByIds(ids)
                .stream()
                .filter(ticket -> ticket.getStatus() != TicketStatus.CLOSED)
                .toList();
    }

    /**
     * Filters the Tickets assigned to a developer, by their username
     * @param developerName username of the Developer that has to solve the Tickets
     * @return list of Tickets.
     */
    public List<Ticket> getTicketsAssignedTo(final String developerName) {
        return getAllTickets()
                .stream()
                .filter(ticket -> ticket.getAssignedTo().equals(developerName))
                .toList();
    }

    /**
     * Filters all tickets solved in a month specified, by a certain developer
     * @param month month in which the tickets were solved.
     * @param developerName the name(String) of the Developer solving the tickets
     * @return a list of Tickets.
     */
    public List<Ticket> getTicketsSolvedInAMonthByDev(final Month month,
                                                      final String developerName) {
        return this.allTickets
                .stream()
                .filter(ticket -> !ticket.getSolvedAt().isEmpty())
                .filter(ticket -> LocalDate.parse(ticket.getSolvedAt()).getMonth() == month)
                .filter(ticket -> developerName.equals(ticket.getAssignedTo()))
                .filter(ticket -> ticket.getStatus() == TicketStatus.CLOSED)
                .toList();
    }

    /**
     * @return a list of filtered Tickets, by an OPEN or IN_PROGRESS status.
     */
    public List<Ticket> getUnsolvedTickets() {
        return this.allTickets
                .stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.OPEN
                                || ticket.getStatus() == TicketStatus.IN_PROGRESS
                        )
                .toList();
    }
    /**
     * @return a list of filtered Tickets, by a CLOSED or RESOLVED status.
     */
    public List<Ticket> getSolvedAndClosedTickets() {
        return this.allTickets
                .stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.CLOSED
                            || ticket.getStatus() == TicketStatus.RESOLVED)
                .toList();
    }

    /**
     * Counts a Tickets having certain type in an already filtered list.
     * @param ticketList filtered list of {@link Ticket}
     * @param ticketType the {@link TicketType} used for further filter.
     * @return an integer representing the number of tickets having that type.
     */
    public int countTicketsByType(final List<Ticket> ticketList,
                                  final TicketType ticketType) {
        return (int) ticketList
                .stream()
                .filter(ticket -> ticket.getType() == ticketType)
                .count();
    }

    /**
     * Counts a Tickets having certain priority in an already filtered list.
     * @param ticketList filtered list of {@link Ticket}
     * @param priority the {@link BusinessPriority} used for further filter.
     * @return an integer representing the number of tickets having that priority.
     */
    public int countTicketsByPriority(final List<Ticket> ticketList,
                                      final BusinessPriority priority) {
        return (int) ticketList
                .stream()
                .filter(ticket -> ticket.getBusinessPriority() == priority)
                .count();
    }

    /**
     * Retrieves all open tickets associated with the given list of milestones.
     * This method collects all ticket identifiers from the provided milestones,
     * resolves them to {@link Ticket} objects, and filters the result to include
     * only tickets whose status is {@code OPEN}.
     * @param milestones the list of milestones whose associated open tickets
     *                   are to be retrieved
     * @return a list of {@link Ticket} objects that are in {@code OPEN} status
     *         and belong to the specified milestones
     */
    public List<Ticket> getOpenTicketsFromMilestones(final List<Milestone> milestones) {
        List<Integer> ticketIds = milestones.stream()
                .flatMap(milestone -> milestone.getTickets().stream())
                .toList();

        return this
                .getAllTicketsByIds(ticketIds)
                .stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.OPEN)
                .toList();
    }

    /**
     * Retrieves all tickets that are reported during a
     * testing phase by a {@link main.users.Reporter}.
     * @param reporterName the name of the {@link main.users.User}
     *                     whose role is {@link main.users.enums.common.Role#REPORTER}
     * @return a list of {@link Ticket}s that were reported by the
     * user during the testing phase
     */
    public List<Ticket> getTicketsReportedBy(final String reporterName) {
        return this.getAllTickets()
                .stream()
                .filter(ticket -> ticket.getReportedBy()
                                                .equals(reporterName))
                .toList();
    }

    /**
     * Resets the container's usable list.
     */
    public void reset() {
        this.allTickets.clear();
        this.nextId = 0;
    }
}
