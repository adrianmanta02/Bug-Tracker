package main.commands.context;

import lombok.Getter;
import lombok.Setter;
import main.milestone.Milestone;
import main.milestone.MilestoneContainer;
import main.milestone.MilestoneService;
import main.notification.AfterDueDateNotification;
import main.notification.OneDayNotification;
import main.ticket.utils.HistoryManager;
import main.ticket.utils.TicketContainer;

import java.time.LocalDate;

@Getter
@Setter
/*
   main.commands.context.CommandContext represents a containerized helper class for a
   command in order to be run
 */
public final class CommandContext {
    private final TicketContainer ticketContainer;
    private final MilestoneContainer milestoneContainer;
    private final MilestoneService milestoneService;
    private final HistoryManager historyManager;
    private boolean isAppRunning;
    private LocalDate lastTestingPhaseTimestamp;
    private boolean enableTestingPhase;

    public CommandContext(final TicketContainer ticketContainer,
                          final MilestoneContainer milestoneContainer,
                          final HistoryManager historyManager,
                          final LocalDate lastTestingPhaseTimestamp) {
        this.ticketContainer = ticketContainer;
        this.milestoneContainer = milestoneContainer;
        this.milestoneService = new MilestoneService();
        this.lastTestingPhaseTimestamp = lastTestingPhaseTimestamp;
        this.historyManager = historyManager;
        this.enableTestingPhase = true;
        this.isAppRunning = true;
    }

    /**
     * Updates all milestones: checks for state changes (locked/unlocked),
     * applies time-based rules (priority updates, notifications)
     * @param currentTimestamp the current timestamp of the command being executed
     */
    public void updateAllMilestones(final LocalDate currentTimestamp) {
        milestoneService.updateAllMilestones(currentTimestamp);
    }

    /**
     * Sends automatic milestone-related notifications based on the current date.
     * For each non-completed milestone, this method checks whether the current date
     * is exactly one day before or one day after the milestone’s due date and
     * triggers the corresponding notification:
     * One day before the due date, a reminder notification is sent
     * One day after the due date, an overdue notification is sent
     * The actual notification delivery is delegated to the milestone’s current
     * {@link main.milestone.state.MilestoneState}, allowing state-specific behavior.
     *
     * @param currentTimestamp the current date used to determine whether
     *                         notifications should be sent
     */
    public void sendAutomaticNotifications(final LocalDate currentTimestamp) {
        for (Milestone milestone : MilestoneContainer.getInstance().getMilestones()) {
            if (milestone.getStatus().equals("COMPLETED")) {
                continue;
            }

            boolean oneDayBefore = currentTimestamp.equals(milestone.getDueDate().minusDays(1));
            boolean oneDayAfter = currentTimestamp.equals(milestone.getDueDate().plusDays(1));

            if (oneDayBefore) {
                milestone.getState().notifyDevelopers(milestone, new OneDayNotification(milestone));
            }

            if (oneDayAfter) {
                milestone.getState().notifyDevelopers(milestone,
                                                    new AfterDueDateNotification(milestone));
            }
        }
    }

    /**
     * Resets the internal containers, which keep track of
     * {@link main.ticket.Ticket}s and {@link Milestone} during
     * the running time of the application.
     */
    public void reset() {
        ticketContainer.reset();
        milestoneContainer.reset();
    }
}
