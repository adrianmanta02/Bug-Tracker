package main.commands.query.strategies.viewhistory;

import main.commands.ticket.TicketHistoryDTO;
import main.milestone.Milestone;
import main.milestone.MilestoneContainer;
import main.ticket.Ticket;
import main.ticket.actions.TicketAction;
import main.ticket.utils.HistoryManager;
import main.ticket.utils.TicketContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * main.commands.strategies.view.viewhistory.TicketHistoryService is a class that hides
 * business logic in terms of retrieving data related to tickets' histories
 * for different user types.
 */
public final class TicketHistoryService {
    private final HistoryManager historyManager;
    private final TicketContainer ticketContainer;
    private final MilestoneContainer milestoneContainer;

    public TicketHistoryService(final HistoryManager historyManager,
                                final TicketContainer ticketContainer,
                                final MilestoneContainer milestoneContainer) {
        this.historyManager = historyManager;
        this.ticketContainer = ticketContainer;
        this.milestoneContainer = milestoneContainer;
    }

    /**
     * Retrieves the history of all tickets that were previously assigned to
     * the specified developer.
     * For each relevant ticket, the history is collected up to (and including)
     * the point at which the ticket is de-assigned from the developer.
     * @param developerName the username of the developer whose past ticket
     *                      assignment history is to be retrieved
     * @return a list of {@link TicketHistoryDTO} objects containing ticket
     *         history information for the specified developer
     */
    public List<TicketHistoryDTO> getDeveloperTicketHistory(final String developerName) {
        List<TicketHistoryDTO> ticketsHistory = new ArrayList<>();

        List<Ticket> ticketsAssignedToDev = ticketContainer
                .getAllTickets()
                .stream()
                .filter(ticket -> historyManager
                        .wasTicketAssignedTo(developerName, ticket.getId()))
                .toList();


        for (Ticket ticket : ticketsAssignedToDev) {
            List<TicketAction> ticketLocalHistory = historyManager
                    .getHistoryUntilDeassignment(ticket.getId());
            ticketsHistory.add(new TicketHistoryDTO(ticket, ticketLocalHistory));
        }

        return ticketsHistory;
    }

    /**
     * Retrieves the history of all tickets associated with milestones created
     * by the specified manager.
     * For each milestone created by the manager, all linked tickets are collected
     * and their complete action history is included in the result.
     * @param managerName the username of the manager whose milestone-related
     *                    ticket histories are to be retrieved
     * @return a list of {@link TicketHistoryDTO} objects containing full history
     *         information for all tickets associated with the manager's milestones
     */
    public List<TicketHistoryDTO> getManagerTicketHistory(final String managerName) {
        List<Milestone> managerMilestones = milestoneContainer.getMilestonesCreatedBy(managerName);

        return  managerMilestones.stream()
                .flatMap(milestone -> milestone.getTickets().stream())
                .map(ticketContainer::getTicketById)
                .map(ticket -> new TicketHistoryDTO(ticket, historyManager
                        .getTicketHistory(ticket.getId())))
                .toList();
    }
}
