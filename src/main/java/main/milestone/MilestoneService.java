package main.milestone;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import main.milestone.state.LockedState;
import main.milestone.state.MilestoneState;
import main.milestone.state.UnlockedState;
import main.notification.MilestoneCreateNotification;
import main.notification.Notification;
import main.notification.NotificationManager;
import main.notification.UnlockNotification;
import main.ticket.Ticket;
import main.ticket.enums.common.TicketStatus;
import main.ticket.utils.TicketContainer;

public final class MilestoneService {
    private final TicketContainer ticketContainer;
    private final MilestoneContainer milestoneContainer;

    public MilestoneService() {
        this.ticketContainer = TicketContainer.getInstance();
        this.milestoneContainer = MilestoneContainer.getInstance();
    }

    /**
     * Finds all Tickets associated with a given Milestone, returning their objects.
     *
     * @param milestone the Milestone to make the query on
     * @return list of Tickets associated with the {@link Milestone}.
     */
    public List<Ticket> getTicketsForMilestone(final Milestone milestone) {
        return ticketContainer.getAllTicketsByIds(milestone.getTickets());
    }

    /**
     * Finds the milestone that contains the specified ticket.
     *
     * @param ticketId the ticket ID to search for
     * @return Milestone object if ticket is part of a milestone, null otherwise
     */
    public Milestone getMilestoneForTicket(final int ticketId) {
        return milestoneContainer.getMilestoneForTicket(ticketId);
    }

    /**
     * Checks if a milestone is blocked by other milestones.
     * A milestone is blocked if there exists another milestone that:
     * - Contains this milestone's name in its blockingFor list
     * - Has ACTIVE status (not all tickets closed)
     *
     * @param milestone the milestone to check
     * @return true if milestone is blocked, false otherwise
     */
    public boolean isMilestoneBlocked(final Milestone milestone) {
        return milestoneContainer.getMilestoneList()
                .stream()
                .anyMatch(m -> m.getBlockingFor().contains(milestone.getName())
                        && m.getStatus().equals("ACTIVE"));
    }

    /**
     * Updates a single milestone: checks state transitions and applies automatic rules.
     * This is the main orchestrator for milestone behavior.
     *
     * @param milestone        the milestone to update
     * @param currentTimestamp current date
     */
    private void updateMilestone(final Milestone milestone,
                                 final LocalDate currentTimestamp) {

        // Initialize state if this is the first update
        if (milestone.getState() == null) {
            boolean isBlocked = isMilestoneBlocked(milestone);
            milestone.setState(isBlocked ? new LockedState() : new UnlockedState());
            milestone.setBlocked(isBlocked);
        }

        // Check current blocking status
        boolean isCurrentlyBlocked = isMilestoneBlocked(milestone);

        // Let the state handle everything: transitions and automatic rules
        MilestoneState newState = milestone.getState().handleUpdate(
                milestone,
                currentTimestamp,
                isCurrentlyBlocked
        );

        // Update milestone with new state and blocking status
        milestone.setState(newState);
        milestone.setBlocked(isCurrentlyBlocked);
    }

    /**
     * Updates all milestones in the system.
     * Should be called before each command execution to apply automatic rules.
     *
     * @param currentTimestamp current date
     */
    public void updateAllMilestones(final LocalDate currentTimestamp) {
        for (Milestone milestone : milestoneContainer.getMilestoneList()) {
            updateMilestone(milestone, currentTimestamp);
        }
    }

    /**
     * Checks if all tickets associated with the current Milestone are resolved.
     *
     * @return a boolean value showing if milestone has all tickets done or not
     */
    public boolean areAllTicketsResolved(final Milestone milestone) {
        List<Ticket> activeAssociatedTickets = ticketContainer
                .getActiveTicketsByIds(milestone.getTickets());
        return activeAssociatedTickets.isEmpty();
    }

