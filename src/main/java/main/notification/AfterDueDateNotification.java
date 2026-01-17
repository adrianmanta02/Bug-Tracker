package main.notification;

import main.milestone.Milestone;

public final class AfterDueDateNotification extends Notification {
    public AfterDueDateNotification(final Milestone milestone) {
        super("Milestone " + milestone.getName() + " was unblocked after due date."
                                + " All active tickets are now CRITICAL.");
        this.receivers = milestone.getAssignedDevs();
    }
}
