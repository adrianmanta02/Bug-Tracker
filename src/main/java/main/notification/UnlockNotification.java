package main.notification;

import main.milestone.Milestone;
import main.ticket.Ticket;

public final class UnlockNotification extends Notification {
    public UnlockNotification(final Milestone milestone, final Ticket ticket) {
        super("Milestone " + milestone.getName() + " is now unblocked as ticket "
                + ticket.getId() + " has been CLOSED.");
        this.receivers = milestone.getAssignedDevs();
    }
}
