package main.milestone;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonGetter;
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

import static main.common.SystemConstants.*;

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
     * Checks if milestone has just became complete and freezes the fields if so.
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
                ? ACTIVE_STATUS : COMPLETED_STATUS;
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
     * @see MilestoneService#getRepartitionForMilestone(Milestone) 
     */
    @JsonGetter("repartition")
    public List<Map<String, Object>> getRepartition() {
        MilestoneService milestoneService = new MilestoneService();
        return milestoneService.getRepartitionForMilestone(this);
    }

    /**
     * Calculates the proportion of tickets that were solved
     * since the Milestone was first launched by the {@link main.users.Manager}.
     * All tickets with a {@code CLOSED} status contribute to the increasing
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
     * their status set on {@code OPEN}. The result is a list with Tickets
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
