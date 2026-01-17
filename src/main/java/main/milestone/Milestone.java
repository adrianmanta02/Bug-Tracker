package main.milestone;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import main.milestone.state.LockedState;
import main.milestone.state.MilestoneState;
import main.milestone.state.UnlockedState;
import main.parsing.ParserUtils;
import main.ticket.Ticket;
import main.ticket.enums.common.TicketStatus;
import main.ticket.utils.TicketContainer;

import static main.common.SystemConstants.NORMALIZATION_FACTOR;

@Getter
@Setter
@JsonPropertyOrder({
        "name",
        "blockingFor",
        "dueDate",
        "createdAt",
        "tickets",
        "assignedDevs",
        "createdBy",
        "status",
        "isBlocked",
        "daysUntilDue",
        "overdueBy",
        "openTickets",
        "closedTickets",
        "completionPercentage",
        "repartition"
})
public final class Milestone {
    private String name;
    private List<String> blockingFor;
    private LocalDate dueDate;
    private LocalDate createdAt;

    private List<Integer> tickets;
    private List<String> assignedDevs;
    private String createdBy;
    private int daysUntilDue;
    private int overdueBy;
    private List<Integer> openTickets;
    private List<Integer> closedTickets;
    private boolean isBlocked;
    @JsonIgnore
    private LocalDate lastPriorityUpdateDate;

    @JsonIgnore
    private LocalDate unlockedAt;

    @JsonIgnore
    private MilestoneState state;

    @JsonIgnore
    private LocalDate completedAt;

    @JsonIgnore
    private boolean frozenFields = false;

    public Milestone(final String name, final List<String> blockingFor,
                     final LocalDate dueDate, final List<Integer> tickets,
                     final List<String> assignedDevs, final String createdBy,
                     final LocalDate createdAt) {
        this.name = name;
        this.blockingFor = blockingFor;
        this.dueDate = dueDate;
        this.tickets = tickets;
        this.assignedDevs = assignedDevs;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    /**
     * Updates all tickets' priorities once few days. A milestone contains multiple tickets.
     * @param timestamp current timestamp
     */
    public void updateTicketsPriorities(final LocalDate timestamp) {
        this.state.updateTicketsPriorities(this, timestamp);
    }

    /**
     * Checks if milestone just became complete and freezes the fields if so.
     * Should be called whenever a ticket status changes to CLOSED.
     * @param timestamp the timestamp when the last ticket was closed
     */
    public void checkAndFreeze(final LocalDate timestamp) {
        if (!frozenFields && areAllTicketsResolved()) {
            this.completedAt = timestamp;
            this.frozenFields = true;
        }
    }

    /**
     * Checks if all tickets associated with the current Milestone are resolved.
     * @return a boolean value showing if milestone has all tickets done or not
     */
    public boolean areAllTicketsResolved() {
        List<Ticket> resolvedTickets = TicketContainer
                .getInstance()
                .getAllTicketsByIds(this.tickets)
                .stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.CLOSED)
                .toList();

        return resolvedTickets.size() == tickets.size();
    }

    /**
     * Checks if the milestone is assigned to a certain developer
     * @param developerName the name of the developer
     */
    public boolean isAssignedTo(final String developerName) {
        return this.assignedDevs.contains(developerName);
    }

    /**
     * Establishes an overall status for the milestone, considering
     * the status of the Tickets released for current Milestone.
     * @return a String value representing {@code Milestone}'s status.
     */
    public String getStatus() {
        MilestoneService milestoneService = new MilestoneService();
        List<Ticket> ticketsAssociated = milestoneService.getTicketsForMilestone(this);
        return ticketsAssociated
                .stream()
                .anyMatch(ticket -> ticket.getStatus() != TicketStatus.CLOSED)
                ? "ACTIVE" : "COMPLETED";
    }

    /**
     * Checks if this milestone is blocked by other milestones.
     * This field is updated by MilestoneService.
     * @return {@code true} if milestone is blocked, {@code false} otherwise
     */
    public boolean getIsBlocked() {
        return this.isBlocked;
    }

