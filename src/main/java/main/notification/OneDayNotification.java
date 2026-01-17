package main.notification;

import main.milestone.Milestone;

public final class OneDayNotification extends Notification {
    public OneDayNotification(final Milestone milestone) {
        super("Milestone " + milestone.getName() + " is due tomorrow."
                                + " All unresolved tickets are now CRITICAL.");
        this.receivers = milestone.getAssignedDevs();
    }
}
