package main.milestone.state;

import main.milestone.Milestone;
import main.notification.Notification;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;
import main.ticket.enums.common.BusinessPriority;
import main.users.User;
import main.users.UserContainer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static main.common.SystemConstants.THREE_DAY_INTERVAL;

public final class UnlockedState implements MilestoneState {
    private final TicketContainer ticketRepository = TicketContainer.getInstance();

    /**
     * @see MilestoneState#updateTicketsPriorities(Milestone, LocalDate)
     */
    @Override
    public void updateTicketsPriorities(final Milestone milestone,
                                        final LocalDate timestamp) {
        List<Ticket> ticketList = ticketRepository.getActiveTicketsByIds(milestone.getTickets());

        for (Ticket ticket : ticketList) {
            BusinessPriority oldPriority = ticket.getBusinessPriority();
            BusinessPriority newPriority = oldPriority.next();

            ticket.setBusinessPriority(newPriority);
        }
    }

    /**
     * @see MilestoneState#markTicketsCritical(Milestone)
     */
    @Override
    public void markTicketsCritical(final Milestone milestone) {
        List<Ticket> ticketList = ticketRepository.getActiveTicketsByIds(milestone.getTickets());
        ticketList.forEach(ticket -> {
            ticket.setBusinessPriority(BusinessPriority.CRITICAL);
        });
    }

    /**
     * @see MilestoneState#notifyDevelopers(Milestone, Notification)
     */
    @Override
    public void notifyDevelopers(final Milestone milestone, final Notification notification) {
        UserContainer container = UserContainer.getInstance();
        List<User> devs = container.getUserList();

        milestone.getAssignedDevs()
                .forEach(devName -> {
                    devs.forEach(developer -> {
                        if (developer.getUsername().equals(devName)) {
                            developer.update(notification);
                        }
                    });
                });
    }

    /**
     * @see MilestoneState#canPickTickets(Milestone)
     */
    @Override
    public boolean canPickTickets(final Milestone milestone) {
        return true;
    }

    /**
     * @see MilestoneState#applyTimeBasedRules(Milestone, boolean, boolean)
     */
    @Override
    public void applyTimeBasedRules(final Milestone milestone,
                                    final boolean oneDayBeforeDue,
                                    final boolean oneDayAfterDue) {
        if (oneDayBeforeDue) {
            markTicketsCritical(milestone);
        }

        if (oneDayAfterDue) {
            markTicketsCritical(milestone);
        }
    }

    /**
     * Handles the updates, including the state of the milestone. If blocked,
     * the Milestone will not encounter any updates. Otherwise, there are regular
     * rules applied for Tickets inside the Milestone, including
     * sending messages to Developers if needed.
     * @param milestone the milestone being updated
     * @param currentTimestamp current date
     * @param isCurrentlyBlocked whether the milestone is currently blocked by others
     * @return a new MilestoneState - Locked or Unlocked, depending on if the state has
     * changed
     */
    @Override
    public MilestoneState handleUpdate(final Milestone milestone,
                                       final LocalDate currentTimestamp,
                                       final boolean isCurrentlyBlocked) {
        if (isCurrentlyBlocked) {
            return new LockedState();
        }

        applyAutomaticRules(milestone, currentTimestamp);
        return this;
    }

    /**
     * Applies automatic time-based rules for unlocked milestones.
     * Rule 1: Every 3 days, increase priorities for all Tickets in Milestone.
     * Rule 2: One day before/after due date, mark critical and notify developers.
     * @param milestone Milestone object to perform updates on
     * @param currentTimestamp current LocalDate time on updates being made.
     */

    private void applyAutomaticRules(final Milestone milestone,
                                     final LocalDate currentTimestamp) {
        // Only apply updates if milestone has been unlocked
        LocalDate unlockedDate = milestone.getUnlockedAt();
        if (unlockedDate == null) {
            // Milestone was never locked, so it's been unlocked since creation
            unlockedDate = milestone.getCreatedAt();
        }

        long daysSinceUnlock = ChronoUnit.DAYS.between(unlockedDate, currentTimestamp);

        // Determine the last day we updated (relative to unlock date)
        LocalDate lastUpdateDate = milestone.getLastPriorityUpdateDate();
        long lastUpdatedDaySinceUnlock = 0;

        if (lastUpdateDate != null && lastUpdateDate.isAfter(unlockedDate)) {
            lastUpdatedDaySinceUnlock = ChronoUnit.DAYS.between(unlockedDate, lastUpdateDate);
        }

        // Check if we're one day before or after due date FIRST
        boolean oneDayBefore = currentTimestamp.equals(milestone.getDueDate().minusDays(1));
        boolean oneDayAfter = currentTimestamp.equals(milestone.getDueDate().plusDays(1));

        // Apply all missed 3-day interval updates SINCE UNLOCK (not since creation!)
        for (long day = lastUpdatedDaySinceUnlock + 1; day <= daysSinceUnlock; day++) {
            if (day % THREE_DAY_INTERVAL == 0) {
                LocalDate updateDate = unlockedDate.plusDays(day);

                // Skip 3-day update if this date is one day before due date
                if (updateDate.equals(milestone.getDueDate().minusDays(1))) {
                    continue;
                }

                updateTicketsPriorities(milestone, updateDate);
            }
        }

        // Update the last priority update date
        if (lastUpdatedDaySinceUnlock < daysSinceUnlock) {
            milestone.setLastPriorityUpdateDate(currentTimestamp);
        }

        // Now apply one-day-before/after rules (which set everything to CRITICAL)
        if (oneDayBefore || oneDayAfter) {
            applyTimeBasedRules(milestone, oneDayBefore, oneDayAfter);
        }
    }
}
