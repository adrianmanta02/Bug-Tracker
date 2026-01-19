package main.commands.commentcommands;

import com.fasterxml.jackson.databind.JsonNode;
import main.commands.Command;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.ticket.Ticket;
import main.ticket.utils.TicketContainer;
import main.users.enums.common.Role;
import java.util.List;

import static main.common.SystemConstants.TICKET_ID_FIELD;

public final class UndoAddCommentCommand extends Command {
    private Ticket ticket;
    private final int ticketId;
    public UndoAddCommentCommand(final JsonNode node) {
        super(node);
        this.ticketId = node.get(TICKET_ID_FIELD).asInt();
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.REPORTER, Role.DEVELOPER);
    }

    /**
     * Checks the context for the ticket that has the comment on.
     * Providing no ticket will result in the command being ignored.
     * An anonymous ticket is not allowed to have comments, as per
     * {@link AddCommentCommand}. Furthermore, checks are being done
     * on the invoker leaving comments on the verified ticket.
     * @param commandContext the context in which the command is executed
     *                         providing necessary environment or state
     * @return a {@link CommandResult} encapsulating the validation error
     * resulted or null if all validations passed.
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        this.ticket = TicketContainer.getInstance().getTicketById(ticketId);
        if (this.ticket == null) {
            return null;
        }

        if (ticket.isAnonymous()) {
            final String errorMessage = "Comments are not allowed on anonymous tickets.";
            return this.buildErrorResult(errorMessage);
        }

        boolean userHasComments = ticket.getComments()
                .stream()
                .anyMatch(comment -> comment.getAuthor().equals(username));

        if (!userHasComments) {
            return null;
        }

        return null;
    }

    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        if (ticket == null) {
            return null;
        }

        ticket.getComments()
                .stream()
                .filter(comment -> comment.getAuthor().equals(this.username))
                .reduce((first, second) -> second)
                .ifPresent(ticket::removeComment);
        return null;
    }
}