    /**
     * Computes the distribution of tickets among the developers assigned to this milestone.
     * For each assigned developer, this method collects the identifiers of the tickets
     * currently assigned to them. Only tickets belonging to this milestone and having
     * a valid assigned developer are considered.
     * The resulting list is sorted by the number of assigned tickets in ascending order.
     * If two developers have the same number of assigned tickets, they are ordered
     * alphabetically by username.
     *
     * @return a list of maps describing the ticket repartition per developer;
     *         each map contains:
     * {@code developer} the developer's username
     * {@code assignedTickets} a list of ticket identifiers
     */
    public List<Map<String, Object>> getRepartition() {
        Map<String, List<Integer>> devToTickets = new HashMap<>();
        assignedDevs.forEach(developerName -> devToTickets.put(developerName, new ArrayList<>()));

        MilestoneService milestoneService = new MilestoneService();
        List<Ticket> ticketsAssociated = milestoneService.getTicketsForMilestone(this);

        ticketsAssociated.forEach(ticket -> {
            final String assignedTo = ticket.getAssignedTo();
            if (assignedTo != null && devToTickets.containsKey(assignedTo)) {
                devToTickets.get(assignedTo).add(ticket.getId());
            }
        });

        return devToTickets.entrySet().stream()
                .sorted(Comparator
                        .comparingInt((Map.Entry<String, List<Integer>> e) -> e.getValue().size())
                        .thenComparing(Map.Entry::getKey))
                .map(entry -> {
                    Map<String, Object> repartitionEntry = new LinkedHashMap<>();
                    repartitionEntry.put("developer", entry.getKey());
                    repartitionEntry.put("assignedTickets", entry.getValue());
                    return repartitionEntry;
                })
                .toList();
    }

    /**
     * Calculates the proportion of tickets that were solved
     * since the Milestone was first launched by the {@link main.users.Manager}.
     * All tickets with a CLOSED status contribute to the increasing
     * of the percentage.
     * @return a double value representing the completion percentage;
     */
    public double getCompletionPercentage() {
        MilestoneService milestoneService = new MilestoneService();
        List<Ticket> ticketsAssociated = milestoneService.getTicketsForMilestone(this);
        long ticketsClosed = ticketsAssociated
                .stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.CLOSED)
                .count();

        final long allTickets = ticketsAssociated.size();
        return Math.round((ticketsClosed * 1.0 / allTickets) * NORMALIZATION_FACTOR)
                                                / NORMALIZATION_FACTOR;
    }

    /**
     * Initializes the state for a newly created Milestone
     * @param isBlockedFlag boolean value checking the milestone for being blocked
     */
    public void initializeState(final boolean isBlockedFlag) {
        if (isBlockedFlag) {
            this.state = new LockedState();
        } else {
            this.state = new UnlockedState();
        }
    }

    /**
     * Method used to facilitate the serialization. Computes
     * the opened and closed tickets, ensuring other fields are not
     * being updated if the milestone was completed (all Tickets associated
     * were solved until Milestone's due date).
     * @param timestamp timestamp at which the method is being called
     */
    public void calculateFields(final LocalDate timestamp) {
        MilestoneService milestoneService = new MilestoneService();
        List<Ticket> ticketsAssociated = milestoneService.getTicketsForMilestone(this);

        this.openTickets = ticketsAssociated.stream()
                .filter(ticket -> ticket.getStatus() != TicketStatus.CLOSED)
                .map(Ticket::getId)
                .toList();

        this.closedTickets = ticketsAssociated.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.CLOSED)
                .map(Ticket::getId)
                .toList();

        // Use completedAt if fields are frozen, otherwise use current timestamp
        LocalDate referenceDate = frozenFields ? completedAt : timestamp;

        this.daysUntilDue = dueDate.isAfter(referenceDate)
                ? ParserUtils.dayDifference(referenceDate, dueDate)
                : 0;

        this.overdueBy = dueDate.isBefore(referenceDate)
                ? ParserUtils.dayDifference(dueDate, referenceDate)
                : 0;
    }

    /**
     * Retrieves all Tickets contained by the Milestone which have
     * their status set on OPEN. The result is a list with Tickets
     * being in the same order they were assigned in the milestone,
     * having been filtered by {@link TicketStatus}
     * @return list of filtered tickets by status.
     */
    @JsonIgnore
    public List<Ticket> getOpenedTickets() {
        MilestoneService milestoneService = new MilestoneService();
        List<Ticket> ticketsAssociated = milestoneService.getTicketsForMilestone(this);
        return ticketsAssociated.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.OPEN)
                .toList();
    }
}
