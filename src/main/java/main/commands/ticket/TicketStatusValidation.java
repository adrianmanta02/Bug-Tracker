package main.commands.ticket;

import main.commands.CommandValidationStrategy;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;

/**
 * main.commands.strategies.status.TicketStatusValidation is responsible
 * for handling {@link StatusCommand}'s inheritors in terms of validation
 * for a given ticket to change its status (do or undo).
 */
public final class TicketStatusValidation
        implements CommandValidationStrategy<StatusCommand> {
    /**
     * @see CommandValidationStrategy#validate(CommandContext, main.commands.Command)
     */
    @Override
    public CommandResult validate(final CommandContext commandContext,
                                  final StatusCommand command) {
        Ticket ticketToChange = command.getTicketToChange();
        final String errorMessage;
        if (!ticketToChange.isAssignedTo(command.getUsername())) {
            errorMessage = "Ticket " + command.getTicketId() + " is not assigned to developer "
                    + command.getUsername() + ".";
            return command.buildErrorResult(errorMessage);
        }

        return null;
    }
}
