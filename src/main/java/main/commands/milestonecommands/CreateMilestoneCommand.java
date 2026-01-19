package main.commands.milestonecommands;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.milestone.Milestone;
import main.milestone.MilestoneService;
import main.parsing.ParserUtils;
import main.ticket.Ticket;
import main.ticket.actions.TicketActionFactory;
import main.ticket.utils.HistoryManager;
import main.users.enums.common.Role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static main.common.SystemConstants.TICKETS_FIELD;

public final class CreateMilestoneCommand extends Command {
    private final JsonNode data;
    private final List<Integer> ticketIds;

    public CreateMilestoneCommand(final JsonNode node) {
        super(node);
        this.data = node;
        this.ticketIds = ParserUtils.parseIntegerArray(node.get(TICKETS_FIELD));
    }
    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER);
    }

    /**
     * Validates whether the tickets targeted by the command can be assigned
     * without conflicting with an existing milestone.
     * The validation checks if any of the provided ticket IDs are already
     * associated with another milestone. If a conflict is detected, the method
     * identifies the overlapping ticket IDs and returns an error
     * {@link CommandResult} describing the conflict.
     * If no conflicting milestone is found, the validation succeeds and
     * {@code null} is returned.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} containing an error message if a conflict
     *         exists, or {@code null} if the validation passes successfully
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        Optional<Milestone> milestoneWithConflict =
                commandContext.getMilestoneService()
                              .getConflictingMilestone(ticketIds);

        if (milestoneWithConflict.isPresent()) {
            List<Integer> conflictingMilestoneTicketIds = milestoneWithConflict.get().getTickets();
            List<Integer> commonTicketIds = conflictingMilestoneTicketIds
                    .stream()
                    .filter(ticketIds::contains)
                    .toList();

            String result = commonTicketIds
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            final String errorMessage = "Tickets " + result + " already assigned to milestone "
                    + milestoneWithConflict.get().getName()
                    + ".";

            return buildErrorResult(errorMessage);
        }
        return null;
    }

    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        MilestoneService milestoneService = commandContext.getMilestoneService();
        Milestone milestoneCreated = milestoneService.createMilestone(data);
        HistoryManager historyManager = commandContext.getHistoryManager();

        List<Ticket> associatedTicketsForMilestone = commandContext
                .getTicketContainer()
                .getAllTicketsByIds(milestoneCreated.getTickets());

        if (!associatedTicketsForMilestone.isEmpty()) {
            associatedTicketsForMilestone
                    .forEach(ticket ->
                                    historyManager.addToTicketHistory(ticket.getId(),
                                            TicketActionFactory.addedToMilestone(
                                                this.username,
                                                this.timestamp,
                                                milestoneCreated.getName()
                                            )
                                    )
                            );
        }

        return null; // no output;
    }
}
