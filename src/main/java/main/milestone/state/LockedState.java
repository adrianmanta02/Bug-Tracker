package main.milestone.state;

import main.milestone.Milestone;
import main.milestone.MilestoneService;
import main.notification.AfterDueDateNotification;
import main.notification.Notification;
import main.notification.OneDayNotification;
import main.notification.UnlockNotification;
import main.ticket.Ticket;

import java.time.LocalDate;

public final class LockedState implements MilestoneState {
    /**
     * @see MilestoneState#updateTicketsPriorities(Milestone, LocalDate)
     */
    @Override
    public void updateTicketsPriorities(final Milestone milestone,
                                        final LocalDate timestamp) {
        // Rule 3: No priority updates when locked
    }

    /**
     * @see MilestoneState#markTicketsCritical(Milestone)
     */
    @Override
    public void markTicketsCritical(final Milestone milestone) {
        // Rule 3: No critical marking when locked
    }

    /**
     * @see MilestoneState#notifyDevelopers(Milestone, Notification)
     */
    @Override
    public void notifyDevelopers(final Milestone milestone, final Notification notification) {
        // Rule 3: No notifications when locked
    }

    /**
     * @see MilestoneState#canPickTickets(Milestone)
     */
    @Override
    public boolean canPickTickets(final Milestone milestone) {
        return false;
    }

    /**
     * @see MilestoneState#applyTimeBasedRules(Milestone, boolean, boolean)
     */
    @Override
    public void applyTimeBasedRules(final Milestone milestone,
                                    final boolean oneDayBeforeDue,
                                    final boolean oneDayAfterDue) {
        // Rule 3: No time-based rules when locked
    }

    /**
     * @see MilestoneState#handleUpdate(Milestone, LocalDate, boolean)
     */
    @Override
    public MilestoneState handleUpdate(final Milestone milestone,
                                       final LocalDate currentTimestamp,
                                       final boolean isCurrentlyBlocked) {
        if (isCurrentlyBlocked) {
            return this;
        }

        MilestoneService milestoneService = new MilestoneService();
        Ticket last = milestoneService.findLastClosedTicketFromMilestone(milestone);

        if (last != null) {
            UnlockNotification notification = new UnlockNotification(milestone, last);
            notifyDevelopers(milestone, notification);
        }

        UnlockedState unlockedState = new UnlockedState();
        milestone.setUnlockedAt(currentTimestamp);

        if (currentTimestamp.isAfter(milestone.getDueDate())) {
            // Rule 5: Unlocked after due date - all active tickets become CRITICAL
            unlockedState.markTicketsCritical(milestone);
            unlockedState.notifyDevelopers(milestone,
                    new AfterDueDateNotification(milestone));
        } else if (currentTimestamp.equals(milestone.getDueDate().minusDays(1))) {
            // Rule 4: Unlocked one day before due date - apply rule 2
            unlockedState.markTicketsCritical(milestone);
            unlockedState.notifyDevelopers(milestone,
                    new OneDayNotification(milestone));
        }

        return unlockedState;
    }
}