    /**
     * Finds the most recently closed ticket associated with the given milestone.
     * Only tickets with status {@code CLOSED} and a non-null resolution timestamp
     * are considered. If multiple tickets match, the one with the latest
     * {@code solvedAt} timestamp is returned.
     *
     * @param milestone the milestone whose tickets are to be searched
     * @return the most recently closed ticket, or {@code null} if no such ticket exists
     */
    public Ticket findLastClosedTicketFromMilestone(final Milestone milestone) {
        return ticketContainer.getAllTicketsByIds(milestone.getTickets())
                .stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.CLOSED)
                .filter(ticket -> ticket.getSolvedAt() != null)
                .max(Comparator.comparing(Ticket::getSolvedAt))
                .orElse(null);
    }

    /**
     * Creates a new {@link Milestone} based on the provided JSON data.
     * This method performs the following steps:
     * Uses {@link MilestoneFactory} to create a milestone instance from the JSON data.
     * Initializes the milestone's state according to its blocking status.
     * Registers the milestone in the system (adds it to the container and
     * notifies assigned developers).
     *
     * @param data a {@link JsonNode} containing the information needed to create the milestone,
     *             including name, assigned developers, tickets, due date, etc.
     * @return the newly created {@link Milestone} instance, fully initialized and registered
     */
    public Milestone createMilestone(final JsonNode data) {
        Milestone milestone = MilestoneFactory.createMilestone(data);
        milestone.initializeState(isMilestoneBlocked(milestone));
        registerMilestone(milestone);
        return milestone;
    }

    /**
     * Adds a milestone in the internal form of storing all milestones
     * and sends a notification to all assigned developers about the instance
     * being created.
     *
     * @param milestone Milestone created
     */
    private void registerMilestone(final Milestone milestone) {
        milestoneContainer.add(milestone);
        notifyDevelopersAboutMilestone(milestone);
    }

    /**
     * Sends a notification to all assigned {@link main.users.Developer}s
     * at a Milestone creation. Each milestone has a list of assigned developers,
     * and they must be informed about the presence of a new milestone.
     *
     * @param milestone milestone instance that has just been created.
     */
    private void notifyDevelopersAboutMilestone(final Milestone milestone) {
        Notification notification = new MilestoneCreateNotification(milestone);

        NotificationManager notificationManager = NotificationManager.getInstance();

        milestone.getAssignedDevs()
                .forEach(devUsername ->
                        notificationManager.addNotificationToUser(notification, devUsername)
                );
    }

    /**
     * Retrieves a milestone which has a conflict with other milestones
     * in terms of owning tickets. By default, a ticket should only be assigned
     * to a milestone at one time. This method ensures validation, being used
     * in {@link main.commands.milestonecommands.CreateMilestoneCommand}.
     * @param ticketIds the list of ticket ids to be checked
     * @return the problematic {@link Milestone} if found, otherwise null.
     */
    public Optional<Milestone> getConflctingMilestone(final List<Integer> ticketIds) {
        return this.milestoneContainer
                .getMilestoneList()
                .stream()
                .filter(milestone -> milestone.getTickets()
                        .stream()
                        .anyMatch(ticketIds::contains))
                .findFirst();
    }

    /**
     * Checks whether the milestone that contains the given closed ticket
     * should transition into a frozen state.
     * The method identifies the milestone associated with the provided ticket
     * and delegates the freezing decision to the milestone itself via
     * {@link Milestone#checkAndFreeze(LocalDate)}.
     * @param closedTicket the ticket that has been closed
     * @param timestamp the date at which the ticket was closed, used to evaluate
     *                  milestone freezing conditions
     */
    public void checkForMilestoneFreezing(final Ticket closedTicket,
                                          final LocalDate timestamp) {
        Milestone milestoneContainingTicket = milestoneContainer
                .getMilestoneForTicket(closedTicket.getId());

        milestoneContainingTicket.checkAndFreeze(timestamp);
    }

    /**
     * Handles milestone completion logic after a ticket is closed.
     * This method checks whether the milestone containing the given ticket
     * has all of its tickets resolved. If so, it identifies all milestones
     * that were blocked by the completed milestone and notifies their
     * assigned developers that the blocking condition has been removed.
     * Notifications are sent individually to each developer assigned to
     * the newly unblocked milestones.
     * @param ticketId the identifier of the ticket that has just been closed
     */
    public void handleMilestoneCompletionAfterTicketClosing(final int ticketId,
                                                            final LocalDate timestamp) {
        Milestone milestoneToCheck = this.getMilestoneForTicket(ticketId);
        Ticket ticket = this.ticketContainer.getTicketById(ticketId);

        if (milestoneToCheck.areAllTicketsResolved()) {
            if (!timestamp.isAfter(milestoneToCheck.getDueDate())) {
                List<Milestone> blockedMilestones = this.milestoneContainer
                        .getBlockedMilestonesBy(milestoneToCheck);

                for (Milestone milestone : blockedMilestones) {
                    List<String> assignedDevsNames = milestone.getAssignedDevs();

                    for (String devName : assignedDevsNames) {
                        NotificationManager.getInstance()
                                .addNotificationToUser(
                                        new UnlockNotification(milestone, ticket),
                                        devName
                                );
                    }
                }
            }
        }
    }


    /**
     * Unfreezes a milestone when a ticket transitions back to an active state.
     * This method is triggered when a ticket changes status. If the ticket
     * was previously considered resolved (either {@code CLOSED} or {@code RESOLVED})
     * and is reverted to an active state ({@code IN_PROGRESS} or {@code OPEN}),
     * the milestone containing that ticket is checked.
     * If the milestone is currently frozen, its frozen state is lifted and
     * its completion timestamp is cleared, allowing the milestone to resume
     * normal updates.
     * Only the first milestone containing the given ticket is affected.
     * @param ticketId the identifier of the ticket whose status has changed
     * @param previousStatus the ticket's status before the change
     * @param currentStatus the ticket's status after the change
     */
    public void unfreezeMilestoneByTicketStatus(final int ticketId,
                                                final TicketStatus previousStatus,
                                                final TicketStatus currentStatus) {
        if ((currentStatus == TicketStatus.CLOSED
                || currentStatus == TicketStatus.RESOLVED)
                && (previousStatus == TicketStatus.IN_PROGRESS
                    || previousStatus == TicketStatus.OPEN)) {
            for (Milestone milestone : milestoneContainer.getMilestoneList()) {
                if (milestone.getTickets().contains(ticketId)) {
                    if (milestone.isFrozenFields()) {
                        milestone.setFrozenFields(false);
                        milestone.setCompletedAt(null);
                    }
                    break;
                }
            }
        }
    }
}
