package main.notification;
import main.milestone.Milestone;

public final class MilestoneCreateNotification extends Notification {
    public MilestoneCreateNotification(final Milestone milestone) {
        super("New milestone " + milestone.getName() + " has been "
                + "created with due date " + milestone.getDueDate().toString() + ".");
        this.receivers = milestone.getAssignedDevs();
    }
}
