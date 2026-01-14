package main.ticket.utils;

import lombok.Getter;
import main.ticket.actions.ActionType;
import main.ticket.actions.TicketAction;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * main.ticket.utils.HistoryManager is a class responsible with mapping a
 * ticket id with its own history of actions occurred during the running
 * time of the application.
 */
@Getter
public final class HistoryManager {
    private Map<Integer, List<TicketAction>> history = new HashMap<>();
    private static HistoryManager instance;

    private HistoryManager() { }

    /**
     * Returns the Singleton instance responsible with managing a
     * {@link main.ticket.Ticket}'s history.
     * The instance is lazily initialized on the first invocation of this
     * method.
     * @return the singleton {@code HistoryManager} instance
     */
    public static HistoryManager getInstance() {
        if (instance == null) {
            instance = new HistoryManager();
        }
        return instance;
    }

    /**
     * Adds an {@link TicketAction} event occurred to a ticket's history
     * of past actions. The entire list contains all registered events
     * during the running time of the application.
     * @param ticketId the unique identifier of the {@link main.ticket.Ticket}.
     * @param action {@link TicketAction} history-event action to be added
     */
    public void addToTicketHistory(final int ticketId, final TicketAction action) {
        history.computeIfAbsent(ticketId, id -> new ArrayList<>())
                .add(action);
    }

    /**
     * Resets the internal form of storage in terms of history.
     */
    public void reset() {
        history.clear();
    }

    /**
     * Retrieves the last status a {@link main.ticket.Ticket} had
     * before a {@link ActionType#STATUS_CHANGED} was registered inside
     * ticket's history.
     * @param ticketId the unique identifier of the ticket to whom
     *                 the last status before changing is to be retrieved
     * @return a String value describing Ticket's last status
     */
    public String getLatestHistoryStatusForTicket(final int ticketId) {
        List<TicketAction> localTicketHistory = this.history.get(ticketId);
        return localTicketHistory
                .reversed()
                .stream()
                .filter(ticketAction ->
                        ticketAction.getAction().equals(ActionType.STATUS_CHANGED)
                )
                .findFirst()
                .map(TicketAction::getTo)
                .orElse(TicketContainer
                        .getInstance()
                        .getTicketById(ticketId)
                        .getStatus()
                        .toString()
                );
    }

    /**
     * Verifies if a {@link main.ticket.Ticket} was ever assigned to a {@link main.users.Developer}
     * during the application's running time.
     * It searches across ticket's history for the event describing an assignment
     * to a certain developer
     * @param developerName the name of the developer to which
     *                      the assignment is queried.
     * @param ticketId the unique identifier of the ticket that
     *                 is being checked
     * @return a boolean value being {@code true} if the ticket has ever been
     * assigned to the developer given, or {@code false} otherwise.
     */
    public boolean wasTicketAssignedTo(final String developerName,
                                       final int ticketId) {
        List<TicketAction> localTicketHistory = this.history.get(ticketId);
        return localTicketHistory
                .stream()
                .anyMatch(ticketAction ->
                        ticketAction.getAction() == ActionType.ASSIGNED
                                && ticketAction.getBy().equals(developerName));
    }

    /**
     * Retrieves the most recent {@link TicketAction} representing a status change
     * performed by the specified user on the given ticket.
     * @param ticketId the identifier of the ticket whose history is inspected
     * @param username the username of the user who performed the status change
     * @return an {@link Optional} containing the last matching status change action,
     *         or {@code Optional.empty()} if no such action exists
     */
    public Optional<TicketAction> getLastStatusChange(final int ticketId, final String username) {
        List<TicketAction> localTicketHistory = this.history.get(ticketId);
        return localTicketHistory.stream()
                .filter(action -> action.getAction() == ActionType.STATUS_CHANGED)
                .filter(action -> action.getBy().equals(username))
                .reduce((first, second) -> second);
    }

    /**
     * Searches across {@code HistoryManager}'s internal form of storing tickets'
     * histories and retrieves the one matching a given identifier.
     * @param ticketId the identifier of the ticket whose history is to be retrieved.
     * @return a list of {@link TicketAction} events that took place in the past.
     */
    public List<TicketAction> getTicketHistory(final int ticketId) {
        return this.history.get(ticketId);
    }

    /**
     * Retrieves the history of actions for a ticket up to and including
     * the moment it is de-assigned.
     * The returned list preserves the chronological order of actions and
     * stops once a {@link ActionType#DE_ASSIGNED} action is encountered.
     * @param ticketId the identifier of the ticket whose history is requested
     * @return a list of {@link TicketAction} objects representing the ticket's
     *         history until de-assignment; an empty list if no history exists
     */
    public List<TicketAction> getHistoryUntilDeassignment(final int ticketId) {
        List<TicketAction> pastTicketActions = this.getTicketHistory(ticketId);
        List<TicketAction> ticketLocalHistory = new ArrayList<>();
        for (TicketAction ticketAction : pastTicketActions) {
            ticketLocalHistory.add(ticketAction);
            if (ticketAction.getAction() == ActionType.DE_ASSIGNED) {
                break;
            }
        }
        return ticketLocalHistory;
    }
}
