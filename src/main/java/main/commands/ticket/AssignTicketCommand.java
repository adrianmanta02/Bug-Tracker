package main.commands.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.milestone.Milestone;
import main.milestone.MilestoneService;
import main.ticket.Ticket;
import main.ticket.actions.TicketActionFactory;
import main.ticket.enums.common.TicketStatus;
import main.ticket.utils.HistoryManager;
import main.ticket.utils.TicketAssignmentService;
import main.users.Developer;
import main.users.User;
import main.users.enums.common.Role;

import java.util.List;

public final class AssignTicketCommand extends Command {
    private final int ticketId;
    public AssignTicketCommand(final JsonNode node) {
        super(node);
        this.ticketId = node.get("ticketID").asInt();
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    /**
     * Assigns a ticket to a given {@link Developer}, marking the change
     * of state in the Ticket's history too.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return null, meaning the command has no output
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        Ticket ticket = commandContext.getTicketContainer().getTicketById(ticketId);
        HistoryManager historyManager = HistoryManager.getInstance();

        TicketStatus oldStatus = ticket.getStatus();
        ticket.assign(this.getInvoker().getUsername(), timestamp);


        historyManager.addToTicketHistory(ticketId,
                                    TicketActionFactory.assigned(this.username,
                                                                this.timestamp)
                );

        if (oldStatus != ticket.getStatus()) {
            historyManager.addToTicketHistory(ticketId,
                    TicketActionFactory.statusChanged(this.username,
                                                    this.timestamp,
                                                    oldStatus.toString(),
                                                    ticket.getStatus().toString())
            );
        }
        return null;
    }

    /**
     * Does a set of Ticket-property validations, related to the expertise level
     * and the {@link main.users.enums.developer.Seniority} of a Developer
     * needed to have in order to be able to assign the ticket. Checks the
     * association between the current developer, the invoker of the command, and
     * the Milestone the ticket belongs to. A ticket assigned must also come from a
     * non-blocking milestone.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the result of validating
     * the command, with a distinct error message based on the validation requirement
     * not met by the Command
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        final String errorMessage;
        final Ticket ticket = commandContext.getTicketContainer().getTicketById(ticketId);
        final User user = this.getInvoker();

        // 1. Expertise validation
        if (!this.getInvoker().checkAreas(ticket)) {
            List<String> required = TicketAssignmentService
                    .getCompatibleDevSpecializationNames(ticket);

            errorMessage = "Developer "
                    + user.getUsername()
                    + " cannot assign ticket "
                    + ticketId
                    + " due to expertise area. Required: "
                    + String.join(", ", required)
                    + "; Current: "
                    + ((Developer) user).getExpertiseArea().name()
                    + ".";

            return buildErrorResult(errorMessage);
        }

        // 2. Seniority validation
        if (!user.canAccess(ticket)) {
            List<String> required = TicketAssignmentService
                    .getRequiredSeniorityLevelNames(ticket);

            errorMessage = "Developer "
                    + user.getUsername()
                    + " cannot assign ticket "
                    + ticketId
                    + " due to seniority level. Required: "
                    + String.join(", ", required)
                    + "; Current: "
                    + ((Developer) user).getSeniority().name()
                    + ".";

            return buildErrorResult(errorMessage);
        }

        // 3. Open ticket validation
        if (!ticket.isOpen()) {
            errorMessage = "Only OPEN tickets can be assigned.";
            return buildErrorResult(errorMessage);
        }

        // 4. Milestone assignment validation
        final MilestoneService milestoneService = commandContext.getMilestoneService();
        final Milestone ticketMilestone = milestoneService.getMilestoneForTicket(ticketId);

        if (ticketMilestone != null && !ticketMilestone.isAssignedTo(user.getUsername())) {
            errorMessage = "Developer "
                    + user.getUsername()
                    + " is not assigned to milestone "
                    + ticketMilestone.getName()
                    + ".";

            return buildErrorResult(errorMessage);
        }

        // 5. Ticket from blocked milestone validation
        if (ticketMilestone != null && ticketMilestone.getIsBlocked()) {
            errorMessage = "Cannot assign ticket "
                    + ticket.getId()
                    + " from blocked milestone "
                    + ticketMilestone.getName()
                    + ".";
            return buildErrorResult(errorMessage);
        }

        return null; // validation passed
    }
}
