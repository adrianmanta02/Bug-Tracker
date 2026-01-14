package main.ticket.actions;
import java.time.LocalDate;

/**
 * main.ticket.actions is a class responsible with creating history
 * objects for a Ticket. Each {@link main.ticket.Ticket} has its own history,
 * created by different actions done on it, or by registering different statuses
 * at the time the application is running.
 */
public final class TicketActionFactory {
    private TicketActionFactory() { }

    /**
     * Builds and returns a {@code TicketAction} representing an assignment event
     * to be recorded in a {@link main.ticket.Ticket}'s history.
     * The action indicates that the ticket has been assigned to the specified
     * {@link main.users.Developer} at the given moment in time.
     * @param username the username of the developer to whom the ticket is assigned
     * @param timestamp the date on which the assignment occurred
     * @return a {@code TicketAction} describing a ticket assignment event
     */
    public static TicketAction assigned(final String username,
                                        final LocalDate timestamp) {
        return new TicketAction.TicketActionBuilder(
                ActionType.ASSIGNED,
                username,
                timestamp
        ).build();
    }

    /**
     * Builds and returns a {@code TicketAction} representing an assignment event
     * to be recorded in a {@link main.ticket.Ticket}'s history.
     * The action indicates that the ticket has been unassigned to the specified
     * {@link main.users.Developer} at the given moment in time.
     * @param username the username of the developer to whom the ticket is unassigned
     * @param timestamp the date on which the unassignment occurred
     * @return a {@code TicketAction} describing a ticket unassignment event
     */
    public static TicketAction deAssigned(final String username,
                                          final LocalDate timestamp) {
        return new TicketAction.TicketActionBuilder(
                ActionType.DE_ASSIGNED,
                username,
                timestamp
        ).build();
    }

    /**
     * Builds and returns a {@code TicketAction} representing a change of status event
     * to be recorded in a {@link main.ticket.Ticket}'s history.
     * The action indicates that the ticket had its status changed.
     * @param username the username of the developer to whom the ticket belongs
     * @param timestamp the date on which the change of status occurred
     * @return a {@code TicketAction} describing a ticket status change event
     */
    public static TicketAction statusChanged(final String username,
                                            final LocalDate timestamp,
                                            final String from,
                                            final String to) {
        return new TicketAction.TicketActionBuilder(
                ActionType.STATUS_CHANGED,
                username,
                timestamp
        ).from(from)
        .to(to)
        .build();
    }

    /**
     * Builds and returns a {@code TicketAction} representing a milestone addition event
     * to be recorded in a {@link main.ticket.Ticket}'s history.
     * The action indicates that the ticket has been added to the specified
     * {@link main.milestone.Milestone} by name.
     * @param username the username of the developer to whom the ticket belongs
     * @param timestamp the date on which the ticket's addition to the milestone occurred.
     * @return a {@code TicketAction} describing a ticket association with a milestone event
     */
    public static TicketAction addedToMilestone(final String username,
                                                final LocalDate timestamp,
                                                final String milestoneName) {
        return new TicketAction.TicketActionBuilder(
                ActionType.ADDED_TO_MILESTONE,
                username,
                timestamp
        ).milestone(milestoneName)
        .build();
    }

    /**
     * Builds and returns a {@code TicketAction} representing a developer ticket
     * removal event to be recorded in a {@link main.ticket.Ticket}'s history.
     * The action indicates that the ticket has been removed from the specified
     * {@link main.users.Developer} at the given moment in time.
     * @param username the username of the developer which has the ticket removed
     * @param timestamp the date on which the removal occurred
     * @return a {@code TicketAction} describing a ticket removal event
     */
    public static TicketAction removedFromDev(final String username,
                                              final LocalDate timestamp,
                                              final String from) {
        return new TicketAction.TicketActionBuilder(
                ActionType.REMOVED_FROM_DEV,
                username,
                timestamp
        ).from(from)
        .build();
    }
}
