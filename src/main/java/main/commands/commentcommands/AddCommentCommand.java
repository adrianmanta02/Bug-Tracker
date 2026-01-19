package main.commands.commentcommands;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import main.commands.Command;
import main.commands.CommandValidationStrategy;
import main.commands.context.CommandContext;
import main.commands.context.CommandResult;
import main.commands.strategies.comment.AddCommentValidationFactory;

import main.comment.Comment;
import main.ticket.Ticket;
import main.users.enums.common.Role;

import java.util.List;

import static main.common.SystemConstants.*;

@Getter
public final class AddCommentCommand extends Command {
    private Ticket ticket;
    private final int ticketId;
    private final String commentContent;

    public AddCommentCommand(final JsonNode node) {
        super(node);
        this.ticketId = node.get(TICKET_ID_FIELD).asInt();
        this.commentContent = node.get(COMMENT_FIELD).asText();
    }

    /**
     * @see Command#getAllowedRoles()
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.REPORTER);
    }

    /**
     * Checks the context before directly running the command.
     * Trying to leave a {@link Comment} on a non-existent {@link Ticket}
     * will result in ignoring the command. The method also manages
     * the case of commenting on an anonymous ticket or leaving a comment
     * which is too short. Further validations are being made taking into account
     * the role of the invoker requesting to add a comment.
     * @param commandContext the context in which the command is executed
     *                       providing necessary environment or state
     * @return a {@link CommandResult} with the reason of failure for the command
     * or null if all validations passed.
     */
    @Override
    public CommandResult validateCommand(final CommandContext commandContext) {
        this.ticket = commandContext.getTicketContainer().getTicketById(ticketId);

        if (ticket == null) {
            return null;
        }

        if (ticket.isAnonymous()) {
            return buildErrorResult("Comments are not allowed on anonymous tickets.");
        }

        if (commentContent.length() < MINIMUM_COMMENT_LENGTH) {
            return buildErrorResult("Comment must be at least 10 characters long.");
        }

        CommandValidationStrategy<AddCommentCommand> validationStrategy =
                AddCommentValidationFactory.create(this.getInvoker());

        return validationStrategy.validate(commandContext, this);
    }

    /**
     * @see Command#execute(CommandContext)
     */
    @Override
    public CommandResult execute(final CommandContext commandContext) {
        if (this.ticket == null) {
            return null;
        }

        Comment comment = new Comment(this.getUsername(), commentContent, getTimestamp());
        this.ticket.addComment(comment);

        return null; // no output
    }
}
